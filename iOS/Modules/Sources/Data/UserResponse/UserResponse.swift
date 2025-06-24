import Foundation

public struct UserResponse: Codable {
    public let uid: String
    public let name: String
    public let surname: String
    public let email: String
    public let role: Int
    public let photo: String?
    public let dni: String?
    public let socialSecurityNumber: String?
    public let pdfCv: String?
    public let contactName: String?
    public let contactEmail: String?
    public let contactPhone: String?
}
