import SwiftUI
import Domain

public enum CandidacyOffersFullScreen: Hashable, Identifiable, Sendable {
    case candidacyList(offer: Offer)
    public var id: Self { self }
}

public enum CandidacyOffersSheet: Hashable, Identifiable, Sendable {
    case mock(candidacyId: Int)
    case candidacyDetail(candidacyId: Int)
    public var id: Self { self }
}

public enum CandidacyOffersPath: Hashable, Sendable {
    case candidacies(offer: Offer)
}

public typealias CandidacyOffersCoordinator = Coordinator<
    CandidacyOffersFullScreen,
    CandidacyOffersSheet,
    CandidacyOffersPath
>

public extension EnvironmentValues {
    @Entry var candidacyOffersCoordinator: CandidacyOffersCoordinator?
}
