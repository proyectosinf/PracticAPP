@testable import AppCommon
import Testing

@Suite("RegEx Validator Tests") struct ValidatorRegExTests {
    @Test func passwordHasUppercaseLowerCaseNumber() {
        #expect("MyPassword123".passesValidation(validator: .password(message: nil)) == true)
    }

    @Test func passwordHasUppercaseLowerCaseSpecial() {
        #expect("MyPassword*".passesValidation(validator: .password(message: nil)) == true)
    }

    @Test func passwordHasUpperCaseNumberSpecial() {
        #expect("MYPASSWORD1$".passesValidation(validator: .password(message: nil)) == true)
    }

    @Test func passwordHasLowerCaseNumberSpecial() {
        #expect("mypassword1+".passesValidation(validator: .password(message: nil)) == true)
    }

    @Test func passwordNotAllowedCharacter() {
        #expect("MyPassword1 ".passesValidation(validator: .password(message: nil)) == false)
    }

    @Test func passwordNoUpperCaseNoNumber() {
        #expect("mypassword/".passesValidation(validator: .password(message: nil)) == false)
    }

    @Test func passwordNoUpperCaseNoSpecial() {
        #expect("mypassword1".passesValidation(validator: .password(message: nil)) == false)
    }

    @Test func passwordNoLowerCaseNoNumber() {
        #expect("MYPASSWORD$".passesValidation(validator: .password(message: nil)) == false)
    }

    @Test func passwordNoLowerCaseNoSpecial() {
        #expect("MYPASSWORD1".passesValidation(validator: .password(message: nil)) == false)
    }

    @Test func passwordNoSpecialNoNumber() {
        #expect("MyPassword".passesValidation(validator: .password(message: nil)) == false)
    }

    @Test func passwordLenght7() {
        #expect("MyPas1$".passesValidation(validator: .password(message: nil)) == false)
    }

    @Test func invalidCharacterMessage() {
        let validator = Validator<String>.password(
            message: "default message",
            invalidCharacterMessage: "invalid character message"
        )
        let validation = validator.validate("MyPassword1 ")
        #expect(validation.error == "invalid character message")
    }

    @Test func validDNI_withValidLetters() {
        #expect("12345678Z".passesValidation(validator: .dni(message: nil)) == true)
        #expect("00000000T".passesValidation(validator: .dni(message: nil)) == true)
        #expect("99999999R".passesValidation(validator: .dni(message: nil)) == true)
    }

    @Test func invalidDNI_withWrongLetter() {
        #expect("12345678A".passesValidation(validator: .dni(message: nil)) == false)
        #expect("00000000X".passesValidation(validator: .dni(message: nil)) == false)
    }

    @Test func invalidDNI_withNonDigits() {
        #expect("ABCDEFGHZ".passesValidation(validator: .dni(message: nil)) == false)
        #expect("1234ABCDZ".passesValidation(validator: .dni(message: nil)) == false)
    }

    @Test func invalidDNI_withIncorrectLength() {
        #expect("1234567Z".passesValidation(validator: .dni(message: nil)) == false)  // 7 digits + 1 letter
        #expect("123456789Z".passesValidation(validator: .dni(message: nil)) == false) // 9 digits + 1 letter
        #expect("".passesValidation(validator: .dni(message: nil)) == false) // empty
    }

    @Test func invalidDNI_withLowercaseLetter() {
        #expect("12345678z".passesValidation(validator: .dni(message: nil)) == false) // letter should be uppercase
    }

    @Test func invalidDNI_withSpecialCharacters() {
        #expect("1234@567Z".passesValidation(validator: .dni(message: nil)) == false)
        #expect("12345678!".passesValidation(validator: .dni(message: nil)) == false)
    }
}

extension String {
    func passesValidation(validator: Validator<String>) -> Bool {
        validator.validate(self).isValid
    }
}
