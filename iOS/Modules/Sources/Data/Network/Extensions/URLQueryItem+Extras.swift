import Foundation
import FoundationUtils

extension URLQueryItem {
    init(name: String, intValue: Int?) {
        self.init(name: name, value: intValue?.toString)
    }
}
