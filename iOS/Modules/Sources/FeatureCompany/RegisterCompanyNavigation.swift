import AppCommon
import SwiftUI

public struct RegisterCompanyNavigation: View {
    @State private var coordinator: RegisterCompanyCoordinator

    public init(_ isPresented: Binding<Bool>?) {
        coordinator = .init(isPresented: isPresented)
    }

    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .register)
                .navigationDestination(for: RegisterCompanyPath.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.registerCompanyCoordinator, coordinator)
    }

    @ViewBuilder
    func build(screen: RegisterCompanyPath) -> some View {
        switch screen {
        case .mock:
            List {
                Button("Present sheet") { coordinator.presentSheet(.mock) }
                Button("Present full screen") { coordinator.presentFullScreen(.mock) }
                Button("Dismiss") { coordinator.dismiss() }
            }
            .navigationTitle("RegisterCompany")
        case .register:
            RegisterCompanyScreen()
        }
    }

    @ViewBuilder
    func build(sheet: RegisterCompanySheet) -> some View {
        switch sheet {
        case .mock:
            RegisterCompanyNavigation($coordinator.sheet.toBool())
        }
    }

    @ViewBuilder
    func build(fullScreen: RegisterCompanyFullScreen) -> some View {
        switch fullScreen {
        case .mock:
            RegisterCompanyNavigation($coordinator.fullScreen.toBool())
        }
    }
}

#if DEBUG
#Preview("Root") {
    RegisterCompanyNavigation(nil)
}

#Preview("Sheet") {
    @Previewable @State var isPresented = true
    Button("Present") { isPresented.toggle() }
        .sheet(isPresented: $isPresented) {
            RegisterCompanyNavigation($isPresented)
        }
}
#endif
