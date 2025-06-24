import SwiftUI

public enum ForgotPasswordFullScreen: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum ForgotPasswordSheet: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum ForgotPasswordPath: Hashable, Sendable {
    case fotgotPassword
}

public typealias ForgotPasswordCoordinator = Coordinator<
    ForgotPasswordFullScreen,
    ForgotPasswordSheet,
    ForgotPasswordPath
>

public extension EnvironmentValues {
    @Entry var forgotPasswordCoordinator: ForgotPasswordCoordinator?
}
