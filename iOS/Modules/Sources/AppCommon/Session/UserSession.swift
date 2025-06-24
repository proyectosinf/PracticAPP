import Foundation
import Domain

public final class UserSession: ObservableObject {
    @Published public private(set) var currentUser: User?

    public init(currentUser: User? = nil) {
        self.currentUser = currentUser
    }

    public func set(user: User) {
        self.currentUser = user
    }

    public func clear() {
        self.currentUser = nil
    }

    public var isLoggedIn: Bool {
        currentUser != nil
    }
}
