import Foundation

open class Validator<T: Any> {
    public enum Result { case success, error(String?) }
    open func validate(_ value: T?) -> Result { .success }

    public init() {}
}

public extension Validator.Result {
    var isValid: Bool {
        switch self {
        case .success: true
        case .error: false
        }
    }

    var error: String? {
        switch self {
        case .success: nil
        case let .error(error): error
        }
    }
}
