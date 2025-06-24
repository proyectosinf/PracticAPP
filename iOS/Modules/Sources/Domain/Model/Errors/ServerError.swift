import Foundation

public struct ServerError: Sendable, Codable, Error {
    public let statusCode: Int?
    public let code: Int?
    public let message: String?
    public let detail: String?

    public init(
        statusCode: Int? = nil,
        code: Int? = nil,
        message: String? = nil,
        detail: String? = nil
    ) {
        self.statusCode = statusCode
        self.code = code
        self.message = message
        self.detail = detail
    }

    public var displayMessage: String {
        return message ?? detail ?? "Se ha producido un error inesperado."
    }
}
