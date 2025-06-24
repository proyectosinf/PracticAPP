import Factory
import Foundation

public final class APIService: Sendable {
    let baseURL: String
    let session: URLSession
    let encoder: RequestEncoder
    let decoder: ResponseDecoder
    let interceptors: [Interceptor]

    public init(
        baseURL: String,
        session: URLSession,
        encoder: RequestEncoder = JSONEncoder(),
        decoder: ResponseDecoder = JSONDecoder(),
        interceptors: [Interceptor] = []
    ) {
        self.baseURL = baseURL
        self.session = session
        self.encoder = encoder
        self.decoder = decoder
        self.interceptors = interceptors
    }

    @discardableResult
    func response(_ definition: RequestDefinition<some Any>) async throws -> APIResponse {
        let request = try buildRequest(definition)
        do {
            var next: (URLRequest, URLSession) async throws -> APIResponse = { request, session in
                let (data, response) = try await session.data(for: request)
                return .init(request: request, response: response, data: data, decoder: self.decoder)
            }
            for interceptor in interceptors.reversed() {
                let nextInterceptor = next
                next = { request, session in
                    try await interceptor.intercept(
                        request: request,
                        session: session,
                        next: nextInterceptor
                    )
                }
            }
            return try await next(request, session).validate()
        } catch let error as APIErrorData {
            throw error
        } catch {
            throw APIErrorData.unknownError(error)
        }
    }

    @discardableResult
    func get(
        _ path: String,
        query: [URLQueryItem]? = nil,
        headers: [String: String]? = nil
    ) async throws -> APIResponse {
        let body: RequestBody<String>? = nil
        return try await response(
            .init(
                method: .GET,
                path: path,
                query: query,
                body: body,
                headers: headers
            )
        )
    }

    @discardableResult
    func post(
        _ path: String,
        body: some Encodable,
        encoder: RequestEncoder? = nil,
        headers: [String: String]? = nil
    ) async throws -> APIResponse {
        return try await response(
            .init(
                method: .POST,
                path: path,
                body: .init(content: body),
                encoder: encoder ?? self.encoder,
                headers: headers
            )
        )
    }

    @discardableResult
    func put(
        _ path: String,
        body: some Encodable,
        encoder: RequestEncoder? = nil,
        headers: [String: String]? = nil
    ) async throws -> APIResponse {
        return try await response(
            .init(
                method: .PUT,
                path: path,
                body: .init(content: body),
                encoder: encoder ?? self.encoder,
                headers: headers
            )
        )
    }

    private func buildRequest(_ definition: RequestDefinition<some Any>) throws -> URLRequest {
        var components = URLComponents(string: baseURL + definition.path)
        components?.percentEncodedQueryItems = definition.query?.map {
            .init(name: $0.name, value: $0.value?.addingPercentEncoding(withAllowedCharacters: .fixedUrlQueryAllowed))
        }
        guard let url = components?.url else { throw APIErrorData.unknownError(URLError(.badURL)) }

        var request = URLRequest(url: url)
        request.httpMethod = definition.method.rawValue

        if let headers = definition.headers {
            for (key, value) in headers {
                request.setValue(value, forHTTPHeaderField: key)
            }
        }

        if let body = definition.body {
            do {
                let usedEncoder = definition.encoder ?? encoder
                request.setValue(usedEncoder.contentType, forHTTPHeaderField: "Content-Type")
                request.httpBody = try usedEncoder.encode(body.content)
            } catch {
                throw APIErrorData.encodingError(error)
            }
        }
        return request
    }

}
