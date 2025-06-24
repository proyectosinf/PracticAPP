import AppCommon
import SwiftUI

public struct SignUpNavigation: View {
    @State private var coordinator: SignUpCoordinator

    public init(_ isPresented: Binding<Bool>?) {
        coordinator = .init(isPresented: isPresented)
    }

    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .mock)
                .navigationDestination(for: SignUpPath.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.signUpCoordinator, coordinator)
    }

    @ViewBuilder
    func build(screen: SignUpPath) -> some View {
        switch screen {
        case .mock:
            List {
                Button("Present sheet") { coordinator.presentSheet(.mock) }
                Button("Present full screen") { coordinator.presentFullScreen(.mock) }
                Button("Dismiss") { coordinator.dismiss() }
            }
            .navigationTitle("SignUp")
        }
    }

    @ViewBuilder
    func build(sheet: SignUpSheet) -> some View {
        switch sheet {
        case .mock:
            SignUpNavigation($coordinator.sheet.toBool())
        }
    }

    @ViewBuilder
    func build(fullScreen: SignUpFullScreen) -> some View {
        switch fullScreen {
        case .mock:
            SignUpNavigation($coordinator.fullScreen.toBool())
        }
    }
}

#if DEBUG
#Preview("Root") {
    SignUpNavigation(nil)
}

#Preview("Sheet") {
    @Previewable @State var isPresented = true
    Button("Present") { isPresented.toggle() }
        .sheet(isPresented: $isPresented) {
            SignUpNavigation($isPresented)
        }
}
#endif
