import AppCommon
import SwiftUI

public struct LoginNavigation<RegisterScreen: View>: View {
    @State private var coordinator: LoginCoordinator

    let registerScreen: () -> RegisterScreen

    public init(_ isPresented: Binding<Bool>?, registerScreen: @escaping () -> RegisterScreen) {
        coordinator = .init(isPresented: isPresented)
        self.registerScreen = registerScreen
    }

    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .login)
                .navigationDestination(for: LoginPath.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.loginCoordinator, coordinator)
    }

    @ViewBuilder
    func build(screen: LoginPath) -> some View {
        switch screen {
        case .login:
            LoginScreen()
        case .register:
            registerScreen()
        }
    }

    @ViewBuilder
    func build(sheet: LoginSheet) -> some View {
        switch sheet {
        case .test:
            LoginNavigation($coordinator.fullScreen.toBool(), registerScreen: registerScreen)
        case .goToforgotPassword:
            ForgotPasswordNavigation($coordinator.sheet.toBool())
        }
    }

    @ViewBuilder
    func build(fullScreen: LoginFullScreen) -> some View {
        switch fullScreen {
        case .login:
            LoginScreen()
        case .test:
            Text("Test")
        case .register:
            registerScreen()
        }
    }
}

#if DEBUG
#Preview {
    @Previewable @State var isPresented = true
    LoginNavigation($isPresented) { Text("register screen") }
}
#endif
