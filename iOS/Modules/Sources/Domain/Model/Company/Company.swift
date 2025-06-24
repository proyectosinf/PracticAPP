import Foundation

public struct Company: Identifiable, Sendable, Codable {
    public let id: Int
    public let name: String
    public let sector: String
    public let web: String?
    public let cif: String
    public let logo: String?
    public let securityCode: String?

    enum CodingKeys: String, CodingKey {
        case id, name, sector, web, cif, logo
        case securityCode = "security_code"
        }
    public init(
        id: Int,
        name: String,
        sector: String,
        web: String?,
        cif: String,
        logo: String?,
        securityCode: String?
    ) {
        self.id = id
        self.name = name
        self.sector = sector
        self.web = web
        self.cif = cif
        self.logo = logo
        self.securityCode = securityCode
    }
}
