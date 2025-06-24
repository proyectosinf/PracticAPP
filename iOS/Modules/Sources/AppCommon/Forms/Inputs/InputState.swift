import Foundation

public struct InputState<T: Equatable> {
    public var value: T? {
        didSet {
            guard value != oldValue else { return }
            if !isFirstError { validate() }
            onValueDidChange?(value)
        }
    }

    public var error: String? {
        didSet { if error?.isEmpty == false { isFirstError = false } }
    }

    public var validator: Validator<T>?
    public var onValueDidChange: ((T?) -> Void)?
    private var isFirstError: Bool = true

    public init(
        value: T? = nil,
        error: String? = nil,
        validator: Validator<T>? = nil,
        onValueDidChange: ((T?) -> Void)? = nil
    ) {
        self.value = value
        self.error = error
        self.validator = validator
        self.onValueDidChange = onValueDidChange
    }
}

extension InputState: ValidatableInput {
    @discardableResult public mutating func validate() -> Bool {
        guard let validator else { return true }
        let result = validator.validate(value)
        error = result.error
        return result.isValid
    }
}
public extension InputState {
    mutating func clearError() {
        self.error = nil
    }
}
