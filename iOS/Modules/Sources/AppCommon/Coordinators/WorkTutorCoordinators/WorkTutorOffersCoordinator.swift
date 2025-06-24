import SwiftUI
import Domain

public enum WorkTutorOffersFullScreen: Hashable, Identifiable, Sendable {
    case mock
    case candidacies(offer: Offer)
    public var id: Self { self }
}

public enum WorkTutorOffersSheet: Hashable, Identifiable, Sendable {
    case mock
    case createOffers
    public var id: Self { self }
}

public enum WorkTutorOffersPath: Hashable, Sendable {
    case offers
}

public typealias WorkTutorOffersCoordinator = Coordinator<
    WorkTutorOffersFullScreen,
    WorkTutorOffersSheet,
    WorkTutorOffersPath
>

public extension EnvironmentValues {
    @Entry var worktutorOffersCoordinator: WorkTutorOffersCoordinator?
}
