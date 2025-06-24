import Foundation

public protocol Interceptor: Sendable {
    typealias Next = (URLRequest, URLSession) async throws -> APIResponse
    func intercept(
        request: URLRequest,
        session: URLSession,
        next: @escaping Next
    ) async throws -> APIResponse
}
