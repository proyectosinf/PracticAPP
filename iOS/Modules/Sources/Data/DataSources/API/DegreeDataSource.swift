import Foundation
import Domain

// sourcery: AutoMockable
protocol DegreeDataSource: Sendable {
    func getDegrees(authToken: String) async throws -> [Degree]
}

final class DegreeDataSourceImpl: DegreeDataSource {
    private let apiService: APIService
    public init(apiService: APIService) {
        self.apiService = apiService
    }
    public func getDegrees(authToken: String) async throws -> [Degree] {
        let response = try await apiService.get(
            "/api/v1/degrees/",
            query: nil,
            headers: ["Authorization": "Bearer \(authToken)"]
        )
        try response.validate()
        return try response.decoded()
    }
}
