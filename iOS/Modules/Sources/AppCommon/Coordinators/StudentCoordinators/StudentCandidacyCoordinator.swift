import SwiftUI

public enum StudentCandidacyFullScreen: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum StudentCandidacySheet: Hashable, Identifiable, Sendable {
    case mock
    case detailCandidacy(candidacyId: Int)
    public var id: Self { self }
}

public enum StudentCandidacyPath: Hashable, Sendable {
    case candidacies
}

public typealias StudentCandidacyCoordinator = Coordinator<
    StudentCandidacyFullScreen,
    StudentCandidacySheet,
    StudentCandidacyPath
>

public extension EnvironmentValues {
    @Entry var studentcandidacyCoordinator: StudentCandidacyCoordinator?
}
