import Foundation
import Domain

public struct UserData: Codable {
    public var id: Int?
    public var uid: String?
    public var name: String
    public var surname: String
    public var email: String
    public var dni: String?
    public var socialSecurityNumber: String?
    public var pdfCv: String?
    public var photo: String?
    public var contactName: String?
    public var contactEmail: String?
    public var contactPhone: String?
    public var role: Int?
    public var companyId: Int?

    enum CodingKeys: String, CodingKey {
            case id
            case uid
            case name
            case surname
            case email
            case dni
            case socialSecurityNumber = "social_security_number"
            case pdfCv = "pdf_cv"
            case photo
            case contactName = "contact_name"
            case contactEmail = "contact_email"
            case contactPhone = "contact_phone"
            case companyId = "company_id"
            case role
        }
}

public extension UserData {
    func toDomain() -> User {
        guard let role = role,
              let userRole = UserRole(rawValue: role) else {
            assertionFailure("Error: 'roles' no puede ser nil. Se usará .student por defecto.")
            return User(
                id: id,
                uid: uid,
                name: name,
                surname: surname,
                email: email,
                dni: dni,
                socialSecurityNumber: socialSecurityNumber,
                pdfCV: pdfCv,
                photo: photo,
                contactName: contactName,
                contactEmail: contactEmail,
                contactPhone: contactPhone,
                role: .student
            )
        }

        return User(
            id: id,
            uid: uid,
            name: name,
            surname: surname,
            email: email,
            dni: dni,
            socialSecurityNumber: socialSecurityNumber,
            pdfCV: pdfCv,
            photo: photo,
            contactName: contactName,
            contactEmail: contactEmail,
            contactPhone: contactPhone,
            companyId: companyId,
            role: userRole
        )
    }
}
