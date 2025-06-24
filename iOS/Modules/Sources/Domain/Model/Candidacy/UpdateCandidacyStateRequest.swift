import Foundation

public struct UpdateCandidacyStateRequest {
    public let additionalNotes: String
    public let status: Int

    public init(additionalNotes: String, status: Int) {
        self.additionalNotes = additionalNotes
        self.status = status
    }
}
