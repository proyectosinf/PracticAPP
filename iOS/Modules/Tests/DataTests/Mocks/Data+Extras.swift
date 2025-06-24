import Foundation
import XCTest

extension Data {
    static func json(_ fileName: String) -> Data? {
        guard let filePath = Bundle.module.path(forResource: fileName, ofType: "json") else {
            XCTFail("Json named \(fileName) not found")
            return nil
        }
        let url = URL(fileURLWithPath: filePath)
        return try? Data(contentsOf: url)
    }
}
