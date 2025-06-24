import Foundation

public struct RegisterWorkTutorParams: Codable {
    public let uid: String
    public let name: String
    public let surname: String
    public let email: String
    public let role: Int
    public let photo: String?
    public let companyId: Int?

    enum CodingKeys: String, CodingKey {
        case uid, name, surname, email, role, photo
        case companyId = "company_id"
    }
}
