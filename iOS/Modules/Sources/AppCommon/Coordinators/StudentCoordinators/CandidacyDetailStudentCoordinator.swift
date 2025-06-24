import SwiftUI

public enum CandidacyDetailStudentFullScreen: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum CandidacyDetailStudentSheet: Hashable, Identifiable, Sendable {
    case candidacyDetaillStudent(candidacyId: Int)
    public var id: Self { self }
}

public enum CandidacyDetailStudentPath: Hashable, Sendable {
    case candidacyDetaillStudent(candidacyId: Int)
}

public typealias CandidacyDetailStudentCoordinator = Coordinator<
    CandidacyDetailStudentFullScreen,
    CandidacyDetailStudentSheet,
    CandidacyDetailStudentPath
>

public extension EnvironmentValues {
    @Entry var candidacyDetailStudentCoordinator: CandidacyDetailStudentCoordinator?
}
