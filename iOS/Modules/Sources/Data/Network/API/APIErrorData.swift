import Domain
import Foundation

enum APIErrorData: Error {
    case encodingError(Error)
    case decodingError(Error)
    case serverError(APIResponse)
    case unknownError(Error?)
}

struct ServerErrorData: Decodable {
    let code: Int
    let message: String

    private enum CodingKeys: String, CodingKey {
        case code
        case message
        case error
    }

    init(from decoder: Decoder) throws {
            let container = try decoder.container(keyedBy: CodingKeys.self)
            code = try container.decode(Int.self, forKey: .code)

            if let msg = try? container.decode(String.self, forKey: .message) {
                message = msg
            } else if let err = try? container.decode(String.self, forKey: .error) {
                message = err
            } else {
                message = "Error desconocido del servidor"
            }
    }
}

extension APIErrorData {
    var apiResponse: APIResponse? {
        switch self {
        case .encodingError: nil
        case .decodingError: nil
        case let .serverError(response): response
        case .unknownError: nil
        }
    }

    var serverError: ServerErrorData? {
        apiResponse?.serverError
    }
}

extension APIErrorData {
    var toDomain: AppError {
        switch self {
        case let .encodingError(error): .internalError(error)
        case let .decodingError(error): .internalError(error)
        case let .serverError(response): .serverError(response.toDomain)
        case let .unknownError(error): .internalError(error)
        }
    }
}

extension APIResponse {
    var toDomain: ServerError {
        .init(statusCode: statusCode, code: serverError?.code, message: serverError?.message)
    }
}
