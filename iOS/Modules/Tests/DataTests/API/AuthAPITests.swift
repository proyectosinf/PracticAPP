@testable import Data
import Factory
import XCTest

final class AuthAPITests: XCTestCase {
    var authAPI: AuthAPI!

    override func setUpWithError() throws {
        Container.shared.publicSession.register {
            let configuration = URLSessionConfiguration.ephemeral
            configuration.protocolClasses = [URLProtocolMock.self]
            return .init(configuration: configuration)
        }
        authAPI = Container.shared.authAPI()
    }

    func testSignInSuccess() async throws {
        URLProtocolMock.requestHandler = { .response($0.url!, code: 200, data: .json("sign_in_ok")!) }
        let token = try await authAPI.signIn(user: "test@email.com", password: "test1234")
        XCTAssertEqual(
            token.accessToken,
            "eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTIzLCJ1c2VybmFtZSI6ImphbmVkZW9lIiwiaWF0IjoxNjAyMDUzODA2fQ.YJh3DsFZS0FXE6FgFug0Wmpwc4KXZ52_XngB1CxXXmg"
        )
        XCTAssertEqual(token.refreshToken, "b8bafd64-0a42-4f26-89af-cb876f33c5a6")
    }

    func testSignInWrongCredentials() async throws {
        URLProtocolMock.requestHandler = { .response($0.url!, code: 401, data: .json("sign_in_ko")!) }
        do {
            _ = try await authAPI.signIn(user: "test@email.com", password: "test1234")
            XCTFail("Sign in should throw an error")
        } catch let error as APIErrorData {
            XCTAssertEqual(error.apiResponse?.serverError?.code, 401)
            XCTAssertEqual(error.apiResponse?.serverError?.message, "Unauthorized")
        } catch {
            XCTFail("Error type not expected")
        }
    }

    func testRefreshTokenSuccess() async throws {
        URLProtocolMock.requestHandler = { .response($0.url!, code: 200, data: .json("refresh_token_ok")!) }
        let token = await authAPI.refreshToken(refreshToken: "b8bafd64-0a42-4f26-89af-cb876f33c5a6")
        XCTAssertEqual(
            token?.accessToken,
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        )
        XCTAssertEqual(token?.refreshToken, "d1e57614-d3a5-4a61-a6c2-56e2b49eab47")
    }
}
