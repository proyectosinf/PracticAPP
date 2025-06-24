import AppCommon
import Domain
import Factory
import SwiftUI
import Logger

extension CandidacyOffersScreen {
    enum Navigation: Equatable {
        case close
        case goToDetailCandidacy(candidacyId: Int)
    }
        enum Event {
        case onViewDidLoad
        case didNavigate
        case cancel
        case onRefresh
        case detailCandidacy(candidacyId: Int)
    }
    struct UIState {
        var loading: Bool = false
        var alert: AlertUIState?
        var actionSheet: ActionSheetUIState?
        var navigation: Navigation?

    }
    @Observable @MainActor
    final class ViewModel {
        var state: UIState
        var candidacies: [CandidacyListItem] = []
        private let logger: Logger
        let offer: Offer
        private let candidacyRepository: CandidacyRepository

        // dependencies here...
        init(offer: Offer, container: Container = .shared) {
            self.offer = offer
            self.candidacyRepository = container.candidacyRepository()
            self.state = .init()
            self.logger = container.logger()
        }
        func handle(_ event: Event) {
            switch event {
            case .onViewDidLoad:
                loadData()
            case .didNavigate:
                state.navigation = nil
            case .cancel:
                state.navigation = .close
            case .onRefresh:
                loadData()
            case .detailCandidacy(let candidacyId):
                state.navigation = .goToDetailCandidacy(candidacyId: candidacyId)
            }
        }
        func loadData() {
            Task { [candidacyRepository] in
                state.loading = true
                defer { state.loading = false }
                do {
                    let response = try await candidacyRepository.getCandidacies(id: offer.id, page: 0)
                    candidacies = response.items
                } catch {
                    state.alert = .init(error: error)
                }
            }
        }
    }
}
