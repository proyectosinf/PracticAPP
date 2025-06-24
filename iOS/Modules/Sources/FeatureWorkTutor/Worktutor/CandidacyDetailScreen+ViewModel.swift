import AppCommon
import Domain
import Factory
import SwiftUI

extension CandidacyDetailScreen {

    enum Navigation: Equatable {
        case close
    }

    enum Event {
        case onViewDidLoad
        case didNavigate
        case cancel
        case toAcceptCandidacy
        case rejected
    }

    struct UIState {
        var loading: Bool = false
        var alert: AlertUIState?
        var actionSheet: ActionSheetUIState?
        var navigation: Navigation?
        var candidacy: Candidacy?
        var additionalNote: String = ""
    }

    @Observable @MainActor
    final class ViewModel {
        var state: UIState
        let candidacyId: Int
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

            case .toAcceptCandidacy:
                showConfirmationAlert(status: 2)

            case .rejected:
                showConfirmationAlert(status: 3)
            }
        }

        private func showConfirmationAlert(status: Int) {
            let name = "\(state.candidacy?.studentName ?? "") \(state.candidacy?.studentSurname ?? "")"
            let title = Strings.general.candidacy_dialog_confirm_button

            let action = status == 2
                ? Strings.general.candidacy_dialog_accept_confirm_button
                : Strings.general.candidacy_dialog_reject_confirm_button

            let message =
                "\(Strings.general.candidacy_question_text) \(action) " +
                "\(Strings.general.candidacy_question_to_text) \(name)?"

            let confirmTitle = action

            state.alert = .init(
                title: title,
                message: message,
                action: .destructive(title: confirmTitle, action: {
                    Task {
                        if status == 2 {
                            await self.sendDecisionRequest(note: self.state.additionalNote)
                        } else {
                            await self.sendRejectRequest(note: self.state.additionalNote)
                        }
                    }
                })
            )
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

        func sendDecisionRequest(note: String) async {
            let (repository, candidacyId): (CandidacyRepository, Int) = await MainActor.run {
                (self.candidacyRepository, self.candidacyId)
            }
            do {
                try await repository.updateCandidacyState(
                    id: candidacyId,
                    request: .init(additionalNotes: note, status: 2)
                )
                await MainActor.run {
                    state.alert = .init(
                        title: Strings.general.candidacy_update_candidacy_text,
                        message: Strings.general.candidacy_acept_candidacy_alert_text
                    )
                    state.navigation = .close
                }
            } catch {
                await MainActor.run { state.alert = .init(error: error) }
            }
        }

        func sendRejectRequest(note: String) async {
            let (repository, candidacyId): (CandidacyRepository, Int) = await MainActor.run {
                (self.candidacyRepository, self.candidacyId)
            }
            do {
                try await repository.updateCandidacyState(
                    id: candidacyId,
                    request: .init(additionalNotes: note, status: 3)
                )
                await MainActor.run {
                    state.alert = .init(
                        title: Strings.general.candidacy_update_candidacy_text,
                        message: Strings.general.candidacy_reject_candidacy_alert_text
                    )
                    state.navigation = .close
                }
            } catch {
                await MainActor.run { state.alert = .init(error: error) }
            }
        }
    }
}
