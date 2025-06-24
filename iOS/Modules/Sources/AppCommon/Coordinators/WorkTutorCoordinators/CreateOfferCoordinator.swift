import SwiftUI

public enum CreateOfferFullScreen: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum CreateOfferSheet: Hashable, Identifiable, Sendable {
    case createOffers
    public var id: Self { self }
}

public enum CreateOfferPath: Hashable, Sendable {
    case createOffer
}

public typealias CreateOfferCoordinator = Coordinator<
    CreateOfferFullScreen,
    CreateOfferSheet,
    CreateOfferPath
>

public extension EnvironmentValues {
    @Entry var createOfferCoordinator: CreateOfferCoordinator?
}
