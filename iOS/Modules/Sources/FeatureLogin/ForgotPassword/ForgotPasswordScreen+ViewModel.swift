import AppCommon
import Domain
import Factory
import SwiftUI

extension ForgotPasswordScreen {
    enum Navigation: Equatable {
        case close
    }

    enum Event {
        case onViewDidLoad
        case didNavigate
        case cancel
        case submit
    }

    struct UIState {
        var email: InputState<String> = .init(
            value: "",
            validator: .and(
                .mandatory(message: Strings.general.common_mandatory_text, ignoreSpaces: true),
                .email(message: Strings.general.register_invalid_email_text)
            )
        )
        var loading: Bool = false
        var alert: AlertUIState?
        var actionSheet: ActionSheetUIState?
        var navigation: Navigation?
    }
    @Observable @MainActor
    final class ViewModel {
        var state: UIState
        private var authRepository: AuthRepository
        // dependencies here...
        init(container: Container = .shared) {
            self.authRepository = container.authRepository()
            state = .init()
            // initialize dependencies from container here...
        }
        func handle(_ event: Event) {
            switch event {
            case .onViewDidLoad:
                loadData()
            case .didNavigate:
                state.navigation = nil
            case .cancel:
                state.navigation = .close
            case .submit:
                guard validate() else { return }
                sendResetLink()
            }
        }
        private func validate() -> Bool {
            state.email.validate()
        }
        func sendResetLink() {
            Task {
                state.loading = true
                defer { state.loading = false }

                do {
                    guard let email = state.email.value, !email.isEmpty else {
                        state.alert = .init(
                            title: Strings.general.common_error_text,
                            message: Strings.general.register_fields_completed_text
                        )
                        return
                    }

                    try await authRepository.sendResetPassword(email: email)

                    state.alert = .init(
                        title: Strings.general.common_forgot_password_success_title_text,
                        message: String(format: Strings.general.common_forgot_password_success_message_text, email),
                        action: .default(
                            title: Strings.general.common_accept_text,
                            action: { self.state.navigation = .close }
                        )
                    )
                } catch {
                    state.alert = .init(
                        title: Strings.general.common_error_text,
                        message: (error as? AppError)?.message ?? Strings.general.error_unknow_error_text
                    )
                }        }
        }
            func loadData() {
                /*
                 Task {
                 defer { state.loading = false }
                 state.loading = true
                 do {
                 // Call to your use case or repository here...
                 } catch {
                 // Handle error...
                 state.alert = .init(error: error)
                 }
                 }
                 */
            }
        }
    }
