import Foundation

public extension Optional where Wrapped == String {
    var sanitizedOrNil: String? {
        guard let self = self, !self.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
            return nil
        }
        return self
    }
}
