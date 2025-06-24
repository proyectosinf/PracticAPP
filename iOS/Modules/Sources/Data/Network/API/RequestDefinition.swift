import Foundation

enum RequestMethod: String {
    case GET, POST, PUT, PATCH
}

struct RequestBody<T: Encodable> {
    let content: T
}

struct RequestDefinition<T: Encodable> {
    let method: RequestMethod
    let path: String
    let body: RequestBody<T>?
    let encoder: RequestEncoder?
    let decoder: ResponseDecoder?
    let query: [URLQueryItem]?
    let headers: [String: String]?

    init(
        method: RequestMethod,
        path: String,
        query: [URLQueryItem]? = nil,
        body: RequestBody<T>? = nil,
        encoder: RequestEncoder? = nil,
        decoder: ResponseDecoder? = nil,
        headers: [String: String]? = nil
    ) {
        self.method = method
        self.path = path
        self.query = query
        self.body = body
        self.encoder = encoder
        self.decoder = decoder
        self.headers = headers
    }
}

extension RequestDefinition {
    static func POST<B: Encodable>(_ path: String, body: B) -> RequestDefinition<B> {
        .init(method: .POST, path: path, body: .init(content: body))
    }

    static func GET(
        _ path: String,
        encoder: RequestEncoder? = nil,
        decoder: ResponseDecoder? = nil,
        query: [URLQueryItem]? = nil
    ) -> RequestDefinition<String> {
        .init(method: .GET, path: path, query: query, body: nil, encoder: encoder, decoder: decoder)
    }
}
