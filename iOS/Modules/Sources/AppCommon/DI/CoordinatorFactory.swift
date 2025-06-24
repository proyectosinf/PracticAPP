import Factory
import Domain

public extension Container {
    var appCoordinator: Factory<AppCoordinator> {
        self {
            @MainActor in
            AppCoordinator(authRepository: self.authRepository())
        }.singleton
    }
}
