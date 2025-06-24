import AppCommon
import SwiftUI

public struct SampleNavigation: View {
    @State private var coordinator: SampleCoordinator

    public init(_ isPresented: Binding<Bool>?) {
        coordinator = .init(isPresented: isPresented)
    }

    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .path1)
                .navigationDestination(for: SamplePath.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.sampleCoordinator, coordinator)
    }

    @ViewBuilder
    func build(screen: SamplePath) -> some View {
        switch screen {
        case .path1:
            SampleView(title: "Sample 1")
        case .path2:
            SampleView(title: "Sample 2")
        case .path3:
            SampleView(title: "Sample 3")
        }
    }

    @ViewBuilder
    func build(sheet: SampleSheet) -> some View {
        switch sheet {
        case .sheet:
            SampleNavigation($coordinator.sheet.toBool())
        }
    }

    @ViewBuilder
    func build(fullScreen: SampleFullScreen) -> some View {
        switch fullScreen {
        case .fullScreen:
            SampleNavigation($coordinator.fullScreen.toBool())
        }
    }
}

#if DEBUG
#Preview {
    @Previewable @State var isPresented = true
    SampleNavigation($isPresented)
}
#endif
