import SwiftUI

public enum CandidacyDetailFullScreen: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum CandidacyDetailSheet: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum CandidacyDetailPath: Hashable, Sendable {
    case candidacyDetail(candidacyId: Int)
}

public typealias CandidacyDetailCoordinator = Coordinator<
    CandidacyDetailFullScreen,
    CandidacyDetailSheet,
    CandidacyDetailPath
>

public extension EnvironmentValues {
    @Entry var candidacyDetailCoordinator: CandidacyDetailCoordinator?
}
