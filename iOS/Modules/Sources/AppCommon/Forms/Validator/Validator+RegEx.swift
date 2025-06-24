import Foundation

public extension Validator {
    struct RegEx {
        let value: String

        public init(_ value: String) {
            self.value = value
        }
    }
}

// swiftlint:disable line_length
public extension Validator.RegEx {
    static var email: Self {
        .init(
            "^(?:[\\p{L}0-9!#$%\\&'*+/=?\\^_`{|}~-]+(?:\\.[\\p{L}0-9!#$%\\&'*+/=?\\^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[\\p{L}0-9](?:[a-z0-9-]*[\\p{L}0-9])?\\.)+[\\p{L}0-9](?:[\\p{L}0-9-]*[\\p{L}0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[\\p{L}0-9-]*[\\p{L}0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$"
        )
    }

    static var positiveInteger: Self { .init("^\\d+$") }

    static var url: Self {
        .init(
            "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)"
        )
    }

    static var location: Self {
        .init(
            "^\\(?[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s?[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?\\)?)$"
        )
    }

    static var postalCode: Self { .init("^\\d{5}$") }

    static var documentNumber: Self { .init("^[a-zA-Z0-9]{1,20}$") }
}

// swiftlint:enable line_length

public extension String {
    func matches(regEx: Validator<String>.RegEx) -> Bool {
        Validator.regex(regEx, message: nil).validate(self).isValid
    }
}

public extension Validator where T == String {
    static func regex(_ regex: String, message: String? = nil) -> Validator<T> {
        .closure(error: message, validation: { ($0 ?? "")
                .range(of: regex, options: .regularExpression, range: nil, locale: nil) != nil
        })
    }

    static func regex(_ regex: RegEx, message: String?) -> Validator<T> {
        .closure(error: message, validation: {
            ($0 ?? "").range(of: regex.value, options: .regularExpression, range: nil, locale: nil) != nil
        })
    }

    static func email(message: String?) -> Validator<T> {
        .regex(.email, message: message)
    }

    static func password(message: String?, invalidCharacterMessage: String? = nil) -> Validator<T> {
        .and(
            .minLength(length: 8, message: message),
            .atLeast(
                3,
                message,
                .regex("^(?=.*\\p{Ll}).*$"), // Lower case letters
                .regex("^(?=.*\\p{Lu}).*$"), // Upper case letters
                .regex("^(?=.*\\d).*$"), // Digits
                .regex("^(?=.*[\(passwordSpecialCharacters)]).*$") // Special characters
            ),
            .regex("^[\\p{L}\\d\(passwordSpecialCharacters)]+$", message: invalidCharacterMessage)
        )
    }
    static func optional(_ validator: Validator<T>) -> Validator<T> {
            .closure(error: "Valor inválido") { value in
                guard let value, !value.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
                    return true
                }
                return validator.validate(value).isValid
            }
        }
}
