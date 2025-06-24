import Foundation

public struct APIResponse: Sendable {
    public let request: URLRequest
    public let response: URLResponse
    public let data: Data
    public let decoder: ResponseDecoder

    var statusCode: Int? {
        (response as? HTTPURLResponse)?.statusCode
    }

    var serverError: ServerErrorData? {
        try? JSONDecoder().decode(ServerErrorData.self, from: data)
    }

    @discardableResult
    func validate() throws -> Self {
        if let statusCode, !(200 ..< 300).contains(statusCode) {
            throw APIErrorData.serverError(self)
        }
        return self
    }

    func decoded<T: Decodable>(decoder: ResponseDecoder? = nil) throws -> T {
        do {
            return try (decoder ?? self.decoder).decode(T.self, from: data)
        } catch {
            throw APIErrorData.decodingError(error)
        }
    }
}

enum HTTPStatusCode {
    static let success = 200
    static let unauthorized = 401
    static let notFound = 404
    static let internalServerError = 500
}
