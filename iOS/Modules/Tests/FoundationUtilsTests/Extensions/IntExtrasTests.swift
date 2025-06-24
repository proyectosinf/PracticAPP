@testable import FoundationUtils
import Testing

@Suite("Int Extras Tests") struct IntExtrasTests {
    @Test func identifiable() throws {
        #expect(IdentifiableInt(value: 8).id == 8)
    }

    @Test func toString() throws {
        #expect(8.toString == "8")
    }
}
