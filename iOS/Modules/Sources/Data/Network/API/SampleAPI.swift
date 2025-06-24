import Domain
import Factory
import Foundation

extension APIEnvironment {
    var apiDomain: String {
        switch self {
        case .production: "https://47bca468-eba2-4d6c-967b-64c63a12c790.mock.pstmn.io"
        case .development: "https://47bca468-eba2-4d6c-967b-64c63a12c790.mock.pstmn.io"
        }
    }
}

final class SampleAPI: Sendable {
    private let apiService: APIService

    init(apiService: APIService) {
        self.apiService = apiService
    }

    func signUp(request: SignUpRequestData) async throws {
        try await apiService.post("/signup", body: request)
    }

    func user() async throws -> UserData {
        try await apiService.get("/user").decoded()
    }
}
