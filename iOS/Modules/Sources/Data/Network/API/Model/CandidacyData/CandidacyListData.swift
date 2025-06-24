import Foundation
import Domain

struct CandidacyListData: Decodable {
    var id: Int
    var status: Int
    var postulationDate: String
    var studentName: String
    var studentSurname: String
    var offerTitle: String
    var companyName: String?

    enum CodingKeys: String, CodingKey {
        case id
        case status
        case postulationDate = "postulation_date"
        case studentName = "student_name"
        case studentSurname = "student_surname"
        case offerTitle = "offer_title"
        case companyName = "company_name"
    }
}

extension CandidacyListData {
    func toDomain() -> CandidacyListItem {
        CandidacyListItem(
            id: id,
            offerTitle: offerTitle,
            studentName: studentName,
            studentSurname: studentSurname,
            postulationDate: postulationDate,
            status: status,
            companyName: companyName ?? ""
        )
    }
}
