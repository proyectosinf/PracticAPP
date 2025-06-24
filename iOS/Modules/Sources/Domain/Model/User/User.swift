import Foundation

public struct User: Sendable, Codable, Equatable {
    public let id: Int?
    public let uid: String?
    public let name: String
    public let surname: String
    public let email: String
    public let dni: String?
    public let socialSecurityNumber: String?
    public let pdfCV: String?
    public let photo: String?
    public let contactName: String?
    public let contactEmail: String?
    public let contactPhone: String?
    public let companyId: Int?
    public let role: UserRole

    public init(
        id: Int? = nil,
        uid: String? = nil,
        name: String,
        surname: String,
        email: String,
        dni: String? = nil,
        socialSecurityNumber: String? = nil,
        pdfCV: String? = nil,
        photo: String? = nil,
        contactName: String? = nil,
        contactEmail: String? = nil,
        contactPhone: String? = nil,
        companyId: Int? = nil,
        role: UserRole
    ) {
        self.id = id
        self.uid = uid
        self.name = name
        self.surname = surname
        self.email = email
        self.dni = dni
        self.socialSecurityNumber = socialSecurityNumber
        self.pdfCV = pdfCV
        self.photo = photo
        self.contactName = contactName
        self.contactEmail = contactEmail
        self.contactPhone = contactPhone
        self.companyId = companyId
        self.role = role
    }
}

public enum UserRole: Int, Codable, CaseIterable, Equatable, Sendable {
    case student = 1
    case tutor = 2

    public var name: String {
        switch self {
        case .student: return "STUDENT"
        case .tutor: return "TUTOR"
        }
    }

    public var displayName: String {
        switch self {
        case .student: return "Estudiante"
        case .tutor: return "Tutor laboral"
        }
    }
}
