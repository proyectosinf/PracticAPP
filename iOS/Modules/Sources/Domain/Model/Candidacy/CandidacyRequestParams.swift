import Foundation

public struct CandidacyRequestParams: Encodable, Sendable {
    public let offerId: Int
    public let presentationCard: String

    public init(offerId: Int, presentationCard: String) {
        self.offerId = offerId
        self.presentationCard = presentationCard
    }
    enum CodingKeys: String, CodingKey {
            case offerId = "offer_id"
            case presentationCard = "presentation_card"
        }
}
