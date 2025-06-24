import Foundation
import Domain

public struct CreateOfferRequestParams: Encodable {
    public let title: String
    public let description: String
    public let vacanciesNumber: Int
    public let startDate: String
    public let endDate: String
    public let type: Int
    public let address: String
    public let postalCode: String
    public let contactName: String
    public let contactEmail: String
    public let contactPhone: String?
    public let degreeId: Int

    public init(
        title: String,
        description: String,
        vacanciesNumber: Int,
        startDate: String,
        endDate: String,
        type: Int,
        address: String,
        postalCode: String,
        contactName: String,
        contactEmail: String,
        contactPhone: String?,
        degreeId: Int
    ) {
        self.title = title
        self.description = description
        self.vacanciesNumber = vacanciesNumber
        self.startDate = startDate
        self.endDate = endDate
        self.type = type
        self.address = address
        self.postalCode = postalCode
        self.contactName = contactName
        self.contactEmail = contactEmail
        self.contactPhone = contactPhone
        self.degreeId = degreeId
    }

    public init(from domain: Domain.CreateOfferRequestParams) {
        self.init(
            title: domain.title,
            description: domain.description,
            vacanciesNumber: domain.vacanciesNumber,
            startDate: domain.startDate,
            endDate: domain.endDate,
            type: domain.type,
            address: domain.address,
            postalCode: domain.postalCode,
            contactName: domain.contactName,
            contactEmail: domain.contactEmail,
            contactPhone: domain.contactPhone,
            degreeId: domain.degreeId
        )
    }

    enum CodingKeys: String, CodingKey {
        case title
        case description
        case vacanciesNumber = "vacancies_number"
        case startDate = "start_date"
        case endDate = "end_date"
        case type
        case address
        case postalCode = "postal_code"
        case contactName = "contact_name"
        case contactEmail = "contact_email"
        case contactPhone = "contact_phone"
        case degreeId = "degree_id"
    }
}
