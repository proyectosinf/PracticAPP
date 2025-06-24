import Foundation

public struct UpdateCandidacyStateRequestData: Encodable {
    public let additionalNotes: String
    public let status: Int

    enum CodingKeys: String, CodingKey {
        case additionalNotes = "additional_notes"
        case status
    }
}
