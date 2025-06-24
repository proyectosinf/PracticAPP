import Foundation

public struct Offer: Codable, Identifiable, Equatable, Hashable, Sendable {
    public let id: Int
    public let title: String
    public let description: String
    public let vacanciesNumber: Int
    public let startDate: Date
    public let endDate: Date
    public let views: Int
    public let type: OfferType
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

    public init(
        id: Int,
        title: String,
        description: String,
        vacanciesNumber: Int,
        startDate: Date,
        endDate: Date,
        views: Int,
        type: OfferType,
        address: String,
        postalCode: String,
        contactName: String,
        contactEmail: String,
        contactPhone: String?,
        company: String,
        degree: String,
        companyPhoto: String?,
        inscribe: Bool?,
        inscriptionCandidacy: Int
    ) {
        self.id = id
        self.title = title
        self.description = description
        self.vacanciesNumber = vacanciesNumber
        self.startDate = startDate
        self.endDate = endDate
        self.views = views
        self.type = type
        self.address = address
        self.postalCode = postalCode
        self.contactName = contactName
        self.contactEmail = contactEmail
        self.contactPhone = contactPhone
        self.company = company
        self.degree = degree
        self.companyPhoto = companyPhoto
        self.inscribe = inscribe
        self.inscriptionCandidacy = inscriptionCandidacy
    }
    enum CodingKeys: String, CodingKey {
            case id, title, description
            case vacanciesNumber = "vacancies_number"
            case startDate = "start_date"
            case endDate = "end_date"
            case views, type, address
            case postalCode = "postal_code"
            case contactName = "contact_name"
            case contactEmail = "contact_email"
            case contactPhone = "contact_phone"
            case company, degree
            case companyPhoto = "company_photo"
            case inscribe
            case inscriptionCandidacy = "inscriptions_candidacy"
        }
}

public enum OfferType: Int, Codable, CaseIterable, Identifiable, Equatable, Hashable, Sendable {
    case presencial = 1
    case online = 2

    public var id: Int { rawValue }

    public var description: String {
        switch self {
        case .presencial: return "Régimen Ordinario"
        case .online: return "Régimen personas adultas"
        }
    }
}
