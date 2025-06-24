import AppCommon
import Domain
import Factory
import SwiftUI

extension OfferDetaillScreen {

    enum Navigation: Equatable {
        case close
        case closeIfRegister
    }

    enum Event {
        case onViewDidLoad
        case didNavigate
        case cancel
        case submit

    }

    struct UIState {
        var loading: Bool = false
        var alert: AlertUIState?
        var navigation: Navigation?
        var offer: Offer?
        var presentationCard: String = ""
    }

    @Observable @MainActor
    final class ViewModel {
        var state: UIState
        let offerId: Int
        private var hasLoaded = false
        private var offerRepository: OfferRepository
        private var candidacyRepository: CandidacyRepository

        init(offerId: Int, container: Container = .shared) {
            state = .init()
            self.offerId = offerId
            self.offerRepository = container.offerRepository()
            self.candidacyRepository = container.candidacyRepository()
        }

        func handle(_ event: Event) {
            switch event {
            case .onViewDidLoad:
                if !hasLoaded {
                    hasLoaded = true
                    Task { await loadData() }
                }
            case .didNavigate:
                state.navigation = nil
            case .cancel:
                state.navigation = .close
            case .submit:
                showConfirmationAlert()
            }
        }

        private func showConfirmationAlert() {
            state.alert = AlertUIState(
                title: Strings.general.candidacy_confirm_register_text,
                message: Strings.general.candidacy_register_question_text,
                actions: [
                    .destructive(title: Strings.general.candidacy_register_text, action: {
                        Task { await self.submit() }
                        self.state.navigation = .closeIfRegister
                    }),
                    .cancel()
                ]
            )
        }

        func submit() async {
            let repository = candidacyRepository
            let params = CandidacyRequestParams(
                offerId: offerId,
                presentationCard: state.presentationCard
            )

            do {
                try await repository.createCandidacy(params)

                await loadData()

            } catch {
                await MainActor.run {
                    state.alert = .init(error: error)
                }
            }
        }

        func loadData() async {
            let repository = offerRepository

            do {
                let offer = try await repository.getOfferDetail(id: offerId)

                await MainActor.run {
                    state.offer = offer
                }
            } catch {
                await MainActor.run {
                    state.alert = .init(error: error)
                }
            }
        }
    }
}
