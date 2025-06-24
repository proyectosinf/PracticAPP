import SwiftUI

public enum HomeCompanyFullScreen: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum HomeCompanySheet: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum HomeCompanyPath: Hashable, Sendable {
    case home
}

public typealias HomeCompanyCoordinator = Coordinator<
    HomeCompanyFullScreen,
    HomeCompanySheet,
    HomeCompanyPath
>

public extension EnvironmentValues {
    @Entry var homeCompanyCoordinator: HomeCompanyCoordinator?
}
