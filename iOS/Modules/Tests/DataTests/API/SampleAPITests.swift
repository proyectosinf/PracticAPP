@testable import Data
import Factory
@testable import Mocks
import XCTest

final class SampleAPITests: XCTestCase, @unchecked Sendable {
    var sampleAPI: SampleAPI!
    var authAPI: AuthAPI!
    var mockKeychain: KeychainDataSourceMock!
    override func setUpWithError() throws {
        Container.shared.publicSession.register {
            let configuration = URLSessionConfiguration.ephemeral
            configuration.protocolClasses = [URLProtocolMock.self]
            return .init(configuration: configuration)
        }
        Container.shared.privateSession.register {
            let configuration = URLSessionConfiguration.ephemeral
            configuration.protocolClasses = [URLProtocolMock.self]
            return .init(configuration: configuration)
        }
        mockKeychain = KeychainDataSourceMock()
        Container.shared.keychainDataSource.register { self.mockKeychain }
        sampleAPI = Container.shared.sampleAPI()
        authAPI = Container.shared.authAPI()
    }
    func testInterceptorAuthorizationWithToken() async throws {
        mockKeychain.tokenServiceStringAccountStringAPITokenDataReturnValue = .init(
            accessToken: "ABCD",
            refreshToken: "EFG"
        )
        URLProtocolMock.requestHandler = { request in
            XCTAssertEqual(
                self.authAPI.httpAdditionalHeaders["Authorization"],
                "Bearer ABCD"
            )
            return .response(request.url!, code: 200)
        }
        try await sampleAPI.signUp(request: .init(email: "", password: ""))
    }
    func testInterceptorAuthorizationWithoutToken() async throws {
        mockKeychain.tokenServiceStringAccountStringAPITokenDataReturnValue = nil
        URLProtocolMock.requestHandler = { request in
            XCTAssertNil(request.value(forHTTPHeaderField: "Authorization"))
            return .response(request.url!, code: 200)
        }
        try await sampleAPI.signUp(request: .init(email: "", password: ""))
    }

#warning("//TODO: Fix test")
    //    func testInterceptorAuthorizationAfterSignIn() async throws {
    //        mockKeychain.tokenServiceStringAccountStringAPITokenDataReturnValue = nil
    //        mockKeychain.saveTokenAPITokenDataServiceStringAccountStringBoolReturnValue = true
    //        URLProtocolMock.requestHandler = { request in
    //            if request.url?.absoluteString == "\(Container.shared.environment().apiDomain)/login" {
    //                return .response(request.url!, code: 200, data: .json("sign_in_ok")!)
    //            } else if request.url?.absoluteString == "\(Container.shared.environment().apiDomain)/user" {
    //                XCTAssertEqual(
    //                    self.authAPI.httpAdditionalHeaders["Authorization"],
    //                    "Bearer eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTIzLCJ1c2VybmFtZSI6ImphbmVkZW9lIiwiaWF0IjoxNjAyMDUzODA2fQ.YJh3DsFZS0FXE6FgFug0Wmpwc4KXZ52_XngB1CxXXmg"
    //                )
    //                return .response(request.url!, code: 200, data: .json("user_ok")!)
    //            } else {
    //                return .response(request.url!, code: 200)
    //            }
    //        }
    //        _ = try await authAPI.signIn(user: "test@email.com", password: "Test1234")
    //        let user = try await sampleAPI.user()
    //        XCTAssertEqual(
    //            mockKeychain.tokenServiceStringAccountStringAPITokenDataReturnValue?.accessToken,
    //            "eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTIzLCJ1c2VybmFtZSI6ImphbmVkZW9lIiwiaWF0IjoxNjAyMDUzODA2fQ.YJh3DsFZS0FXE6FgFug0Wmpwc4KXZ52_XngB1CxXXmg"
    //        )
    //        XCTAssertEqual(user.name, "Álvaro")
    //        XCTAssertEqual(user.email, "alvaro.murillo@mobivery.com")
    //    }
}
