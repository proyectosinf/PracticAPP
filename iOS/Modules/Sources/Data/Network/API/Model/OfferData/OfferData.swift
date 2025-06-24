import Foundation
import Domain

public struct OfferData: Codable {
    public let id: Int
    public let title: String
    public let description: String
    public let vacanciesNumber: Int
    public let startDate: String
    public let endDate: String
    public let views: Int
    public let type: Int
    public let address: String
    public let postalCode: String
    public let contactName: String
    public let contactEmail: String
    public let contactPhone: String?
    public let company: String
    public let degree: String
    public let companyPhoto: String?
    public let inscribe: Bool?
    public let inscriptionCandidacy: Int

    enum CodingKeys: String, CodingKey {
        case id
        case title
        case description
        case vacanciesNumber = "vacancies_number"
        case startDate = "start_date"
        case endDate = "end_date"
        case views
        case type
        case address
        case postalCode = "postal_code"
        case contactName = "contact_name"
        case contactEmail = "contact_email"
        case contactPhone = "contact_phone"
        case company
        case degree
        case companyPhoto = "company_photo"
        case inscribe
        case inscriptionCandidacy = "inscriptions_candidacy"
    }
}

extension OfferData {
    public func toDomain() -> Offer {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.timeZone = TimeZone(secondsFromGMT: 0)

        return .init(
            id: id,
            title: title,
            description: description,
            vacanciesNumber: vacanciesNumber,
            startDate: formatter.date(from: startDate) ?? Date.distantPast,
            endDate: formatter.date(from: endDate) ?? Date.distantFuture,
            views: views,
            type: OfferType(rawValue: type) ?? .presencial,
            address: address,
            postalCode: postalCode,
            contactName: contactName,
            contactEmail: contactEmail,
            contactPhone: contactPhone ?? "",
            company: company,
            degree: degree,
            companyPhoto: companyPhoto ?? "",
            inscribe: inscribe ?? false,
            inscriptionCandidacy: inscriptionCandidacy
        )
    }
}
