import AppCommon
import Data
import Domain
import Factory
import SwiftUI

extension SplashScreen {
    enum Event {
        case onViewDidLoad
        case dismissUpdate
        case openAppStore(_: OpenURLAction)
        case didNavigate
    }

    enum Navigation {
        case next
    }

    struct UIState {
        var appUpdate: AppUpdateUIState?
        var version: String
        var loading: Bool = false
        var alert: AlertUIState?
        var navigation: Navigation?
    }

    @Observable
    class ViewModel {
        var state: UIState
        private let appRepository: AppSettingsRepository
        private let authRepository: AuthRepository
        private let updateAppUseCase: UpdateAppUseCase

        init(container: Container = .shared) {
            state = .init(version: "")
            authRepository = container.authRepository()
            appRepository = container.appSettingsRepository()
            updateAppUseCase = container.updateAppUseCase()
        }

        @MainActor
        func handle(_ event: Event) {
            switch event {
            case .onViewDidLoad:
                loadData()
            case .dismissUpdate:
                state.appUpdate = nil
                state.navigation = .next
            case let .openAppStore(openURL):
                guard let url = state.appUpdate?.url?.toURL else { return }
                openURL(url)
            case .didNavigate:
                state.navigation = nil
            }
        }

        @MainActor
        private func loadData() {
            state.loading = true
            Task {
                defer { state.loading = false }
                state.version = "v\(appRepository.currentVersion)"
                await migrateVersion()
                if let appUpdate = try? await updateAppUseCase.invoke() {
                    state.appUpdate = appUpdate.toUIState
                } else {
                    state.navigation = .next
                }
            }
        }

        private func migrateVersion() async {
            guard let updatingVersion = appRepository.updatingVersion else { return }
            #if DEBUG
            state.version = "Migrando desde \(appRepository.currentVersion) -> \(updatingVersion)"
            #endif
        }
    }
}
