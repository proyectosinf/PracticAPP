import Foundation

public struct PaginatedCandidacies: Sendable {
    public let items: [CandidacyListItem]
    public let total: Int

    public init(items: [CandidacyListItem], total: Int) {
        self.items = items
        self.total = total
    }
}
