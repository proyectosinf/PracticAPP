import Domain
import Testing

final class AppVersionTests {
    @Test func initializer() throws {
        // Given
        let sut = AppVersion("1.2.3")

        // Then
        #expect(sut.major == 1)
        #expect(sut.minor == 2)
        #expect(sut.patch == 3)
    }

    @Test func initializerWithReleaseCandidate() throws {
        // Given
        let sut = AppVersion("1.2.3-rc")

        // Then
        #expect(sut.major == 1)
        #expect(sut.minor == 2)
        #expect(sut.patch == 3)
    }

    @Test func comparableMajor() throws {
        // Given
        let sut = AppVersion("1.2.0")

        // Then
        #expect(sut < AppVersion("2.0.0"))
        #expect(sut != AppVersion("2.0.0"))
        #expect(sut > AppVersion("1.1.0"))
        #expect(sut == AppVersion("1.2.0"))
        #expect(sut < AppVersion("1.2.1"))
    }

    @Test func comparableMinor() throws {
        // Given
        let sut = AppVersion("1.2.0")

        // Then
        #expect(sut < AppVersion("1.3.0"))
        #expect(sut > AppVersion("1.1.0"))
        #expect(sut == AppVersion("1.2.0"))
        #expect(sut != AppVersion("1.2.1"))
    }

    @Test func comparablePath() throws {
        // Given
        let sut = AppVersion("1.2.3")

        // Then
        #expect(sut < AppVersion("1.2.4"))
        #expect(sut > AppVersion("1.2.2"))
        #expect(sut == AppVersion("1.2.3"))
        #expect(sut != AppVersion("1.2.1"))
    }

    @Test func initWithComponents() throws {
        // Given
        let sut = AppVersion(major: 1, minor: 2, patch: 3)

        // Then
        #expect(sut.major == 1)
        #expect(sut.minor == 2)
        #expect(sut.patch == 3)
    }

    @Test func debugDescription() throws {
        // Given
        let sut = AppVersion(major: 1, minor: 2, patch: 3)

        // Then
        #expect(sut.debugDescription == "1.2.3")
    }

    @Test func initWithEmptyString() throws {
        // Given
        let sut = AppVersion("")

        // Then
        #expect(sut.debugDescription == "0.0.0")
    }

    @Test func initWithMalformedVersionString() throws {
        // Given
        let sut = AppVersion("-")

        // Then
        #expect(sut.debugDescription == "0.0.0")
    }
}
