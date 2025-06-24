import AppCommon
import SwiftUI

public struct HomeCompanyNavigation: View {
    @State private var coordinator: HomeCompanyCoordinator

    public init(_ isPresented: Binding<Bool>?) {
        coordinator = .init(isPresented: isPresented)
    }

    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .home)
                .navigationDestination(for: HomeCompanyPath.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.homeCompanyCoordinator, coordinator)
    }

    @ViewBuilder
    func build(screen: HomeCompanyPath) -> some View {
        switch screen {
        case .home:
            HomeCompanyScreen()
        }
    }

    @ViewBuilder
    func build(sheet: HomeCompanySheet) -> some View {
        switch sheet {
        case .mock:
            HomeCompanyNavigation($coordinator.sheet.toBool())
        }
    }

    @ViewBuilder
    func build(fullScreen: HomeCompanyFullScreen) -> some View {
        switch fullScreen {
        case .mock:
            HomeCompanyNavigation($coordinator.fullScreen.toBool())
        }
    }
}

#if DEBUG
#Preview("Root") {
    HomeCompanyNavigation(nil)
}

#Preview("Sheet") {
    @Previewable @State var isPresented = true
    Button("Present") { isPresented.toggle() }
        .sheet(isPresented: $isPresented) {
            HomeCompanyNavigation($isPresented)
        }
}
#endif
