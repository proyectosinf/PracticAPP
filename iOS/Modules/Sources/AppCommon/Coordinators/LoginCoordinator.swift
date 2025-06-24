import SwiftUI

public enum LoginFullScreen: Hashable, Identifiable, Sendable {
    case login
    case test
    case register
    public var id: String {
        switch self {
        case .login: return "login"
        case .register: return "register"
        case .test: return "test"
        }
    }
}

public enum LoginSheet: Hashable, Identifiable, Sendable {
    case test
    case goToforgotPassword
    public var id: Self { self }
}

public enum LoginPath: Hashable, Sendable {
    case login
    case register
}

public typealias LoginCoordinator = Coordinator<LoginFullScreen, LoginSheet, LoginPath>

public extension EnvironmentValues {
    @Entry var loginCoordinator: LoginCoordinator?
}

extension LoginCoordinator: @unchecked Sendable {}
