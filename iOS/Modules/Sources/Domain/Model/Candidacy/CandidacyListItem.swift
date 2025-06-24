import Foundation

public struct CandidacyListItem: Identifiable, Equatable, Decodable, Sendable {
    public let id: Int
    public let offerTitle: String
    public let studentName: String
    public let studentSurname: String
    public let postulationDate: String
    public let status: Int
    public let companyName: String

    enum CodingKeys: String, CodingKey {
        case id
        case offerTitle = "offer_title"
        case studentName = "student_name"
        case studentSurname = "student_surname"
        case postulationDate = "postulation_date"
        case status
        case companyName = "company_name"
    }

    public init(
        id: Int,
        offerTitle: String,
        studentName: String,
        studentSurname: String,
        postulationDate: String,
        status: Int,
        companyName: String
    ) {        self.id = id
        self.offerTitle = offerTitle
        self.studentName = studentName
        self.studentSurname = studentSurname
        self.postulationDate = postulationDate
        self.status = status
        self.companyName = companyName
    }
}
