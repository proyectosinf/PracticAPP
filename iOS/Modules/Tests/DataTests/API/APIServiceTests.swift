@testable import Data
import XCTest

final class APIServiceTests: XCTestCase {
    lazy var session: URLSession = {
        let configuration = URLSessionConfiguration.ephemeral
        configuration.protocolClasses = [URLProtocolMock.self]
        return .init(configuration: configuration)
    }()

    lazy var api: APIService = .init(baseURL: "https://api.com", session: session)

    func testSuccess() async throws {
        URLProtocolMock.requestHandler = { .response($0.url!, code: 200) }
        let response = try! await api.get("/api/v1/me")
        XCTAssertEqual(response.request.url?.absoluteString, "https://api.com/api/v1/me")
    }

    func testStatusCodeError() async throws {
        URLProtocolMock.requestHandler = { .response($0.url!, code: 401) }
        do {
            _ = try await api.get("/api/v1/me")
            XCTFail("Error must to be thrown")
        } catch {
            XCTAssertEqual((error as? APIErrorData)?.apiResponse?.statusCode, 401)
        }
    }

    func testServerErrorData() async throws {
        URLProtocolMock.requestHandler = { .response($0.url!, code: 401, data: .json("server_error_unauthorized")!) }
        do {
            _ = try await api.get("/api/v1/me")
            XCTFail("Error must to be thrown")
        } catch {
            let apiError = error as? APIErrorData
            XCTAssertEqual(apiError?.apiResponse?.statusCode, 401)
            XCTAssertEqual(apiError?.serverError?.code, 401)
            XCTAssertEqual(apiError?.serverError?.message, "Unauthorized")
        }
    }

    func testGetWithQueryParams() async throws {
        URLProtocolMock.requestHandler = { .response($0.url!, code: 200) }
        let query: [URLQueryItem] = [.init(name: "age", intValue: 40), .init(name: "name", value: "Álvaro")]
        let response = try! await api.get("/api/v1/user", query: query)
        XCTAssertEqual(response.request.url?.absoluteString, "https://api.com/api/v1/user?age=40&name=%C3%81lvaro")
    }

    func testUnkownError() async throws {
        URLProtocolMock.requestHandler = { _ in .error(URLError(.timedOut)) }
        do {
            _ = try await api.get("/api/v1/me")
            XCTFail("Error must to be thrown")
        } catch {
            switch error as? APIErrorData {
            case let .unknownError(error):
                XCTAssertEqual((error as? URLError)?.code, .timedOut)
            default:
                XCTFail("Unexpected error type thrown")
            }
        }
    }

    func testInvalidUrlError() async throws {
        URLProtocolMock.requestHandler = { .response($0.url!, code: 200) }
        let api = APIService(baseURL: "_http:wrongurl", session: session)
        do {
            _ = try await api.get("/api/v1/me")
            XCTFail("Error must to be thrown")
        } catch {
            switch error as? APIErrorData {
            case let .unknownError(error):
                XCTAssertEqual((error as? URLError)?.code, .badURL)
            default:
                XCTFail("Unexpected error type thrown")
            }
        }
    }
}
