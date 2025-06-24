import Foundation
import Testing

@Suite("String Extras Tests") struct StringExtrasTests {
    @Test func isInt() {
        #expect("4".isInt == true)
        #expect("4a".isInt == false)
        #expect("a".isInt == false)
        #expect(",".isInt == false)
    }

    @Test func toURL() {
        #expect("http://google.com".toURL == URL(string: "http://google.com"))
    }

    @Test func emptyToNil() {
        #expect("".emptyToNil == nil)
        #expect(" ".emptyToNil == " ")
    }
}
