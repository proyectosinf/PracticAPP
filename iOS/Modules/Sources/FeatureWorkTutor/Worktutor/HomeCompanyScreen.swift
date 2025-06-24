import AppCommon
import Domain
import Factory
import FoundationUtils
import SwiftUI

public struct HomeCompanyScreen: View {
    @State private var viewModel: ViewModel
    @Environment(\.appCoordinator) private var appCoordinator: AppCoordinator?
    public init() {
        let coordinator = Container.shared.appCoordinator()
        _viewModel = .init(wrappedValue: .init(appCoordinator: coordinator))
    }
    public var body: some View {
        NavigationStack {
            Content(
                state: $viewModel.state,
                event: { event in
                    viewModel.handle(event)
                },
                uploadLogo: { image in
                    viewModel.uploadLogo(image)
                }
            )
            .alert(model: $viewModel.state.alert)
            .actionSheet(model: $viewModel.state.actionSheet)
            .onChange(of: viewModel.state.navigation) { _, newValue in
                guard let newValue else { return }
                switch newValue {
                case .close:
                    viewModel.appCoordinator.root = .tabBar(role: .tutor)
                case .goToLogin:
                    appCoordinator?.root = .login
                case .goToRegisterCompany:
                    appCoordinator?.root = .registerCompany
                }
                viewModel.handle(.didNavigate)
            }
        }
    }
    struct Content: View {
        @Binding var state: UIState
        let event: (Event) -> Void
        let uploadLogo: (UIImage) -> Void
        @State private var isShowingImagePicker = false
        @State private var selectedImage: UIImage?
        @State private var hasLoaded = false
        var body: some View {
            if state.loading && state.company == nil {
                ProgressView(Strings.general.company_loading_company_text)
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
            } else {
                ScrollView {
                    VStack(spacing: .spacingM) {
                        logoSection
                        companyInfoSection
                    }
                    .padding()
                }
                .navigationBarTitleDisplayMode(.inline)
                .toolbar(content: toolbarContent)
                .navigationTitle(Strings.general.company_my_company_label_text_text)
                .onAppear(perform: onAppear)
                .sheet(isPresented: $isShowingImagePicker) {
                    ImagePicker(image: $selectedImage)
                }
                .onChange(of: selectedImage) { _, newValue in
                    if let image = newValue {
                        uploadLogo(image)
                    }
                }
            }
        }
        private var logoSection: some View {
            let logoUrl = (state.company?.logo ?? "").trimmingCharacters(in: .whitespacesAndNewlines)
            return Group {
                if !logoUrl.isEmpty {
                    ZStack {
                        ProgressView()
                        RemoteImage(url: logoUrl)
                            .scaledToFit()
                            .frame(width: 300, height: 200)
                            .clipShape(RoundedRectangle(cornerRadius: 12))
                            .shadow(radius: 4)
                    }
                    .frame(width: 300, height: 200)
                    .onTapGesture {
                        isShowingImagePicker = true
                    }
                } else {
                    Image("practicapp")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 300, height: 200)
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                        .shadow(radius: 4)
                        .onTapGesture {
                            isShowingImagePicker = true
                        }
                }
            }
        }
        private var companyInfoSection: some View {
            Group {
                if let company = state.company {
                    VStack(alignment: .leading, spacing: .spacingS) {
                        LabeledContent(Strings.general.company_company_tab_text, value: company.name)
                        LabeledContent(Strings.general.company_sector_label_text, value: company.sector)
                        if let web = company.web {
                            LabeledContent(Strings.general.company_web_label_text, value: web)
                        }
                        LabeledContent(Strings.general.company_cif_label_text, value: company.cif)
                    }
                    .padding()
                    .background(
                        RoundedRectangle(cornerRadius: 16)
                            .fill(Color(.systemBackground))
                            .shadow(color: Color.black.opacity(0.1), radius: 6, x: 0, y: 2)
                    )
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
            }
        }
        private func toolbarContent() -> some ToolbarContent {
            ToolbarItem(placement: .navigationBarTrailing) {
                Button {
                    event(.logoutConfirmationRequested)
                } label: {
                    Image(systemName: "rectangle.portrait.and.arrow.right")
                        .foregroundColor(.red)
                }
                .accessibilityLabel(Strings.general.company_logout_text_text)
            }
        }
        private func onAppear() {
            if !hasLoaded {
                event(.onViewDidLoad)
                hasLoaded = true
            }
        }
    }
}
#if DEBUG
import Domain

#Preview("Vista ejemplo empresa") {
    HomeCompanyScreen()
}
#endif
