import Foundation

public struct PaginatedOffers: Decodable, Sendable {
    public let items: [Offer]
    public let total: Int

    public init(items: [Offer], total: Int) {
        self.items = items
        self.total = total
    }
}
