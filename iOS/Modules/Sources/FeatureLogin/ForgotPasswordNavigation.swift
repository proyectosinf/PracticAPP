import AppCommon
import SwiftUI

public struct ForgotPasswordNavigation: View {
    @State private var coordinator: ForgotPasswordCoordinator

    public init(_ isPresented: Binding<Bool>?) {
        coordinator = .init(isPresented: isPresented)
    }

    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .fotgotPassword)
                .navigationDestination(for: ForgotPasswordPath.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.forgotPasswordCoordinator, coordinator)
    }

    @ViewBuilder
    func build(screen: ForgotPasswordPath) -> some View {
        switch screen {
        case .fotgotPassword:
            ForgotPasswordScreen()
        }
    }

    @ViewBuilder
    func build(sheet: ForgotPasswordSheet) -> some View {
        switch sheet {
        case .mock:
            ForgotPasswordNavigation($coordinator.sheet.toBool())
        }
    }

    @ViewBuilder
    func build(fullScreen: ForgotPasswordFullScreen) -> some View {
        switch fullScreen {
        case .mock:
            ForgotPasswordNavigation($coordinator.fullScreen.toBool())
        }
    }
}

#if DEBUG
#Preview("Root") {
    ForgotPasswordNavigation(nil)
}

#Preview("Sheet") {
    @Previewable @State var isPresented = true
    Button("Present") { isPresented.toggle() }
        .sheet(isPresented: $isPresented) {
            ForgotPasswordNavigation($isPresented)
        }
}
#endif
