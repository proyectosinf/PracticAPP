import Foundation

public struct CreateOfferRequestParams: Sendable, Equatable, Hashable {
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
}
