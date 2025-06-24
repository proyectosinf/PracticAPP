import SwiftUI

public enum OfferDetailFullScreen: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum OfferDetailSheet: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum OfferDetailPath: Hashable, Sendable {
    case offerDetail(id: Int)
    case offerDetailCompleted(id: Int)
}

public typealias OfferDetailCoordinator = Coordinator<
    OfferDetailFullScreen,
    OfferDetailSheet,
    OfferDetailPath
>

public extension EnvironmentValues {
    @Entry var offerDetailCoordinator: OfferDetailCoordinator?
}
