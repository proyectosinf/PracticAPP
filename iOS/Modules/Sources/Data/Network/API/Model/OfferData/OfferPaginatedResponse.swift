import Foundation

public struct OfferPaginatedResponse: Decodable {
    public let items: [OfferData]
    public let total: Int

    enum CodingKeys: String, CodingKey {
        case items
        case total
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.items = try container.decode([OfferData].self, forKey: .items)
        self.total = try container.decode(Int.self, forKey: .total)
    }

    public init(items: [OfferData], total: Int) {
        self.items = items
        self.total = total
    }
}
