import Domain
import Foundation

public extension UpdateCandidacyStateRequest {
    func toData() -> UpdateCandidacyStateRequestData {
        .init(
            additionalNotes: additionalNotes,
            status: status
        )
    }
}
