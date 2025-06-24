import Factory
import Foundation
import Logger

public struct APILogger {
    private let logger: Logger

    public init(container: Container = .shared) {
        logger = container.logger()
    }

    func log(apiResponse: APIResponse, session: URLSession) {
        let curl = apiResponse.request.curl(session: session)
        var message = "REQUEST:\n\(curl)"
        if let body = apiResponse.data.prettyPrintedJSONString {
            message += "\n\nRESPONSE:\nCode: \(String(apiResponse.statusCode ?? 0))\n\(body)"
        }
        logger.log(.error, .network, message: message)
    }

    func log(request: URLRequest, session: URLSession, error: Error) {
        let curl = request.curl(session: session)
        var message = "REQUEST:\n\(curl)"
        if let apiError = error as? APIErrorData {
            let statusCode = String(apiError.apiResponse?.statusCode ?? 0)
            if let body = apiError.apiResponse?.data.prettyPrintedJSONString {
                message += "\n\nRESPONSE:\nCode: \(statusCode)\n\(body)"
            } else {
                message += "\n\nERROR:\n\(apiError)"
            }
        } else {
            message += "\n\nERROR:\n\(error)"
        }
        logger.log(.error, .network, message: message)
    }
}

extension APILogger: Interceptor {
    public func intercept(
        request: URLRequest,
        session: URLSession,
        next: (URLRequest, URLSession) async throws -> APIResponse
    ) async throws -> APIResponse {
        do {
            let response = try await next(request, session)
            log(apiResponse: response, session: session)
            return response
        } catch {
            log(request: request, session: session, error: error)
            throw error
        }
    }
}
