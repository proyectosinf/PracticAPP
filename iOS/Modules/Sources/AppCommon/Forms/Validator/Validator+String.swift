import Foundation

public extension Validator where T == String {
    static func empty(message: String?) -> Validator<T> {
        .closure(error: message, validation: { $0?.isEmpty ?? true })
    }

    static func mandatory(
        message: String? = Strings.general.common_mandatory_text,
        ignoreSpaces: Bool = false
    ) -> Validator<T> {
        if ignoreSpaces {
            .closure(
                error: message,
                validation: {
                    $0?.trimmingCharacters(
                        in: .whitespacesAndNewlines
                    ).isEmpty == false
                }
            )
        } else {
            .closure(error: message, validation: { $0?.isEmpty == false })
        }
    }

    static func length(range: CountableClosedRange<Int>, message: String?) -> Validator<T> {
        .closure(error: message) { range.contains($0?.count ?? 0) }
    }

    static func length(_ length: Int, message: String?) -> Validator<T> {
        .closure(error: message) { ($0?.count ?? 0) == length }
    }

    static func minLength(length: Int, message: String?) -> Validator<T> {
        .closure(error: message) { ($0?.count ?? 0) >= length }
    }

    static func url(message: String?) -> Validator<T> { .regex(.url, message: message) }

    static let passwordSpecialCharacters = "*.!@#$%&,.?_+-" // *.!@#$%&,.?_+-

    static func maxLength(length: Int, message: String?) -> Validator<T> {
        .closure(error: message) { ($0?.count ?? 0) <= length }
    }

    static func dni(message: String?) -> Validator<T> {
        .closure(error: message) { value in
            guard let dni = value, dni.count == 9 else { return false }

            let numberPart = dni.prefix(8)
            let letterPart = dni.suffix(1)

            guard numberPart.range(of: "^\\d{8}$", options: .regularExpression) != nil,
                  letterPart.range(of: "^[A-Z]$", options: .regularExpression) != nil,
                  let number = Int(numberPart) else {
                return false
            }

            let letters = Array("TRWAGMYFPDXBNJZSQVHLCKE")
            let expectedLetter = letters[number % 23]

            return letterPart == String(expectedLetter)
        }
    }
    static func cif(message: String?) -> Validator<T> {
        .closure(error: message) { value in
            guard let cif = value else { return false }

            let pattern = #"^(?:[A-Z]\d{8}|\d{8}[A-Z])$"#
            let regex = try? NSRegularExpression(pattern: pattern)

            let range = NSRange(location: 0, length: cif.utf16.count)
            return regex?.firstMatch(in: cif, options: [], range: range) != nil
        }
    }

}
