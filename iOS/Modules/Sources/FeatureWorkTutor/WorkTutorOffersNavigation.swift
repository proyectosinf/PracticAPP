import AppCommon
import Combine
import SwiftUI

public struct WorkTutorOffersNavigation: View {
    @State private var coordinator: WorkTutorOffersCoordinator
    @State private var didCreateOffer = PassthroughSubject<Void, Never>()

    public init(_ isPresented: Binding<Bool>?) {
        coordinator = .init(isPresented: isPresented)
    }

    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .offers)
                .navigationDestination(for: WorkTutorOffersPath.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.worktutorOffersCoordinator, coordinator)
    }

    @ViewBuilder
    func build(screen: WorkTutorOffersPath) -> some View {
        switch screen {
        case .offers:
            WorkTutorOffersScreen(didCreateOffer: didCreateOffer.eraseToAnyPublisher())
        }
    }

    @ViewBuilder
    func build(sheet: WorkTutorOffersSheet) -> some View {
        switch sheet {
        case .mock:
            WorkTutorOffersNavigation($coordinator.sheet.toBool())
        case .createOffers:
            CreateOfferNavigation(
                $coordinator.sheet.toBool(),
                didCreateOffer: didCreateOffer
            )
        }
    }

    @ViewBuilder
    func build(fullScreen: WorkTutorOffersFullScreen) -> some View {
        switch fullScreen {
        case .mock:
            WorkTutorOffersNavigation($coordinator.fullScreen.toBool())
        case .candidacies(let offer):
            CandidacyOffersNavigation(offer: offer, isPresented: $coordinator.fullScreen.toBool())
        }
    }
}

#if DEBUG
#Preview("Root") {
    WorkTutorOffersNavigation(nil)
}

#Preview("Sheet") {
    @Previewable @State var isPresented = true
    Button("Present") { isPresented.toggle() }
        .sheet(isPresented: $isPresented) {
            WorkTutorOffersNavigation($isPresented)
        }
}
#endif
