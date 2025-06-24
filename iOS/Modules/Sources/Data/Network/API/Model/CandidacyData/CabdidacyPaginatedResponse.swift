import Foundation

struct CandidacyPaginatedResponse: Decodable {
    let items: [CandidacyListData]
    let total: Int
}
