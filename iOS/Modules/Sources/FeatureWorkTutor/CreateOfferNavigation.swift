import AppCommon
import Combine
import SwiftUI

public struct CreateOfferNavigation: View {
    @State private var coordinator: CreateOfferCoordinator
    public let didCreateOffer: PassthroughSubject<Void, Never>

    public init(_ isPresented: Binding<Bool>?, didCreateOffer: PassthroughSubject<Void, Never>) {
        self.didCreateOffer = didCreateOffer
        coordinator = .init(isPresented: isPresented)
    }

    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .createOffer)
                .navigationDestination(for: CreateOfferPath.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.createOfferCoordinator, coordinator)
    }

    @ViewBuilder
    func build(screen: CreateOfferPath) -> some View {
        switch screen {
        case .createOffer:
            CreateOfferScreen(didCreateOffer: didCreateOffer)
        }
    }

    @ViewBuilder
    func build(sheet: CreateOfferSheet) -> some View {
        switch sheet {
        case .createOffers:
            CreateOfferNavigation(
                $coordinator.sheet.toBool(),
                didCreateOffer: didCreateOffer
            )
        }
    }

    @ViewBuilder
    func build(fullScreen: CreateOfferFullScreen) -> some View {
        switch fullScreen {
        case .mock:
            CreateOfferNavigation($coordinator.fullScreen.toBool(), didCreateOffer: didCreateOffer)
        }
    }
}
