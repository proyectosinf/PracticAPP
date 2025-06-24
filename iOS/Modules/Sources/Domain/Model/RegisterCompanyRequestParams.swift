import Foundation

public struct RegisterCompanyRequestParams: Encodable {
    public let name: String
    public let sector: String
    public let web: String?
    public let cif: String
    public let logo: String?

    public init(
        name: String,
        sector: String,
        web: String?,
        cif: String,
        logo: String?
    ) {
        self.name = name
        self.sector = sector
        self.web = web
        self.cif = cif
        self.logo = logo
    }
}
