import Foundation
import Factory
import Domain

// sourcery: AutoMockable
protocol CandidacyDataSource: Sendable {
    func createCandidacy(_ data: CandidacyRequestParams, authToken: String) async throws
    func getCandidacies(id: Int, token: String, page: Int) async throws -> CandidacyPaginatedResponse
    func getMeAllCandidacies(token: String, page: Int) async throws -> CandidacyPaginatedResponse
    func getCandidacyById(token: String, id: Int) async throws -> Candidacy
    func updateCandidacyState(id: Int, body: UpdateCandidacyStateRequestData, token: String) async throws

}

final class CandidacyDataSourceImpl: CandidacyDataSource {
    private let apiService: APIService

    init(apiService: APIService = Container.shared.apiService()) {
        self.apiService = apiService
    }

    func createCandidacy(_ data: CandidacyRequestParams, authToken: String) async throws {
        do {

            let response = try await apiService.post(
                "/api/v1/candidacies/",
                body: data,
                encoder: .json(JSONEncoder()),
                headers: ["Authorization": "Bearer \(authToken)"]
            )
            try response.validate()

        } catch {
            if let apiError = error as? APIErrorData {
                _ = String(data: apiError.apiResponse?.data ?? Data(), encoding: .utf8) ?? "sin cuerpo"

            } else {
            }
            throw error
        }
    }

    func getCandidacies(id: Int, token: String, page: Int) async throws -> CandidacyPaginatedResponse {
        let query: [URLQueryItem] = [
            URLQueryItem(name: "page", value: "\(page)")
            ]
            do {
                let response = try await apiService.get(
                    "/api/v1/candidacies/paginated/\(id)",
                    query: query,
                    headers: ["Authorization": "Bearer \(token)"]
                )
                let raw = response.data
                try response.validate()
                let candidacies = try JSONDecoder().decode(CandidacyPaginatedResponse.self, from: raw)
                return candidacies
            } catch {
                throw error
            }
    }
    func getMeAllCandidacies(token: String, page: Int) async throws -> CandidacyPaginatedResponse {
        let query: [URLQueryItem] = [
            URLQueryItem(name: "page", value: "\(page)")
            ]
            do {
                let response = try await apiService.get(
                    "/api/v1/candidacies/paginated",
                    query: query,
                    headers: ["Authorization": "Bearer \(token)"]
                )
                let raw = response.data
                try response.validate()
                let candidacies = try JSONDecoder().decode(CandidacyPaginatedResponse.self, from: raw)
                return candidacies
            } catch {
                throw error
            }
    }
    func getCandidacyById(token: String, id: Int) async throws -> Candidacy {
        let response = try await apiService.get(
            "/api/v1/candidacies/by-id/\(id)",
            headers: ["Authorization": "Bearer \(token)"]
        )
        let rawData = response.data
        let decoder = JSONDecoder()
        let candidacy = try decoder.decode(CandidacyData.self, from: rawData)
        return candidacy.toDomain()
    }
    func updateCandidacyState(id: Int, body: UpdateCandidacyStateRequestData, token: String) async throws {
        let response = try await apiService.put(
            "/api/v1/candidacies/update-state/\(id)",
            body: body,
            encoder: .json(JSONEncoder()),
            headers: ["Authorization": "Bearer \(token)"]
        )
        try response.validate()
    }
}
