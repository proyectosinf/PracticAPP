@testable import Domain
import Mocks
import Testing

struct UpdateAppUseCaseTests {
    private let appSettingsRepositoryMock: AppSettingsRepositoryMock = .init()

    @Test func returnsNilWhenNoUpdateIsAvailable() async throws {
        // Given
        let sut = UpdateAppUseCaseImpl(appSettingsRepository: appSettingsRepositoryMock)
        appSettingsRepositoryMock.availableAppUpdateAvailableAppUpdateReturnValue = nil
        appSettingsRepositoryMock.currentVersion = .init("2.5.0")

        // When
        let result = try await sut.invoke()

        // Then
        #expect(result == nil)
    }

    @Test func returnsNilWhenUpdateToVersionIsLowerThanCurrent() async throws {
        // Given
        let sut = UpdateAppUseCaseImpl(appSettingsRepository: appSettingsRepositoryMock)
        appSettingsRepositoryMock.currentVersion = .init("2.0.0")
        appSettingsRepositoryMock.availableAppUpdateAvailableAppUpdateReturnValue = .init(
            fromVersion: .init("0.0.0"),
            toVersion: .init("1.9.0"),
            force: true,
            url: nil
        )

        // When
        let result = try await sut.invoke()

        // Then
        #expect(result == nil)
    }

    @Test func returnsUpdateWhenCurrentVersionIsWithinUpdateRange() async throws {
        // Given
        let sut = UpdateAppUseCaseImpl(appSettingsRepository: appSettingsRepositoryMock)
        appSettingsRepositoryMock.currentVersion = .init("2.0.0")
        appSettingsRepositoryMock.availableAppUpdateAvailableAppUpdateReturnValue = .init(
            fromVersion: .init("0.0.0"),
            toVersion: .init("2.0.0"),
            force: true,
            url: "https://example.com"
        )

        // When
        let result = try #require(await sut.invoke())

        // Then
        #expect(result.force)
        #expect(result.url == "https://example.com")
    }

    @Test func returnsNilWhenCurrentVersionExceedsUpdateRange() async throws {
        // Given
        let sut = UpdateAppUseCaseImpl(appSettingsRepository: appSettingsRepositoryMock)
        appSettingsRepositoryMock.currentVersion = .init("2.0.1")
        appSettingsRepositoryMock.availableAppUpdateAvailableAppUpdateReturnValue = .init(
            fromVersion: .init("0.0.0"),
            toVersion: .init("2.0.0"),
            force: true,
            url: "https://example.com"
        )

        // When
        let result = try await sut.invoke()

        // Then
        #expect(result == nil)
    }

    @Test func returnsUpdateWhenUpdateIsAvailableButUrlIsMissing() async throws {
        // Given
        let sut = UpdateAppUseCaseImpl(appSettingsRepository: appSettingsRepositoryMock)
        appSettingsRepositoryMock.currentVersion = .init("2.0.0")
        appSettingsRepositoryMock.availableAppUpdateAvailableAppUpdateReturnValue = .init(
            fromVersion: .init("0.0.0"),
            toVersion: .init("2.0.0"),
            force: true,
            url: nil
        )

        // When
        let result = try #require(await sut.invoke())

        // Then
        #expect(result.force)
        #expect(result.url == nil)
    }

    @Test func returnsOptionalUpdateWhenUpdateIsAvailableButNotForced() async throws {
        // Given
        let sut = UpdateAppUseCaseImpl(appSettingsRepository: appSettingsRepositoryMock)
        appSettingsRepositoryMock.currentVersion = .init("2.0.0")
        appSettingsRepositoryMock.availableAppUpdateAvailableAppUpdateReturnValue = .init(
            fromVersion: .init("0.0.0"),
            toVersion: .init("2.0.0"),
            force: false,
            url: "https://example.com"
        )

        // When
        let result = try #require(await sut.invoke())

        // Then
        #expect(!result.force)
        #expect(result.url == "https://example.com")
    }

    @Test func returnsUpdateWhenCurrentVersionEqualsFromVersion() async throws {
        // Given
        let sut = UpdateAppUseCaseImpl(appSettingsRepository: appSettingsRepositoryMock)
        appSettingsRepositoryMock.currentVersion = .init("1.0.0")
        appSettingsRepositoryMock.availableAppUpdateAvailableAppUpdateReturnValue = .init(
            fromVersion: .init("1.0.0"),
            toVersion: .init("2.0.0"),
            force: true,
            url: "https://example.com"
        )

        // When
        let result = try #require(await sut.invoke())

        // Then
        #expect(result != nil)
    }

    @Test func returnsUpdateWhenCurrentVersionEqualsToVersion() async throws {
        // Given
        let sut = UpdateAppUseCaseImpl(appSettingsRepository: appSettingsRepositoryMock)
        appSettingsRepositoryMock.currentVersion = .init("2.0.0")
        appSettingsRepositoryMock.availableAppUpdateAvailableAppUpdateReturnValue = .init(
            fromVersion: .init("1.0.0"),
            toVersion: .init("2.0.0"),
            force: true,
            url: "https://example.com"
        )

        // When
        let result = try #require(await sut.invoke())

        // Then
        #expect(result != nil)
    }
}
