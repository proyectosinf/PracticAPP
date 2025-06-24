import AppCommon
import Domain
import Factory
import SwiftUI

extension WorkTutorOffersScreen {
    enum Navigation: Equatable {
        case close
        case goToCreateOffer
        case goToCandidacies(offer: Offer)
    }
    enum Event {
        case onViewDidLoad
        case didNavigate
        case cancel
        case createOfferScreen
        case candidacies(offer: Offer)
        case loadMore
        case onRefresh
    }
    struct UIState {
        var loading: Bool = false
        var alert: AlertUIState?
        var actionSheet: ActionSheetUIState?
        var navigation: Navigation?
    }
    @Observable @MainActor
    final class ViewModel {
        private var currentPage: Int = 0
        private var canLoadMore: Bool = true
        private var isLoadingMore: Bool = false
        var state: UIState
        var offers: [OfferUIModel] = []
        var hasLoaded: Bool = false
        private var offerRepository: OfferRepository
        private var degreeRepository: DegreeRepository

        // dependencies here...
        init(container: Container = .shared) {
            self.offerRepository = container.offerRepository()
            self.degreeRepository = container.degreesRepository()
            state = .init()

            // initialize dependencies from container here...
        }
        func handle(_ event: Event) {
            switch event {
            case .onViewDidLoad:
                loadData(reset: true)
            case .didNavigate:
                state.navigation = nil
            case .cancel:
                state.navigation = .close
            case .createOfferScreen:
                state.navigation = .goToCreateOffer
            case .loadMore:
                loadData()
            case .candidacies(let offer):
                state.navigation = .goToCandidacies(offer: offer)
            case .onRefresh:
                loadData(reset: true)
            }
        }
        func loadData(reset: Bool = false) {
            Task {
                defer {
                    state.loading = false
                    hasLoaded = true
                }
                if reset {
                    currentPage = 0
                    canLoadMore = true
                    offers = []
                }
                guard !isLoadingMore, canLoadMore else { return }
                isLoadingMore = true
                state.loading = currentPage == 0
                do {
                    let response = try await offerRepository.getOffers(page: currentPage)
                    let calendar = Calendar.current
                    let now = Date()
                    let mapped = response.items.map { offer in
                        let daysUntilStart = calendar.dateComponents([.day], from: now, to: offer.startDate).day ?? 0
                        let isActive = daysUntilStart >= 15
                        return OfferUIModel(
                            id: offer.id,
                            title: offer.title,
                            formation: offer.degree,
                            vacancies: offer.vacanciesNumber,
                            modality: offer.type.description,
                            degree: offer.degree,
                            views: offer.views,
                            isActive: isActive,
                            inscriptionCandidacy: offer.inscriptionCandidacy
                        )
                    }
                    offers += mapped
                    canLoadMore = offers.count < response.total
                    currentPage += 1
                } catch {
                    state.alert = .init(error: error)
                }
                isLoadingMore = false
            }
        }
    }
}
