import AppCommon
import Data
import Factory
import FoundationUtils
import SwiftUI
import AppModels

public struct RegisterCompanyScreen: View {
    @State private var viewModel: ViewModel
    @Injected(\.userSession) private var userSession
    @Environment(\.appCoordinator) private var appCoordinator: AppCoordinator?
    public init() {
        _viewModel = .init(wrappedValue: .init())
    }
    public var body: some View {
        Content(state: $viewModel.state) { event in
            viewModel.handle(event)
        }
        .alert(model: $viewModel.state.alert)
        .actionSheet(model: $viewModel.state.actionSheet)
        .onChange(of: viewModel.state.navigation) { _, newValue in
            guard let newValue else { return }

            switch newValue {
            case .close:
                break
            case .goToHome:
                Task {
                        await MainActor.run {
                            appCoordinator?.root = .splash
                        }
                        appCoordinator?.start()
                    }
            case .goToLogin:
                appCoordinator?.root = .login
            }

            viewModel.handle(.didNavigate)
        }
    }
    struct Content: View {
        @Binding var state: UIState
        let event: (Event) -> Void

        var body: some View {
            ScrollView {
                VStack {
                    Spacer(minLength: 80)
                    Text(Strings.general.company_companyreg1_text)
                        .font(.title)
                    Spacer(minLength: 10)
                    Text(Strings.general.company_companyreg2_text)
                        .font(.title)
                    VStack(spacing: .spacingM) {
                        Section {
                            TextFieldInput(
                                title: Strings.general.company_name_label_text,
                                state: $state.name,
                                placeholder: Strings.general.common_mandatory_text,
                                isSecure: false
                            )
                        }
                        Section {
                            TextFieldInput(
                                title: Strings.general.company_sector_label_text,
                                state: $state.sector,
                                placeholder: Strings.general.common_mandatory_text,
                                isSecure: false
                            )
                        }
                        Section {
                            TextFieldInput(
                                title: Strings.general.company_web_label_text,
                                state: $state.web,
                                placeholder: "http://www.example.com",
                                isSecure: false
                            )
                        }
                        Section {
                            TextFieldInput(
                                title: Strings.general.company_cif_label_text,
                                state: $state.cif,
                                placeholder: "C12345678 or 87654321C",
                                isSecure: false
                            )
                        }
                        Button(Strings.general.company_create_button) {
                            event(.submit)
                        }
                        .buttonStyle(.primary)
                        .padding(.top, 30)
                    }
                    .padding(30)
                    .frame(maxWidth: 600)

                    Spacer(minLength: 0)
                }
                .frame(maxHeight: .infinity)
                .frame(maxWidth: .infinity)
                .listSectionSpacing(.spacingS)
                .scrollDismissesKeyboard(.immediately)
                .disabled(state.loading)
            }
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button {
                        event(.logoutConfirmationRequested)
                        event(.cancel)
                    } label: {
                        Image(systemName: "rectangle.portrait.and.arrow.right")
                            .foregroundColor(.red)
                    }
                    .accessibilityLabel(Strings.general.company_logout_text_text)
                }
            }
            .onViewDidLoad {
                event(.onViewDidLoad)
            }
        }

    }

}

#if DEBUG
private extension RegisterCompanyScreen {
    static func preview(state: Binding<RegisterCompanyScreen.UIState>) -> some View {
        NavigationStack { Content(state: state, event: { _ in }) }
    }
}

#Preview("Initial State") {
    @Previewable @State var state: RegisterCompanyScreen.UIState = .init()
    RegisterCompanyScreen.preview(state: $state)
}
#endif
