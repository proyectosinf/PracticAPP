import Foundation

public enum AppError: Error {
    case serverError(ServerError?)
    case internalError(Error?)
    case networkError
    case authenticationError(String)
    case sessionExpired
    case unknown(String)
    case validation(ValidationError)

    public enum ValidationError: Sendable {
        case companyNameExists
        case companyCifExists
        case invalidData
    }
}

public extension AppError {
    var message: String {
        switch self {
        case .authenticationError(let msg):
            return msg
        case .unknown(let msg):
            return msg
        case .networkError:
            return "No hay conexión a internet."
        case .serverError(let serverError):
            return serverError?.message ?? "Error del servidor."
        case .sessionExpired:
            return "Tu sesión ha expirado."
        case .internalError(let error):
            return error?.localizedDescription ?? "Error interno inesperado."
        case .validation(let error):
            switch error {
            case .companyNameExists:
                return "Ya existe una empresa con ese nombre."
            case .companyCifExists:
                return "El CIF ya está registrado."
            case .invalidData:
                return "Los datos introducidos no son válidos."
            }
        }
    }
}
