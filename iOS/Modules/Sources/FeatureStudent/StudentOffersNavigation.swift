import AppCommon
import Combine
import SwiftUI

public struct StudentOffersNavigation: View {
    @State private var coordinator: StudentOffersCoordinator
    @State private var didCreateCandidacy = PassthroughSubject<Int, Never>()
    @State private var subscriptions = Set<AnyCancellable>()

    public init(_ isPresented: Binding<Bool>?) {
        coordinator = .init(isPresented: isPresented)
    }

    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .offers)
                .navigationDestination(for: StudentOffersPath.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.studentoffersCoordinator, coordinator)
    }

    @ViewBuilder
    func build(screen: StudentOffersPath) -> some View {
        switch screen {
        case .offers:
            StudentOffersScreen(didCreateCandidacy: didCreateCandidacy.eraseToAnyPublisher())
        }
    }

    @ViewBuilder
    func build(sheet: StudentOffersSheet) -> some View {
        switch sheet {
        case .mock:
            StudentOffersNavigation($coordinator.sheet.toBool())
        }
    }

    @ViewBuilder
    func build(fullScreen: StudentOffersFullScreen) -> some View {
        switch fullScreen {
        case .mock:
            StudentOffersNavigation($coordinator.fullScreen.toBool())
        case .offerDetail(let id):
            OfferDetailNavigation(
                id: id,
                isPresented: $coordinator.fullScreen.toBool(),
                didCreateCandidacy: didCreateCandidacy
            )
        }
    }
}

#if DEBUG
#Preview("Root") {
    StudentOffersNavigation(nil)
}

#Preview("Sheet") {
    @Previewable @State var isPresented = true
    Button("Present") { isPresented.toggle() }
        .sheet(isPresented: $isPresented) {
            StudentOffersNavigation($isPresented)
        }
}
#endif
