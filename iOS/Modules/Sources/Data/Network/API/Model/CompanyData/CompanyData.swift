import Foundation
import Domain

public struct CompanyData: Codable {
    public let id: Int
    public let name: String
    public let sector: String
    public let web: String?
    public let cif: String
    public let logo: String?
    public let securityCode: String?

    enum CodingKeys: String, CodingKey {
        case id
        case name
        case sector
        case web
        case cif
        case logo
        case securityCode = "security_code"
    }
}
public extension CompanyData {
    func toDomain() -> Company {
        .init(
            id: id,
            name: name,
            sector: sector,
            web: web,
            cif: cif,
            logo: logo,
            securityCode: securityCode
        )
    }
}
