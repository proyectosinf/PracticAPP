import AppCommon
import Data
import Factory
import FoundationUtils
import SwiftUI
import Domain
import AppModels

public struct RegisterScreen: View {
    @StateObject private var viewModel = ViewModel()
    @Injected(\.userSession) private var userSession
    @Environment(\.loginCoordinator) private var loginCoordinator
    @Environment(\.appCoordinator) private var appCoordinator

    public init() {}

    public var body: some View {
        NavigationStack {
            Content(state: $viewModel.state, roles: viewModel.roles) { event in
                viewModel.handle(event)
            }
            .alert(model: $viewModel.state.alert)
            .actionSheet(model: $viewModel.state.actionSheet)
            .onChange(of: viewModel.state.navigation) { _, navigation in
                guard let navigation else { return }

                switch navigation {
                case .goToRegisterCompany:
                    appCoordinator?.root = .registerCompany
                case .goToHome:
                    appCoordinator?.start()
                case .close:
                    loginCoordinator?.dismissFullScreen()
                }
                viewModel.handle(.didNavigate)
            }
        }
    }

    enum Focus: Hashable {
        case email, password, repeatPassword, name, surname, dni, ssn, contactName, contactEmail, contactPhone
    }

    struct Content: View {
        @FocusState private var focus: Focus?
        @Binding var state: UIState
        let roles: [UserRole]
        let event: (Event) -> Void

        var body: some View {
            ZStack {
                VStack {
                    ScrollView {
                        VStack(spacing: .spacingM) {
                            headerView
                                .padding(.top, 20)
                            commonFields
                            roleSpecificFields
                            registerButton
                                .padding(.top, .spacingL)
                            Spacer()
                        }
                        .padding(.horizontal)
                        .padding(.top, .spacingS)
                    }
                    .scrollDismissesKeyboard(.interactively)
                    .onTapGesture { focus = nil }
                    .disabled(state.loading)
                    .background(Color.white.ignoresSafeArea())
                    .toolbar {
                        ToolbarItem(placement: .cancellationAction) {
                            Button {
                                event(.cancel)
                            } label: {
                                Text(Strings.general.common_cancel_text)
                                    .foregroundColor(.red)
                            }
                        }
                    }
                }
            }
        }

        private var headerView: some View {
            VStack(alignment: .center, spacing: 0) {
                Image("practicapp")
                    .resizable()
                    .frame(width: 130, height: 130)
                    .aspectRatio(contentMode: .fit)
                    .foregroundStyle(Color.white)
                    .padding(.spacingXS)
                    .clipShape(Circle())

                VSpacer(height: .spacingS)

                Text(Strings.general.register_statement_text)
                    .font(.title2.bold())

                Text(Strings.general.register_sub_statement_text)
                    .font(.title3.bold())
                    .foregroundStyle(Color.dsOnBackgroundVariant)

                VSpacer(height: .spacingM)
            }
            .multilineTextAlignment(.center)
            .foregroundStyle(Color.dsOnBackground)
            .textCase(.none)
            .frame(maxWidth: .infinity)
        }

        private var commonFields: some View {
            VStack(spacing: .spacingM) {
                rolePicker
                emailField
                passwordField
                repeatPasswordField
                nameField
                surnameField
            }
        }

        private var emailField: some View {
            TextFieldInput(
                title: "\(Strings.general.register_email_label_text) " +
                       Strings.general.common_mandatory_placeholder_text,
                state: $state.email,
                placeholder: ""
            )
            .focused($focus, equals: .email)
            .submitLabel(.next)
        }

        private var passwordField: some View {
            TextFieldInput(
                title: "\(Strings.general.login_password_text) " +
                       Strings.general.common_mandatory_placeholder_text,
                state: $state.password,
                placeholder: "",
                isSecure: true
            )
            .focused($focus, equals: .password)
            .submitLabel(.next)
        }

        private var repeatPasswordField: some View {
            TextFieldInput(
                title: "\(Strings.general.register_confirm_password_label_text) " +
                       Strings.general.common_mandatory_placeholder_text,
                state: $state.repeatPassword,
                placeholder: "",
                isSecure: true
            )
            .focused($focus, equals: .repeatPassword)
            .submitLabel(.next)
        }

        private var nameField: some View {
            TextFieldInput(
                title: "\(Strings.general.register_name_label_text) " +
                       Strings.general.common_mandatory_placeholder_text,
                state: $state.name,
                placeholder: ""
            )
            .focused($focus, equals: .name)
            .submitLabel(.next)
        }

        private var surnameField: some View {
            TextFieldInput(
                title: "\(Strings.general.register_lastname_label_text) " +
                       Strings.general.common_mandatory_placeholder_text,
                state: $state.surname,
                placeholder: ""
            )
            .focused($focus, equals: .surname)
            .submitLabel(.next)
        }

        private var rolePicker: some View {
            Picker(Strings.general.register_role_label_text, selection: $state.selectedRole) {
                ForEach(roles, id: \.self) { role in
                    Text(role.displayName).tag(role)
                }
            }
            .pickerStyle(.segmented)
            .padding(.vertical, .spacingXS)
        }

        private var roleSpecificFields: some View {
            Group {
                if state.selectedRole == .student {
                    studentFields
                }
            }
        }

        private var studentFields: some View {
            VStack(spacing: .spacingM) {
                TextFieldInput(
                    title: Strings.general.register_dni_label_text,
                    state: $state.dni,
                    placeholder: ""
                )
                .focused($focus, equals: .dni)
                .submitLabel(.next)

                TextFieldInput(
                    title: Strings.general.register_social_security_label_text,
                    state: $state.socialSecurityNumber,
                    placeholder: "123456789012"
                )
                .focused($focus, equals: .ssn)
                .submitLabel(.next)

                TextFieldInput(
                    title: Strings.general.register_contact_name_label_text,
                    state: $state.contactName,
                    placeholder: ""
                )
                .focused($focus, equals: .contactName)
                .submitLabel(.next)

                TextFieldInput(
                    title: Strings.general.register_contact_email_label_text,
                    state: $state.contactEmail,
                    placeholder: ""
                )
                .focused($focus, equals: .contactEmail)
                .submitLabel(.next)

                TextFieldInput(
                    title: Strings.general.register_contact_phone_label_text,
                    state: $state.contactPhone,
                    placeholder: ""
                )
                .focused($focus, equals: .contactPhone)
                .submitLabel(.done)
                .onChange(of: state.contactPhone.value) {
                    let filtered = state.contactPhone.value?.filter { "0123456789".contains($0) }
                    if filtered != state.contactPhone.value {
                        state.contactPhone.value = filtered
                    }
                }
            }
        }

        private var registerButton: some View {
            VStack(alignment: .center) {
                Button(Strings.general.register_send_button_button) {
                    event(.submit)
                }
                .buttonStyle(.primary)
                .padding(.horizontal, 20)
            }
            .padding(.top, .spacingL)
        }
    }
}
