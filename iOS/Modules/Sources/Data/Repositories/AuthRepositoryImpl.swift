import Domain
import Factory
import Foundation
import FirebaseAuth

public final class AuthRepositoryImpl: AuthRepository {
    private let authAPIDataSource: AuthAPIDataSource
    private let keychainDataSource: KeychainDataSource
    private let cacheDataSource: CacheDataSource
    private let firebaseAuthDataSource: FirebaseAuthDataSource
    private let userDataSource: UserDataSource

    public init(container: Container = .shared) {
        authAPIDataSource = container.authAPIDataSource()
        keychainDataSource = container.keychainDataSource()
        cacheDataSource = container.cacheDataSource()
        firebaseAuthDataSource = container.firebaseAuthDataSource()
        userDataSource = container.userDataSource()
    }

    public func signInFirebase(email: String, password: String) async throws {
        do {
            let token = try await firebaseAuthDataSource.signIn(email: email, password: password)
            keychainDataSource.saveFirebaseToken(token)
            await cacheDataSource.updateSignedInUser(true)
        } catch let error as NSError {
            if error.domain == AuthErrorDomain && error.code == 17004 {
                throw AppError.authenticationError("Credenciales incorrectas.")
            } else if error.code == NSURLErrorNotConnectedToInternet {
                throw AppError.networkError
            } else {
                throw AppError.unknown("No se pudo iniciar sesión. Inténtalo más tarde.")
            }
        }
    }

    public func register(email: String, password: String) async throws -> String {
        return try await firebaseAuthDataSource.register(email: email, password: password)
    }

    public func firebaseToken() async throws -> String {
        return try await firebaseAuthDataSource.currentToken()
    }

    public func signOut() async throws {
        do {
            try await firebaseAuthDataSource.signOut()
        } catch {
            throw AppError.unknown("No se pudo cerrar sesión correctamente.")
        }

        keychainDataSource.deleteFirebaseToken()
        await cacheDataSource.updateSignedInUser(false)
    }

    public func isUserLoggedIn() async -> Bool {
        return keychainDataSource.firebaseToken() != nil
    }

    public var signedInUserStream: AsyncStream<Bool> {
        get async {
            let signedInUser = keychainDataSource.apiToken() != nil
            await cacheDataSource.updateSignedInUser(signedInUser)
            return await cacheDataSource.signedInUserStream
        }
    }

    public func signIn(user: String, password: String) async throws {
        try await normalizeError {
            let token = try await authAPIDataSource.signIn(user: user, password: password)
            keychainDataSource.saveAPIToken(token)
            await cacheDataSource.updateSignedInUser(true)
        }
    }

    public func signUp(request: SignUpRequest) async throws {
        try await normalizeError {
            try await authAPIDataSource.signUp(request: .init(request: request))
        }
    }

    public func user() async throws -> Domain.User {
        try await normalizeError {
            let user = try await authAPIDataSource.user()
            await cacheDataSource.updateSignedInUser(true)
            return user.toDomain()
        }
    }
    public func fetchCurrentUser() async throws -> Domain.User {
        let token = try await firebaseToken()
        let userData = try await userDataSource.getCurrentUser(authToken: token)
        return userData.toDomain()
    }
    public func sendResetPassword(email: String) async throws {
        try await firebaseAuthDataSource.sendResetPassword(email: email)
    }
    public func deleteCurrentUser() async throws {
        try await firebaseAuthDataSource.deleteCurrentUser()
    }
    public func refreshFirebaseToken() async throws {
        let token = try await firebaseAuthDataSource.currentToken()
        keychainDataSource.saveFirebaseToken(token)
    }

}
