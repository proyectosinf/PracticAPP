import Foundation

class ClosureValidator<T: Any>: Validator<T> {
    let validation: (T?) -> Bool
    let errorMessage: String?

    public init(validation: @escaping (T?) -> Bool, errorMessage: String?) {
        self.validation = validation
        self.errorMessage = errorMessage
        super.init()
    }

    override public func validate(_ value: T?) -> Result { validation(value) ? .success : .error(errorMessage) }
}

public extension Validator {
    static func closure(error: String?, validation: @escaping (T?) -> Bool) -> Validator<T> {
        ClosureValidator(validation: validation, errorMessage: error)
    }
}
