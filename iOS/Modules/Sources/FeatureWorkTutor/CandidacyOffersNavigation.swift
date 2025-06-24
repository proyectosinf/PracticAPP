import AppCommon
import Combine
import SwiftUI
import Domain

public struct CandidacyOffersNavigation: View {
    @State private var coordinator: CandidacyOffersCoordinator
    @State private var startingOffer: Offer
    @State private var didUpdateCandidacy = PassthroughSubject<Void, Never>()
    @State private var subscriptions = Set<AnyCancellable>()

    public init(offer: Offer, isPresented: Binding<Bool>?) {
           self._startingOffer = State(initialValue: offer)
           self._coordinator = State(initialValue: .init(isPresented: isPresented))
       }
    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .candidacies(offer: startingOffer))
                .navigationDestination(for: CandidacyOffersPath.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.candidacyOffersCoordinator, coordinator)
    }

    @ViewBuilder
    func build(screen: CandidacyOffersPath) -> some View {
        switch screen {
        case .candidacies(let startingOffer):
            CandidacyOffersScreen(offer: startingOffer, didUpdateCandidacy: didUpdateCandidacy.eraseToAnyPublisher())
        }
    }

    @ViewBuilder
    func build(sheet: CandidacyOffersSheet) -> some View {
        switch sheet {
        case .mock(let candidacyId):
            CandidacyDetailScreen(candidacyId: candidacyId, didUpdateCandidacy: didUpdateCandidacy)
        case .candidacyDetail(candidacyId: let candidacyId):
            CandidacyDetailNavigation(
                candidacyId: candidacyId,
                isPresented: $coordinator.fullScreen.toBool(),
                didUpdateCandidacy: didUpdateCandidacy
            )
        }
    }

    @ViewBuilder
    func build(fullScreen: CandidacyOffersFullScreen) -> some View {
        switch fullScreen {
        case .candidacyList(let offer):
            CandidacyOffersNavigation(
                offer: offer, isPresented: $coordinator.fullScreen.toBool()
            )
        }
    }
}
