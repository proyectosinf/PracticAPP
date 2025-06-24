import Foundation

class ANDValidator<T: Any>: Validator<T> {
    let validations: [Validator<T>]

    public init(validations: [Validator<T>]) {
        self.validations = validations
        super.init()
    }

    override public func validate(_ value: T?) -> Result {
        for validation in validations {
            let result = validation.validate(value)
            guard result.isValid else { return result }
        }
        return .success
    }
}

public extension Validator {
    static func and(_ validations: Validator<T>...) -> Validator<T> {
        ANDValidator(validations: validations)
    }
}
