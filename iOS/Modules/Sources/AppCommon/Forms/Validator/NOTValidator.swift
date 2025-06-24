import Foundation

class NOTValidator<T: Any>: Validator<T> {
    let validation: Validator<T>
    let errorMessage: String?

    public init(validation: Validator<T>, errorMessage: String?) {
        self.errorMessage = errorMessage
        self.validation = validation
        super.init()
    }

    override public func validate(_ value: T?) -> Result {
        validation.validate(value).isValid ? .error(errorMessage) : .success
    }
}

public extension Validator {
    static func not(validation: Validator<T>, error: String?) -> Validator<T> {
        NOTValidator(validation: validation, errorMessage: error)
    }
}
