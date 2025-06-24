import Foundation
import XCTest

enum MockRequestResponse {
    case error(URLError)
    case response(value: HTTPURLResponse, data: Data)
}

extension MockRequestResponse {
    static func response(
        _ url: URL,
        code: Int,
        httpVersion: String? = nil,
        headerFields: [String: String]? = nil,
        data: Data = Data()
    ) -> MockRequestResponse {
        .response(
            value: .init(url: url, statusCode: code, httpVersion: httpVersion, headerFields: headerFields)!,
            data: data
        )
    }
}

class URLProtocolMock: URLProtocol {
    /// Dictionary maps URLs to tuples of error, data, and response
    nonisolated(unsafe) static var requestHandler: ((URLRequest) -> MockRequestResponse)?

    override class func canInit(with request: URLRequest) -> Bool { true }

    override class func canonicalRequest(for request: URLRequest) -> URLRequest { request }

    override func startLoading() {
        guard let handler = Self.requestHandler else { XCTFail("No request handler provided."); return }
        switch handler(request) {
        case let .error(error):
            client?.urlProtocol(self, didFailWithError: error)
        case let .response(value, data):
            client?.urlProtocol(self, didReceive: value, cacheStoragePolicy: .notAllowed)
            client?.urlProtocol(self, didLoad: data)
        }
        client?.urlProtocolDidFinishLoading(self)
    }

    override func stopLoading() {}
}
