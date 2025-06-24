import AppCommon
import SwiftUI

public struct ___VARIABLE_navigationName___: View {
    @State private var coordinator: ___VARIABLE_coordinatorName___

    public init(_ isPresented: Binding<Bool>?) {
        coordinator = .init(isPresented: isPresented)
    }

    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .mock)
                .navigationDestination(for: ___VARIABLE_name___Path.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.___VARIABLE_coordinatorEnvironmentName___Coordinator, coordinator)
    }

    @ViewBuilder
    func build(screen: ___VARIABLE_name___Path) -> some View {
        switch screen {
        case .mock:
            List {
                Button("Present sheet") { coordinator.presentSheet(.mock) }
                Button("Present full screen") { coordinator.presentFullScreen(.mock) }
                Button("Dismiss") { coordinator.dismiss() }
            }
            .navigationTitle("___VARIABLE_name___")
        }
    }

    @ViewBuilder
    func build(sheet: ___VARIABLE_name___Sheet) -> some View {
        switch sheet {
        case .mock:
            ___VARIABLE_navigationName___($coordinator.sheet.toBool())
        }
    }

    @ViewBuilder
    func build(fullScreen: ___VARIABLE_name___FullScreen) -> some View {
        switch fullScreen {
        case .mock:
            ___VARIABLE_navigationName___($coordinator.fullScreen.toBool())
        }
    }
}

#if DEBUG
#Preview("Root") {
    ___VARIABLE_navigationName___(nil)
}

#Preview("Sheet") {
    @Previewable @State var isPresented = true
    Button("Present") { isPresented.toggle() }
        .sheet(isPresented: $isPresented) {
            ___VARIABLE_navigationName___($isPresented)
        }
}
#endif
