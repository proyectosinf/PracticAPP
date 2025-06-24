@testable import Data
import XCTest

final class URLRequestCurlTests: XCTestCase {
    func testCurlCommandWithoutHeadersOrBody() {
        var request = URLRequest(url: URL(string: "https://example.com")!)
        request.httpMethod = "GET"

        let curlCommand = request.curl()
        XCTAssertEqual(curlCommand, "curl -X GET 'https://example.com'")
    }

    func testCurlCommandWithHeaders() {
        var request = URLRequest(url: URL(string: "https://example.com")!)
        request.httpMethod = "GET"
        request.setValue("Bearer token", forHTTPHeaderField: "Authorization")
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")

        let curlCommand = request.curl()
        XCTAssertEqual(
            curlCommand,
            "curl -X GET 'https://example.com' -H 'Authorization: Bearer token' -H 'Content-Type: application/json'"
        )
    }

    func testCurlCommandWithBody() {
        var request = URLRequest(url: URL(string: "https://example.com")!)
        request.httpMethod = "POST"
        let bodyData = Data("{\"key\": \"value\"}".utf8)
        request.httpBody = bodyData

        let curlCommand = request.curl()
        XCTAssertEqual(curlCommand, "curl -X POST 'https://example.com' -d '{\"key\": \"value\"}'")
    }

    func testCurlCommandWithSessionHeaders() {
        var request = URLRequest(url: URL(string: "https://example.com")!)
        request.httpMethod = "GET"

        let sessionConfig = URLSessionConfiguration.default
        sessionConfig.httpAdditionalHeaders = ["Custom-Header": "CustomValue"]
        let session = URLSession(configuration: sessionConfig)

        let curlCommand = request.curl(session: session)
        XCTAssertEqual(curlCommand, "curl -X GET 'https://example.com' -H 'Custom-Header: CustomValue'")
    }

    func testCurlCommandWithHeadersBodyAndSessionHeaders() {
        var request = URLRequest(url: URL(string: "https://example.com")!)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("Bearer token", forHTTPHeaderField: "Authorization")
        let bodyData = Data("{\"key\": \"value\"}".utf8)
        request.httpBody = bodyData

        let sessionConfig = URLSessionConfiguration.default
        sessionConfig.httpAdditionalHeaders = ["Custom-Header": "CustomValue"]
        let session = URLSession(configuration: sessionConfig)

        let curlCommand = request.curl(session: session)
        XCTAssertEqual(
            curlCommand,
            "curl -X POST 'https://example.com' -H 'Authorization: Bearer token' -H 'Content-Type: application/json' -H 'Custom-Header: CustomValue' -d '{\"key\": \"value\"}'"
        )
    }
}
