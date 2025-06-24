import AppCommon
import Combine
import Domain
import Factory
import FoundationUtils
import SwiftUI
import AppModels

extension LoginScreen {
    enum Event {
        case onViewDidLoad
        case cancel
        case submit
        case forgotPassword
        case signUp
        case didNavigate
    }

    enum Navigation: Equatable {
        case loginSuccess
        case cancel
        case signUp
        case goToforgotPassword
    }

    struct UIState {
        var title = ""
        var username: InputState<String> = .init(
            value: "",
            validator: .and(
                .mandatory(message: Strings.general.common_mandatory_text, ignoreSpaces: true),
                .email(message: Strings.general.register_invalid_email_text)
            )
        )
        var password: InputState<String> = .init(
            value: "",
            validator: .password(
                message: Strings.general.register_invalid_password_text,
                invalidCharacterMessage: Strings.general.register_invalid_password_text
            )
        )
        var loading: Bool = false
        var alert: AlertUIState?
        var actionSheet: ActionSheetUIState?
        var navigation: Navigation?
    }

    @Observable @MainActor
    class ViewModel {
        var state: LoginScreen.UIState
        private let authRepository: AuthRepository
        private let appCoordinator: AppCoordinator
        @Injected(\.userSession) @ObservationIgnored private var userSession

        init(
            container: Container = .shared,
            appCoordinator: AppCoordinator = Container.shared.appCoordinator()
        ) {
            state = .init()
            authRepository = container.authRepository()
            self.appCoordinator = appCoordinator
        }

        func handle(_ event: LoginScreen.Event) {
            switch event {
            case .onViewDidLoad:
                break
            case .cancel:
                state.navigation = .cancel
            case .submit:
                signIn()
            case .forgotPassword:
                state.navigation = .goToforgotPassword
            case .didNavigate:
                state.navigation = nil
            case .signUp:
                state.navigation = .signUp
            }
        }

        private func validate() -> Bool {
            [
                state.username.validate(),
                state.password.validate()
            ].allSatisfy { $0 }
        }

        private func signIn() {
            guard validate() else { return }
            state.loading = true

            Task {
                defer { state.loading = false }
                do {
                    let email = state.username.value ?? ""
                    let password = state.password.value ?? ""

                    try await authRepository.signInFirebase(email: email, password: password)

                    let user = try await authRepository.fetchCurrentUser()
                    userSession.set(user: user)
                    guard let role = TabBarRole(rawRole: user.role.rawValue) else {
                        state.alert = .init(
                            title: Strings.general.common_error_text,
                            message: Strings.general.register_error_by_role_null_text
                        )
                        return
                    }
                    state.navigation = .loginSuccess
                    appCoordinator.root = .tabBar(role: role)
                } catch {
                    state.alert = .init(
                        title: Strings.general.common_error_text,
                        message: Strings.general.login_error_credentials_text
                    )

                }
            }
        }
    }
}
