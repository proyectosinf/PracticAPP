import Foundation
import Domain

struct OfferUIModel: Identifiable, Equatable, Sendable {
    public let id: Int
    public let title: String
    public let formation: String
    public let vacancies: Int
    public let modality: String
    public let degree: String
    public let views: Int
    public let isActive: Bool
    public let inscriptionCandidacy: Int
}
extension OfferUIModel {
    func toDomain() -> Offer {
        Offer(
            id: id,
            title: title,
            description: "",
            vacanciesNumber: vacancies,
            startDate: .now,
            endDate: .now,
            views: views,
            type: .presencial,
            address: "",
            postalCode: "",
            contactName: "",
            contactEmail: "",
            contactPhone: nil,
            company: "",
            degree: degree,
            companyPhoto: "",
            inscribe: nil,
            inscriptionCandidacy: inscriptionCandidacy
        )
    }
}
