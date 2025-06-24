import Foundation

struct RegisterStudentRequestParams: Codable {
    public let uid: String
    public let name: String
    public let surname: String
    public let email: String
    public let role: Int
    public let dni: String?
    public let socialSecurityNumber: String?
    public let pdfCv: String?
    public let contactName: String?
    public let contactEmail: String?
    public let contactPhone: String?
    public let photo: String?

    enum CodingKeys: String, CodingKey {
        case uid, name, surname, email, role, dni
        case socialSecurityNumber = "social_security_number"
        case pdfCv = "pdf_cv"
        case contactName = "contact_name"
        case contactEmail = "contact_email"
        case contactPhone = "contact_phone"
        case photo
    }
}
