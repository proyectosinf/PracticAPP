import SwiftUI

public enum TabBarWorkTutorFullScreen: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum TabBarWorkTutorSheet: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum TabBarWorkTutorPath: Hashable, Sendable {
    case mock
}

public typealias TabBarWorkTutorCoordinator = Coordinator<
    TabBarWorkTutorFullScreen,
    TabBarWorkTutorSheet,
    TabBarWorkTutorPath
>

public extension EnvironmentValues {
    @Entry var worktutorCoordinator: TabBarWorkTutorCoordinator?
}
