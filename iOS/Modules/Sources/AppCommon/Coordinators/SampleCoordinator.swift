import SwiftUI

public enum SampleFullScreen: Hashable, Identifiable, Sendable {
    case fullScreen
    public var id: Self { self }
}

public enum SampleSheet: Hashable, Identifiable, Sendable {
    case sheet
    public var id: Self { self }
}

public enum SamplePath: Hashable, Sendable {
    case path1
    case path2
    case path3
}

public typealias SampleCoordinator = Coordinator<SampleFullScreen, SampleSheet, SamplePath>

public extension EnvironmentValues {
    @Entry var sampleCoordinator: SampleCoordinator?
}
