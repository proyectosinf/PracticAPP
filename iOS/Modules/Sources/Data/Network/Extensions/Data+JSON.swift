import Foundation

extension Data {
    var prettyPrintedJSONString: String? {
        guard
            let object = try? JSONSerialization.jsonObject(with: self, options: []),
            let data = try? JSONSerialization.data(withJSONObject: object, options: [.prettyPrinted]),
            let prettyPrintedString = String(bytes: data, encoding: .utf8)
        else { return nil }

        return prettyPrintedString
    }
}
