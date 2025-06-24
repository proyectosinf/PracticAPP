import AppCommon
import Factory
import FoundationUtils
import SwiftUI
import AppModels

struct SplashScreen: View {
    typealias Path = String
    @State private var viewModel: ViewModel
    @Environment(\.appCoordinator) private var coordinator: AppCoordinator?
    init() {
        _viewModel = .init(wrappedValue: .init())
    }
    var body: some View {
        Content(state: $viewModel.state) { event in
            viewModel.handle(event)
        }
        .alert(model: $viewModel.state.alert)
        .onChange(of: viewModel.state.navigation) { _, newValue in
            guard let newValue else { return }
            switch newValue {
            case .next:
                withAnimation { coordinator?.root = .tabBar(role: .student)}
            }
            viewModel.handle(.didNavigate)
        }
    }
    struct Content: View {
        @Binding var state: SplashScreen.UIState
        @Environment(\.openURL) private var openURL
        let event: (Event) -> Void
        var body: some View {
            VStack {
                Spacer()
                Image("practicapp")
                    .resizable()
                    .aspectRatio(1, contentMode: .fill)
                    .frame(width: 250, height: 250)
                    .clipShape(Rectangle())
                    .shadow(radius: 4)
                    .padding(.top, 20)
                if state.loading {
                    ProgressView()
                        .padding(.top, 220)
                }
                Spacer()
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(Color.white.ignoresSafeArea())
            .updateApp(
                appUpdate: state.appUpdate,
                openURLAction: { event(.openAppStore(openURL)) },
                dismissAction: { event(.dismissUpdate) }
            )
            .onViewDidLoad { event(.onViewDidLoad) }
        }
    }
}

#if DEBUG
private extension SplashScreen {
    static func preview(state: Binding<SplashScreen.UIState>) -> some View {
        NavigationStack { Content(state: state, event: { _ in }) }
    }
}

#Preview("Mock") {
    @Previewable @State var state: SplashScreen.UIState = .init(version: "1.0.0")
    SplashScreen.preview(state: $state)
}

#Preview("Force Update") {
    @Previewable @State var state: SplashScreen.UIState = .init(appUpdate: .mockForce, version: "1.0.0")
    SplashScreen.preview(state: $state)
}

#Preview("Force Update optional") {
    @Previewable @State var state: SplashScreen.UIState = .init(appUpdate: .mockOptional, version: "1.0.0")
    SplashScreen.preview(state: $state)
}
#endif
