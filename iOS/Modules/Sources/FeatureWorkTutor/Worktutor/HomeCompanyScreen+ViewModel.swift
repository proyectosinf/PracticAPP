import SwiftUI
import AppCommon
import Domain
import Factory
import Logger

extension HomeCompanyScreen {
    enum Navigation: Equatable {
        case close
        case goToLogin
        case goToRegisterCompany
    }

    enum Event {
        case onViewDidLoad
        case didNavigate
        case cancel
        case logout
        case logoutConfirmationRequested
    }

    struct UIState {
        var company: Company?
        var loading: Bool = false
        var alert: AlertUIState?
        var actionSheet: ActionSheetUIState?
        var navigation: Navigation?
    }

    @Observable @MainActor
    final class ViewModel {
        var state: UIState
        let appCoordinator: AppCoordinator
        private let authRepository: AuthRepository
        private let companyRepository: CompanyRepository
        @Injected(\.userSession) @ObservationIgnored private var userSession
        private var logger: Logger
        init(appCoordinator: AppCoordinator, container: Container = .shared) {
            self.state = .init(company: nil)
            self.authRepository = container.authRepository()
            self.logger = container.logger()
            self.companyRepository = container.companyRepository()
            self.appCoordinator = appCoordinator
        }
        func handle(_ event: Event) {
            switch event {
            case .onViewDidLoad:
                loadCompany()
            case .didNavigate:
                state.navigation = nil
            case .cancel:
                state.navigation = .close
            case .logout:
                logout()
            case .logoutConfirmationRequested:
                state.actionSheet = ActionSheetUIState(
                    title: Strings.general.company_logout_text_text,
                    message: Strings.general.company_areyousure_text,
                    actions: [
                        .destructive(title: Strings.general.company_logout_text_text, action: { self.logout() }),
                        .cancel()
                    ]
                )
            }
        }
        func loadCompany() {
            Task {
                state.loading = true
                defer { state.loading = false }
                do {
                    let company = try await companyRepository.getCompany()
                    self.state.company = company
                } catch {
                    state.alert = .init(error: error)
                }
            }
        }
        func uploadLogo(_ image: UIImage) {
            Task {
                state.loading = true
                defer { state.loading = false }
                do {
                    try await companyRepository.uploadCompanyImage(image)
                    let updatedCompany = try await companyRepository.getCompany()
                    self.state.company = updatedCompany
                } catch {
                    state.alert = .init(error: error)
                }
            }
        }
        private func logout() {
            Task {
                try? await authRepository.signOut()
                userSession.clear()
                state.navigation = .goToLogin
            }
        }
    }
}
