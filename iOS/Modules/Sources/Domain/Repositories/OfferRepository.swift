import Foundation

// sourcery: AutoMockable
public protocol OfferRepository: Sendable {
    func createOffer(_ paramas: CreateOfferRequestParams) async throws
    func getOffers(page: Int) async throws -> PaginatedOffers
    func getOfferDetail(id: Int) async throws -> Offer
}
