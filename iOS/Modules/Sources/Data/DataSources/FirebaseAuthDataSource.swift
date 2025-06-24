import Foundation
import Domain
import FirebaseAuth

// sourcery: AutoMockable
protocol FirebaseAuthDataSource: Sendable {
    func signIn(email: String, password: String) async throws -> String
    func register(email: String, password: String) async throws -> String
    func signOut() async throws
    func currentToken() async throws -> String
    func sendResetPassword(email: String) async throws
    func deleteCurrentUser() async throws
}

final class FirebaseAuthDataSourceImpl: FirebaseAuthDataSource {
    public init() {}

       public func signIn(email: String, password: String) async throws -> String {
           let result = try await Auth.auth().signIn(withEmail: email, password: password)
           return try await result.user.getIDToken()
       }

    public func register(email: String, password: String) async throws -> String {
        do {
            let result = try await Auth.auth().createUser(withEmail: email, password: password)
            return result.user.uid
        } catch let error as NSError {
            if error.domain == AuthErrorDomain {
                switch AuthErrorCode(rawValue: error.code) {
                case .emailAlreadyInUse:
                    throw AppError.authenticationError("El correo electrónico ya está registrado.")
                default:
                    break
                }
            }
            throw AppError.unknown("No se pudo registrar. Inténtalo más tarde.")
        }
    }

       public func signOut() async throws {
           try Auth.auth().signOut()
       }

    public func currentToken() async throws -> String {
        guard let user = Auth.auth().currentUser else {
            return ""
        }
        return try await user.getIDToken(forcingRefresh: true)
    }
    public func deleteCurrentUser() async throws {
        guard let user = Auth.auth().currentUser else {
            throw AppError.unknown("No hay usuario autenticado para eliminar.")
        }
        try await user.delete()
    }

    public func sendResetPassword(email: String) async throws {
        try await withCheckedThrowingContinuation { (continuation: CheckedContinuation<Void, Error>) in
            Auth.auth().sendPasswordReset(withEmail: email) { error in
                if let error {
                    continuation.resume(throwing: AppError.internalError(error))
                } else {
                    continuation.resume()
                }
            }
        }
    }
}
