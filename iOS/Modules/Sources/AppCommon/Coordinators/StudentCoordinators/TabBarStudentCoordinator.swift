import SwiftUI

public enum TabBarStudentFullScreen: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum TabBarStudentSheet: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum TabBarStudentPath: Hashable, Sendable {
    case mock
}

public typealias TabBarStudentCoordinator = Coordinator<
    TabBarStudentFullScreen,
    TabBarStudentSheet,
    TabBarStudentPath
>

public extension EnvironmentValues {
    @Entry var studentCoordinator: TabBarStudentCoordinator?
}
