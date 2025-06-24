import Foundation
import Domain

public struct CandidacyData: Decodable {
    public let id: Int
    public let status: Int
    public let postulationDate: String
    public let additionalNotes: String?
    public let presentationCard: String
    public let offerTitle: String
    public let companyName: String
    public let companyPhoto: String?
    public let contactName: String
    public let contactEmail: String
    public let contactPhone: String?
    public let studentName: String
    public let studentSurname: String
    public let studentEmail: String?
    public let studentPdf: String?
    public let studentPhoto: String?

    enum CodingKeys: String, CodingKey {
        case id
        case status
        case postulationDate = "postulation_date"
        case additionalNotes = "additional_notes"
        case presentationCard = "presentation_card"
        case offerTitle = "offer_title"
        case companyName = "company_name"
        case contactName = "contact_name"
        case companyPhoto = "company_photo"
        case contactEmail = "contact_email"
        case contactPhone = "contact_phone"
        case studentName = "student_name"
        case studentSurname = "student_surname"
        case studentEmail = "student_email"
        case studentPdf = "student_pdf"
        case studentPhoto = "student_photo"
    }
}
extension CandidacyData {
    func toDomain() -> Candidacy {
        .init(
            id: id,
            status: status,
            postulationDate: postulationDate,
            additionalNotes: additionalNotes ?? "",
            presentationCard: presentationCard,
            offerTitle: offerTitle,
            companyName: companyName,
            companyPhoto: companyPhoto ?? "",
            contactName: contactName,
            contactEmail: contactEmail,
            contactPhone: contactPhone ?? "",
            studentName: studentName,
            studentSurname: studentSurname,
            studentEmail: studentEmail ?? "",
            studentPdf: studentPdf ?? "",
            studentPhoto: studentPhoto ?? ""
        )
    }
}
