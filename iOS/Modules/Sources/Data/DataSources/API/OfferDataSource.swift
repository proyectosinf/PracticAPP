import Foundation
import Domain
import Factory
protocol OfferDataSource: Sendable {
    func createOffer(_ offer: CreateOfferRequestParams, authToken: String) async throws
    func getOffers(authToken: String, page: Int) async throws -> OfferPaginatedResponse
    func getOfferDetail(id: Int, token: String) async throws -> Offer
}

final class OfferDataSourceImpl: OfferDataSource {
    private let apiService: APIService
    public init(apiService: APIService = Container.shared.apiService()) {
        self.apiService = apiService
    }
    func createOffer(_ offer: CreateOfferRequestParams, authToken: String) async throws {
        do {
            _ = try await apiService.post(
                "/api/v1/offers/",
                body: offer,
                encoder: .json(JSONEncoder()),
                headers: ["Authorization": "Bearer \(authToken)"]
            ).validate()
        } catch let error as APIErrorData {
            if error.apiResponse != nil {
            }
            throw error.toRegisterCompanyDomainError
        }
    }
    func getOffers(authToken: String, page: Int) async throws -> OfferPaginatedResponse {
        let query: [URLQueryItem] = [
            URLQueryItem(name: "page", value: "\(page)")
        ]
        do {
            let response = try await apiService.get(
                "/api/v1/offers/paginated",
                query: query,
                headers: ["Authorization": "Bearer \(authToken)"]
            )
            if let serverError = try? response.decoded(decoder: JSONDecoder()) as ServerErrorData,
               serverError.code == 2001 {
                return OfferPaginatedResponse(items: [], total: 0)
            }
            let raw = response.data
            try response.validate()
            let paginatedResponse = try JSONDecoder().decode(OfferPaginatedResponse.self, from: raw)
            return paginatedResponse
        } catch let error as APIErrorData {
            if let serverError = error.serverError, serverError.code == 2001 {
                return OfferPaginatedResponse(items: [], total: 0)
            }
            throw error.toDomain
        }
    }
    public func getOfferDetail(id: Int, token: String) async throws -> Offer {
        let response = try await apiService.get(
            "/api/v1/offers/\(id)",
            headers: ["Authorization": "Bearer \(token)"]
        )
        let decoder = JSONDecoder()
        let offerData = try decoder.decode(OfferData.self, from: response.data)
        return offerData.toDomain()
    }
}
