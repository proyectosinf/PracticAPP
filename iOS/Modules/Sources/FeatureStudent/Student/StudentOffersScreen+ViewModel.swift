import AppCommon
import Domain
import Factory
import SwiftUI
import Logger
import Combine

extension StudentOffersScreen {
    enum Navigation: Equatable {
        case close
        case goToOfferDetail(Int)
    }

    enum Event {
        case onViewDidLoad
        case didNavigate
        case cancel
        case logout
        case logoutConfirmationRequested
        case offerDetail(Int)
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
        private var hasLoaded = false
        var state: UIState
        private var currentPage: Int = 0
        var canLoadMore: Bool = true
        var isLoadingMore: Bool = false
        var offers: [StudentOfferUIModel] = []
        let appCoordinator: AppCoordinator
        private let authRepository: AuthRepository
        private let offerRepository: OfferRepository
        private var logger: Logger
        private var candidacySubscription: AnyCancellable?

        init(
            container: Container = .shared,
            appCoordinator: AppCoordinator,
            didCreateCandidacy: AnyPublisher<Int, Never>
        ) {
            state = .init()
            self.authRepository = container.authRepository()
            self.logger = container.logger()
            self.offerRepository = container.offerRepository()
            self.appCoordinator = appCoordinator

            // Suscripción centralizada aquí:
            self.candidacySubscription = didCreateCandidacy
                .receive(on: DispatchQueue.main)
                .sink { [weak self] offerId in
                    self?.updateCandidacyLocally(offerId: offerId)
                }
        }

        func handle(_ event: Event) {
            switch event {
            case .onViewDidLoad:
                guard !hasLoaded else { return }
                hasLoaded = true
                loadData()
            case .didNavigate:
                state.navigation = nil
            case .cancel:
                state.navigation = .close
            case .logout:
                Task {
                    do {
                        try await authRepository.signOut()
                        appCoordinator.root = .login
                    } catch {
                        state.alert = .init(error: error)
                    }
                }
            case .logoutConfirmationRequested:
                state.actionSheet = ActionSheetUIState(
                    title: Strings.general.logout_confirmation_title_text,
                    message: Strings.general.logout_confirmation_message_text,
                    actions: [
                        .destructive(title: Strings.general.logout_confirm_button, action: { self.handle(.logout) }),
                        .cancel()
                    ]
                )
            case .offerDetail(let id):
                state.navigation = .goToOfferDetail(id)
            case .onRefresh:
                loadData()
            }
        }

        func updateCandidacyLocally(offerId: Int) {
            if let index = offers.firstIndex(where: { $0.id == offerId }) {
                offers[index].isSubscribed = true
            }
        }

        func loadData(reset: Bool = true) {
            Task {
                defer { state.loading = false }
                state.loading = true
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
                    let mapped = response.items.map { offer in
                        let logoUrl: String? = offer.companyPhoto.flatMap { photo in
                            photo.isEmpty ? nil : photo
                        }

                        return StudentOfferUIModel(
                            id: offer.id,
                            title: offer.title,
                            companyName: offer.company,
                            companyLogoUrl: logoUrl,
                            startDate: offer.startDate,
                            endDate: offer.endDate,
                            formation: offer.degree,
                            vacancies: offer.vacanciesNumber,
                            type: offer.type.description,
                            isSubscribed: offer.inscribe ?? false
                        )
                    }
                    offers += mapped
                    canLoadMore = offers.count < response.total
                    currentPage += 1

                } catch {
                    state.alert = .init(error: error)
                }

                isLoadingMore = false
                state.loading = false
            }
        }
    }
}
