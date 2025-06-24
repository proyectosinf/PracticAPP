import Factory
import Foundation

// sourcery: AutoMockable
protocol AuthAPIDataSource: Sendable {
    func signIn(user: String, password: String) async throws -> APITokenData
    func signUp(request: SignUpRequestData) async throws
    func refreshToken(refreshToken: String) async throws -> APITokenData?
    func user() async throws -> UserData
}

final class AuthAPIDataSourceImpl: AuthAPIDataSource {
    private let sampleAPI: SampleAPI
    private let authAPI: AuthAPI

    init(container: Container = .shared) {
        sampleAPI = container.sampleAPI()
        authAPI = container.authAPI()
    }

    func signIn(user: String, password: String) async throws -> APITokenData {
        try await authAPI.signIn(user: user, password: password)
    }

    func signUp(request: SignUpRequestData) async throws {
        try await sampleAPI.signUp(request: request)
    }

    func refreshToken(refreshToken: String) async throws -> APITokenData? {
        await authAPI.refreshToken(refreshToken: refreshToken)
    }

    func user() async throws -> UserData {
        try await sampleAPI.user()
    }
}
