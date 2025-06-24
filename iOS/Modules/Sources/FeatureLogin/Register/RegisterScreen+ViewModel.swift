import AppCommon
import Domain
import Factory
import SwiftUI
@preconcurrency import FirebaseAuth

extension RegisterScreen {
    enum Navigation: Equatable {
        case close
        case goToHome
        case goToRegisterCompany
    }

    enum Event {
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
        var password: InputState<String> = .init(
            value: "",
            validator: .and(
                .mandatory(message: Strings.general.common_mandatory_text, ignoreSpaces: true),
                .password(message: Strings.general.register_invalid_password_text)
            )
        )
        var repeatPassword: InputState<String> = .init(
            value: "",
            validator: .and(
                .mandatory(message: Strings.general.common_mandatory_text, ignoreSpaces: true),
                .password(message: Strings.general.register_invalid_password_text)
            )
        )
        var name: InputState<String> = .init(
            value: "",
            validator: .and(
                .mandatory(message: Strings.general.common_mandatory_text, ignoreSpaces: true),
                .maxLength(length: 100, message: Strings.general.register_max_length_name_surname_text)
            )
        )
        var surname: InputState<String> = .init(
            value: "",
            validator: .and(
                .mandatory(message: Strings.general.common_mandatory_text, ignoreSpaces: true),
                .maxLength(length: 100, message: Strings.general.register_max_length_name_surname_text)
            )
        )
        var dni: InputState<String> = .init(
            value: "",
            validator: .and(
                .dni(message: Strings.general.register_invalid_dni_text)
            )
        )
        var socialSecurityNumber: InputState<String> = .init(
            validator: .and(
                .maxLength(length: 12, message: Strings.general.register_invalid_social_security_text)
            )
        )
        var contactName: InputState<String> = .init(
            value: "",
            validator: .and(
                .maxLength(length: 100, message: Strings.general.register_max_length_name_surname_text)
            )
        )
        var contactEmail: InputState<String> = .init(
            value: "",
            validator: .and(
                .email(message: Strings.general.register_invalid_email_text)
            )
        )
        var contactPhone: InputState<String> = .init(
            value: "",
            validator: .and(
                .length(9, message: Strings.general.register_invalid_phone_text),
                .regex("^[0-9]{9}$", message: Strings.general.register_invalid_phone_text)
            )
        )
        var securityCode: InputState<String> = .init()
        var companyId: InputState<Int> = .init()
        var selectedRole: UserRole = .student

        var alert: AlertUIState?
        var actionSheet: ActionSheetUIState?
        var navigation: Navigation?
        var loading = false
    }

    @MainActor
    public final class ViewModel: ObservableObject {
        let roles: [UserRole] = UserRole.allCases
        private let authRepository: AuthRepository
        @Published var state: UIState
        private var registerUseCase: RegisterUseCase
        @Injected(\.userSession) @ObservationIgnored private var userSession

        init(container: Container = .shared) {
            state = .init()
            authRepository = container.authRepository()
            self.registerUseCase = container.registerUseCase()
        }

        func handle(_ event: Event) {
            switch event {
            case .didNavigate:
                state.navigation = nil

            case .cancel:
                state.navigation = .close

            case .submit:
                guard validate() else { return }
                submit()
            }
        }

        private func validate() -> Bool {
            let commonValidations = [
                state.email.validate(),
                state.password.validate(),
                state.repeatPassword.validate(),
                state.name.validate(),
                state.surname.validate()
            ]

            let studentValidations = state.selectedRole == .student ? [
                validateDni(),
                state.socialSecurityNumber.validate(),
                validateOptional(&state.contactName),
                validateOptional(&state.contactEmail),
                validateOptional(&state.contactPhone)
            ] : []

            return (commonValidations + studentValidations).allSatisfy { $0 }
        }

        private func validateDni() -> Bool {
            guard let dniValue = state.dni.value,
                    !dniValue.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
            else {
                state.dni.error = nil
                return true
            }

            let result = state.dni.validate()
            if result { state.dni.error = nil }
            return result
        }
        private func validateOptional(_ input: inout InputState<String>) -> Bool {
            guard let value = input.value,
                    !value.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
            else {
                input.error = nil
                return true
            }
            let result = input.validate()
            if result { input.error = nil }
            return result
        }

        func submit() {
            Task {
                state.loading = true
                defer { state.loading = false }

                do {
                    guard
                        let email = state.email.value,
                        let password = state.password.value,
                        let repeatPassword = state.repeatPassword.value,
                        !email.isEmpty, !password.isEmpty
                    else {
                        state.alert = AlertUIState(
                            title: Strings.general.register_validation_error_text,
                            message: Strings.general.register_fields_completed_text
                        )
                        return
                    }

                    guard password == repeatPassword else {
                        state.alert = AlertUIState(
                            title: Strings.general.login_password_text,
                            message: Strings.general.register_passwords_not_match_text
                        )
                        return
                    }

                    let user = User(
                        id: nil,
                        uid: nil,
                        name: state.name.value ?? "",
                        surname: state.surname.value ?? "",
                        email: email,
                        dni: state.selectedRole == .student ? state.dni.value.sanitizedOrNil : nil,
                        socialSecurityNumber: state.selectedRole == .student
                            ? state.socialSecurityNumber.value.sanitizedOrNil
                            : nil,
                        pdfCV: nil,
                        photo: nil,
                        contactName: state.selectedRole == .student ? state.contactName.value.sanitizedOrNil : nil,
                        contactEmail: state.selectedRole == .student ? state.contactEmail.value.sanitizedOrNil : nil,
                        contactPhone: state.selectedRole == .student ? state.contactPhone.value.sanitizedOrNil : nil,
                        companyId: state.selectedRole == .tutor ? nil : nil,
                        role: state.selectedRole
                    )

                    try await registerUseCase.invoke(email: email, password: password, user: user)
                    try await authRepository.signInFirebase(email: email, password: password)
                    let currentUser = try await authRepository.fetchCurrentUser()
                    userSession.set(user: currentUser)

                    state.alert = AlertUIState(
                        title: Strings.general.register_register_success_1_text,
                        message: Strings.general.register_register_success_2_text,
                        action: .default(title: Strings.general.common_accept_text)
                    )

                    if currentUser.role == .tutor, currentUser.companyId == nil {
                        self.state.navigation = .goToRegisterCompany
                    } else {
                        self.state.navigation = .goToHome
                    }

                } catch {
                    state.alert = AlertUIState(
                        title: Strings.general.common_error_text,
                        message: (error as? AppError)?.message ?? Strings.general.register_register_try_again_text
                    )
                }
            }
        }
    }
}
