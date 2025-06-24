import Foundation

class AtLeastValidator<T: Any>: Validator<T> {
    let count: Int
    let errorMessage: String?
    let validations: [Validator<T>]

    public init(count: Int, errorMessage: String?, validations: [Validator<T>]) {
        self.count = count
        self.errorMessage = errorMessage
        self.validations = validations
        super.init()
    }

    override public func validate(_ value: T?) -> Result {
        validations
            .map {
                $0.validate(value)
            }
            .reduce(0) { $0 + ($1.isValid ? 1 : 0) } >= count ? .success : .error(errorMessage)
    }
}

public extension Validator {
    static func atLeast(_ count: Int, _ message: String?, _ validations: Validator<T>...) -> Validator<T> {
        AtLeastValidator(count: count, errorMessage: message, validations: validations)
    }
}
