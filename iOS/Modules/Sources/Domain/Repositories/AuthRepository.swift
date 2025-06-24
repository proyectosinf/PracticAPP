import Foundation

// sourcery: AutoMockable
public protocol AuthRepository: Sendable {
    var signedInUserStream: AsyncStream<Bool> { get async }
    func signIn(user: String, password: String) async throws
    func signUp(request: SignUpRequest) async throws
    func register(email: String, password: String) async throws -> String
    func signInFirebase(email: String, password: String) async throws
    func isUserLoggedIn() async -> Bool
    func user() async throws -> User
    func signOut() async throws
    func firebaseToken() async throws -> String
    func fetchCurrentUser() async throws -> User
    func sendResetPassword(email: String) async throws
    func deleteCurrentUser() async throws
    func refreshFirebaseToken() async throws
}
