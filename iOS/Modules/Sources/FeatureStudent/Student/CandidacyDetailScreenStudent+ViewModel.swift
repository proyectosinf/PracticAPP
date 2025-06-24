import AppCommon
import Domain
import Factory
import SwiftUI

extension CandidacyDetailScreenStudent {
    enum Navigation: Equatable {
        case close
    }

    enum Event {
        case onViewDidLoad
        case didNavigate
        case cancel
    }
    struct UIState {
        var loading: Bool = false
        var alert: AlertUIState?
        var actionSheet: ActionSheetUIState?
        var navigation: Navigation?
        var candidacy: Candidacy?
    }
    @Observable @MainActor
    final class ViewModel {
        var state: UIState
        var candidacyId: Int
        private let candidacyRepository: CandidacyRepository

        init(candidacyId: Int, container: Container = .shared) {
            state = .init()
            self.candidacyId = candidacyId
            self.candidacyRepository = container.candidacyRepository()
        }
        func handle(_ event: Event) {
            switch event {
            case .onViewDidLoad:
                loadData()
            case .didNavigate:
                state.navigation = nil
            case .cancel:
                state.navigation = .close
            }
        }
        func loadData() {
            let repository = candidacyRepository
            Task {
                await MainActor.run { state.loading = true }
                defer { Task { @MainActor in state.loading = false } }
                do {
                    let candidacy = try await repository.getCandidacyById(id: candidacyId)
                    await MainActor.run { state.candidacy = candidacy }
                } catch {
                    await MainActor.run { state.alert = .init(error: error) }
                }
            }
        }
    }
}
