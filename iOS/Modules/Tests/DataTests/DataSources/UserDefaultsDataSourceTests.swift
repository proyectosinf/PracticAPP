@testable import Data
import XCTest

final class UserDefaultsDataSourceTests: XCTestCase {
    var sut: UserDefaultsDataSourceImpl!
    let sampleAppVersion = "1.0.0"

    override func setUpWithError() throws {
        sut = UserDefaultsDataSourceImpl()
        try super.setUpWithError()
    }

    override func tearDownWithError() throws {
        sut = nil
        try super.tearDownWithError()
    }

    func testSaveLastInstalledVersion() {
        sut.lastInstalledVersion = sampleAppVersion
        XCTAssertEqual(sut.lastInstalledVersion, sampleAppVersion)
    }

    func testDeleteLastInstalledVersion() {
        sut.lastInstalledVersion = sampleAppVersion
        XCTAssertEqual(sut.lastInstalledVersion, sampleAppVersion)
        sut.lastInstalledVersion = nil
        XCTAssertNil(sut.lastInstalledVersion)
    }
}
