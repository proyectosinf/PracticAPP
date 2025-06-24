import AppCommon
import Combine
import SwiftUI

public struct OfferDetailNavigation: View {
    public var didCreateCandidacy = PassthroughSubject<Int, Never>()
    @State private var coordinator: OfferDetailCoordinator
    @State private var startingId: Int

    public init(id: Int, isPresented: Binding<Bool>?, didCreateCandidacy: PassthroughSubject<Int, Never>) {
        self.didCreateCandidacy = didCreateCandidacy
        coordinator = .init(isPresented: isPresented)
        _startingId = State(initialValue: id)
    }
    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .offerDetail(id: startingId))
                .navigationDestination(for: OfferDetailPath.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.offerDetailCoordinator, coordinator)
    }
    @ViewBuilder
    func build(screen: OfferDetailPath) -> some View {
        switch screen {
        case .offerDetail(let id):
            OfferDetaillScreen(offerId: id, didCreateCandidacy: didCreateCandidacy)
        case .offerDetailCompleted(let id):
                OfferDetaillScreen(offerId: id, didCreateCandidacy: didCreateCandidacy)
        }
    }
    @ViewBuilder
    func build(sheet: OfferDetailSheet) -> some View {
        switch sheet {
        case .mock:
            OfferDetailNavigation(
                id: startingId,
                isPresented: $coordinator.sheet.toBool(),
                didCreateCandidacy: didCreateCandidacy
            )
        }
    }
    @ViewBuilder
    func build(fullScreen: OfferDetailFullScreen) -> some View {
        switch fullScreen {
        case .mock:
            OfferDetailNavigation(
                id: startingId,
                isPresented: $coordinator.fullScreen.toBool(),
                didCreateCandidacy: didCreateCandidacy
            )
        }
    }
}
