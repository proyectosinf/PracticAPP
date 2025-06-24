import SwiftUI

public enum RegisterCompanyFullScreen: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum RegisterCompanySheet: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum RegisterCompanyPath: Hashable, Sendable {
    case mock
    case register
}

public typealias RegisterCompanyCoordinator = Coordinator<
    RegisterCompanyFullScreen,
    RegisterCompanySheet,
    RegisterCompanyPath
>

public extension EnvironmentValues {
    @Entry var registerCompanyCoordinator: RegisterCompanyCoordinator?
}
