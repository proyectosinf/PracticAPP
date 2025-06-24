import Domain
import Foundation

/// Executes an asynchronous operation and normalizes any thrown errors, mapping them to domain-layer errors.
/// This function is designed to evolve and handle additional error types in the future.
func normalizeError<T>(_ operation: () async throws -> T) async throws -> T {
    do {
        return try await operation()
    } catch let error as APIErrorData {
        throw error.toDomain
    } catch {
        throw AppError.internalError(error)
    }
}

/// Executes a synchronous operation and normalizes any thrown errors, mapping them to domain-layer errors.
/// This function is designed to evolve and handle additional error types in the future.
func normalizeError<T>(_ operation: () throws -> T) throws -> T {
    do {
        return try operation()
    } catch let error as APIErrorData {
        throw error.toDomain
    } catch {
        throw AppError.internalError(error)
    }
}
extension APIErrorData {
    var toRegisterCompanyDomainError: AppError {
        guard let serverError = serverError else {
            return .unknown("Error desconocido del servidor")
        }

        switch serverError.code {
        case 4001:
            if serverError.message.contains("name") {
                return .validation(.companyNameExists)
            } else if serverError.message.contains("cif") {
                return .validation(.companyCifExists)
            } else {
                return .validation(.invalidData)
            }

        case 2901:
            return .serverError(ServerError(statusCode: 500, code: 2901, message: "Error interno al crear la empresa"))

        default:
            return .serverError(serverError.toDomain())
        }
    }
}
extension ServerErrorData {
    func toDomain(statusCode: Int? = nil) -> ServerError {
        return ServerError(
            statusCode: statusCode,
            code: self.code,
            message: self.message
        )
    }
}
