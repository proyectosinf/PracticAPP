import Foundation

// sourcery: AutoMockable
public protocol UserDataSource: Sendable {
    func registerUser<T: Encodable>(user: T, endpoint: String, authToken: String) async throws
    func getCurrentUser(authToken: String) async throws -> UserData
}

public final class UserDataSourceImpl: UserDataSource {
    private let apiService: APIService
    public init(apiService: APIService) {
        self.apiService = apiService
    }
    public func registerUser<T: Encodable>(user: T, endpoint: String, authToken: String) async throws {
        do {
            _ = try JSONEncoder().encode(user)
            let headers = ["Authorization": "Bearer \(authToken)"]

            _ = try await apiService.post(
                endpoint,
                body: user,
                encoder: .json(JSONEncoder()),
                headers: headers
            )
        } catch let error as APIErrorData {
            throw error
        } catch {
            throw APIErrorData.unknownError(error)
        }
    }
    public func getCurrentUser(authToken: String) async throws -> UserData {
        let request = RequestDefinition<String>(
            method: .GET,
            path: "/api/v1/users/get_current_user",
            query: nil,
            body: nil,
            encoder: nil,
            decoder: nil,
            headers: ["Authorization": "Bearer \(authToken)"]
        )

        let response = try await apiService.response(request)
        return try response.decoded() as UserData
    }
}
