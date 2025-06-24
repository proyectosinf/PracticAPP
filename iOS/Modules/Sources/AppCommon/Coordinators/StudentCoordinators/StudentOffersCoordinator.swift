import SwiftUI

public enum StudentOffersFullScreen: Hashable, Identifiable, Sendable {
    case mock
    case offerDetail(Int)
    public var id: Self { self }
}

public enum StudentOffersSheet: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum StudentOffersPath: Hashable, Sendable {
    case offers
}

public typealias StudentOffersCoordinator = Coordinator<
    StudentOffersFullScreen,
    StudentOffersSheet,
    StudentOffersPath
>

public extension EnvironmentValues {
    @Entry var studentoffersCoordinator: StudentOffersCoordinator?
}
