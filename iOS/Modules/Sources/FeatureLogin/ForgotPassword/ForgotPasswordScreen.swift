import AppCommon
import Data
import Factory
import FoundationUtils
import SwiftUI

struct ForgotPasswordScreen: View {
    @State private var viewModel: ViewModel
    @Environment(\.forgotPasswordCoordinator) private var coordinator: ForgotPasswordCoordinator?

    init() {
        _viewModel = .init(wrappedValue: .init())
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
            case .close:
                coordinator?.dismiss()
            }
            viewModel.handle(.didNavigate)
        }
    }
    struct Content: View {
        @Binding var state: UIState
        let event: (Event) -> Void
        var body: some View {
            ScrollView {
                VStack(spacing: .spacingXL) {
                    VStack(spacing: .spacingL) {
                        Image("practicapp")
                            .resizable()
                            .frame(width: 130, height: 130)
                            .aspectRatio(contentMode: .fit)
                            .clipShape(Rectangle())
                            .padding(.bottom, .spacingM)
                        Text(Strings.general.login_forgot_password_title_text)
                            .font(.title2)
                            .fontWeight(.semibold)
                            .frame(maxWidth: .infinity, alignment: .center)
                        Text(Strings.general.login_password_subtitle_text)
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                            .frame(maxWidth: .infinity, alignment: .center)
                        TextFieldInput(
                            title: "",
                            state: $state.email,
                            placeholder: Strings.general.login_email_text,
                            style: .outlined(.gray)
                        )
                        .keyboardType(.emailAddress)
                    }
                    Button(Strings.general.login_password_send_text) { event(.submit) }
                        .buttonStyle(.primary)
                        .padding(.spacingS)
                }
                .padding()
            }
            .onViewDidLoad { event(.onViewDidLoad) }
            .navigationTitle(Strings.general.login_password_nav_title_text)
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(Strings.general.common_cancel_text) { event(.cancel) }
                }
            }
        }
    }
}

#if DEBUG
private extension ForgotPasswordScreen {
    static func preview(state: Binding<ForgotPasswordScreen.UIState>) -> some View {
        NavigationStack { Content(state: state, event: { _ in }) }
    }
}

#Preview("Initial State") {
    @Previewable @State var state: ForgotPasswordScreen.UIState = .init()
    ForgotPasswordScreen.preview(state: $state)
}
#endif
