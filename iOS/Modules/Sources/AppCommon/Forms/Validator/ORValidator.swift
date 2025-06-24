import Foundation

class ORValidator<T: Any>: Validator<T> {
    let validations: [Validator<T>]

    public init(validations: [Validator<T>]) {
        self.validations = validations
        super.init()
    }

    override public func validate(_ value: T?) -> Result {
        var lastResult: Result?
        for validation in validations {
            let result = validation.validate(value)
            guard !result.isValid else { return .success }
            lastResult = result
        }
        return lastResult ?? .success
    }
}

public extension Validator {
    static func or(_ validations: Validator<T>...) -> Validator<T> {
        ORValidator(validations: validations)
    }
}
