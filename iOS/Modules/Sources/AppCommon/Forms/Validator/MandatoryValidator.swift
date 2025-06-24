import Foundation

public extension Validator {
    static func mandatory(message: String?) -> Validator<T> {
        ClosureValidator(validation: { $0 != nil }, errorMessage: message)
    }
}
