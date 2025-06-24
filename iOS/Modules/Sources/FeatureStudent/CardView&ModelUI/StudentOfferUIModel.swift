import Foundation

struct StudentOfferUIModel: Identifiable, Equatable, Sendable {
    var id: Int
    var title: String
    var companyName: String
    var companyLogoUrl: String?
    var startDate: Date
    var endDate: Date
    var formation: String
    var vacancies: Int
    var type: String
    var isSubscribed: Bool
}
