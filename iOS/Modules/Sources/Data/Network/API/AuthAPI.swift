import Factory
import Foundation

final class AuthAPI: Sendable {
    private let apiService: APIService
    private let keychain: KeychainDataSource
    private let cacheDataSource: CacheDataSource
    private let container: Container

    init(apiService: APIService, container: Container = .shared) {
        self.apiService = apiService
        self.container = container
        keychain = container.keychainDataSource()
        cacheDataSource = container.cacheDataSource()
    }

    var httpAdditionalHeaders: [String: String] {
        var headers: [String: String] = [:]
        if let accessToken = keychain.apiToken()?.accessToken {
            headers["Authorization"] = "Bearer \(accessToken)"
        }
        return headers
    }

    @discardableResult
    func signIn(user: String, password: String) async throws -> APITokenData {
        let token: APITokenData = try await apiService.post(
            "/login",
            body: ["user": user, "password": password]
        ).decoded()
        keychain.saveAPIToken(token)
        await cacheDataSource.updateSignedInUser(true)
        return token
    }

    @discardableResult
    func refreshToken(refreshToken: String) async -> APITokenData? {
        do {
            let token: APITokenData = try await apiService.post(
                "/refreshToken",
                body: ["refreshToken": refreshToken]
            ).decoded()
            keychain.saveAPIToken(token)
            container.manager.reset(scope: .session)
            return token
        } catch {
            // Token cannot be refreshed after 401, app should perform a logout
            await cacheDataSource.updateSignedInUser(false)
        }
        return nil
    }
}

extension AuthAPI: Interceptor {
    func intercept(
        request: URLRequest,
        session: URLSession,
        next: @escaping (URLRequest, URLSession) async throws -> APIResponse
    ) async throws -> APIResponse {
        guard let token = keychain.apiToken() else {
            return try await next(request, session)
        }
        let response = try await next(request, session)
        if let statusCode = response.statusCode {
            switch statusCode {
            case HTTPStatusCode.unauthorized:
                await refreshToken(refreshToken: token.refreshToken)
                let configuration = URLSessionConfiguration.default
                configuration.httpAdditionalHeaders = httpAdditionalHeaders
                let newSession = URLSession(configuration: configuration)
                return try await next(request, newSession)
            default:
                break
            }
        }
        return response
    }
}
