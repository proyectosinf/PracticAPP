import AppCommon
import Data
import Factory
import FoundationUtils
import SwiftUI

struct LoginScreen: View {
    @State private var viewModel: ViewModel
    @Environment(\.loginCoordinator) private var loginCoordinator: LoginCoordinator?

    init() {
        _viewModel = .init(wrappedValue: .init(appCoordinator: Container.shared.appCoordinator()))
    }

    var body: some View {
        Content(state: $viewModel.state) { event in
            viewModel.handle(event)
        }
        .alert(model: $viewModel.state.alert)
        .actionSheet(model: $viewModel.state.actionSheet)
        .onChange(of: viewModel.state.navigation) { _, newValue in
            guard let newValue else { return }
            switch newValue {
            case .loginSuccess:
                loginCoordinator?.dismiss()
            case .cancel:
                loginCoordinator?.dismiss()
            case .goToforgotPassword:
                loginCoordinator?.presentSheet(.goToforgotPassword)
            case .signUp:
                loginCoordinator?.fullScreen = .register
            }
            viewModel.handle(.didNavigate)
        }
    }

    enum Focus: Hashable { case username, password }

    struct Content: View {
        @FocusState var focus: Focus?
        @Binding var state: UIState
        let event: (Event) -> Void

        var body: some View {
            ZStack {
                Color.white.ignoresSafeArea()

                VStack {
                    VStack(spacing: .spacingXS) {
                        headerView
                        usernameField
                        passwordField
                        footerView
                    }
                    .padding(.horizontal, 10)
                    .padding(.top, 60)
                    .onSubmit {
                        switch focus {
                        case .username: focus = .password
                        case .password: focus = nil; event(.submit)
                        case nil: break
                        }
                    }
                    .disabled(state.loading)
                    .ignoresSafeArea(.keyboard)

                    Spacer()
                }

                LoaderView(loading: $state.loading)
            }
            .onTapGesture { focus = nil }
            .onViewDidLoad { event(.onViewDidLoad) }
            .navigationTitle(state.title, displayMode: .automatic)
        }
        var usernameField: some View {
            TextFieldInput(
                title: nil,
                state: $state.username,
                placeholder: Strings.general.register_email_label_text
            )
            .focused($focus, equals: .username)
            .submitLabel(.next)
            .padding(.spacingS)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(Color.gray.opacity(0.3), lineWidth: 1)
            )        }

        var passwordField: some View {
            TextFieldInput(
                title: nil,
                state: $state.password,
                placeholder: Strings.general.login_password_text,
                isSecure: true
            )
            .focused($focus, equals: .password)
            .submitLabel(.join)
            .padding(.spacingS)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(Color.gray.opacity(0.3), lineWidth: 1)
            )
        }

        var headerView: some View {
            VStack(alignment: .center, spacing: 0) {
                Image("practicapp")
                    .resizable()
                    .frame(width: 160, height: 160)
                    .aspectRatio(contentMode: .fit)
                    .foregroundStyle(Color.white)
                    .padding(.spacingXS)
                    .clipShape(Circle())
                VSpacer(height: .spacingXS)
                Text(Strings.general.login_welcome_title_text)
                    .font(.title2.bold())
                Text(Strings.general.login_welcome_body_text)
                    .font(.title3.bold())
                    .foregroundStyle(Color.dsOnBackgroundVariant)
                VSpacer(height: .spacingM)
            }
            .multilineTextAlignment(.center)
            .foregroundStyle(Color.dsOnBackground)
            .textCase(.none)
            .frame(maxWidth: .infinity)
        }

        var footerView: some View {
            VStack(alignment: .center, spacing: .spacingS) {
                Button(Strings.general.login_forgot_password_button) { event(.forgotPassword) }
                    .buttonStyle(.tertiary)

                Button(Strings.general.login_send_button) { event(.submit) }
                    .buttonStyle(.primary)
                    .padding(.spacingS)

                HStack {
                    Text(Strings.general.login_sign_up_text)
                        .foregroundStyle(Color.dsOnSurface)
                    Button(Strings.general.login_sign_up_button) { event(.signUp) }
                        .buttonStyle(.tertiary)
                }
            }
            .frame(maxWidth: .infinity)
        }
    }
}

#if DEBUG
private extension LoginScreen {
    static func preview(state: Binding<LoginScreen.UIState>) -> some View {
        NavigationStack { Content(state: state, event: { _ in }) }
    }
}

#Preview("Mock") {
    @Previewable @State var state: LoginScreen.UIState = .init(title: "")
    LoginScreen.preview(state: $state)
}
#endif
