import Foundation
import SwiftUI

public extension String {
    var toURL: URL? { URL(string: self) }

    var emptyToNil: String? { isEmpty ? nil : self }

    var isInt: Bool { Int(self) != nil }

    var toInt: Int? { Int(self) }

    var trimmingWhitespaces: Self { trimmingCharacters(in: .whitespacesAndNewlines) }

    func mandatory(value: Bool) -> Self {
        guard value else { return self }
        return "*" + self
    }
}
