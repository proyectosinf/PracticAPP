import SwiftUI
import Domain
import AppModels
import Factory

public enum RootView: Sendable {
    case splash
    case login
    case registerCompany
    case tabBar(role: TabBarRole)
}
@MainActor
@Observable
public final class AppCoordinator {
    public var root: RootView = .splash
    @Injected(\.userSession) @ObservationIgnored private var userSession
    private let authRepository: AuthRepository

    public init(authRepository: AuthRepository) {
        self.authRepository = authRepository
    }

    public func start() {
        Task {
            let logged = await authRepository.isUserLoggedIn()
            guard logged else {
                root = .login
                return
            }

            do {
                let user = try await authRepository.fetchCurrentUser()
                userSession.set(user: user)

                guard let role = TabBarRole(rawRole: user.role.rawValue) else {
                    root = .login
                    return
                }

                if role == .tutor, user.companyId == nil {
                    root = .registerCompany
                    return
                }

                root = .tabBar(role: role)

            } catch {
                root = .login
            }
        }
    }

    public func logout() {
        Task {
                do {
                    try await authRepository.signOut()
                    userSession.clear()
                    self.start()
                } catch {
                    throw AppError.unknown("Error al cerrar sesión")
                }
            }
    }
}
public extension EnvironmentValues {
    @Entry var appCoordinator: AppCoordinator?
}
