import AppCommon
import Domain
import Factory
import SwiftUI
import Logger

extension StudentCandidacyScreen {
    enum Navigation: Equatable {
        case close
        case goToDetaill(candidacyId: Int)
    }
    enum Event {
        case onViewDidLoad
        case didNavigate
        case cancel
        case onRefresh
        case didSelectCandidacy(id: Int)
        case loadNextPage
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
        private var currentPage = 0
        private var isLoadingPage = false
        private var hasMorePages = true
        private let candidacyRepository: CandidacyRepository
        private let logger: Logger
        init(container: Container = .shared) {
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
                refreshAll()
            case .didSelectCandidacy(let id):
                state.navigation = .goToDetaill(candidacyId: id)
            case .loadNextPage:
                loadNextPage()
            }
        }
        func loadData() {
            loadFirstPage()
        }
        private func loadFirstPage() {
            currentPage = 0
            candidacies.removeAll()
            hasMorePages = true
            loadNextPage()
        }
        private func refreshAll() {
            loadFirstPage()
        }
        private func loadNextPage() {
            guard !isLoadingPage, hasMorePages else { return }
            isLoadingPage = true
            state.loading = currentPage == 0
            Task {
                do {
                    let response = try await candidacyRepository.getMeAllCandidacies(page: currentPage)
                    await MainActor.run {
                        candidacies.append(contentsOf: response.items)
                        if response.items.isEmpty {
                            hasMorePages = false
                        } else {
                            currentPage += 1
                        }
                    }
                } catch {
                    await MainActor.run {
                        state.alert = .init(error: error)
                        hasMorePages = false
                    }
                }
                await MainActor.run {
                    self.state.loading = false
                    self.isLoadingPage = false
                }
            }
        }
    }
}
