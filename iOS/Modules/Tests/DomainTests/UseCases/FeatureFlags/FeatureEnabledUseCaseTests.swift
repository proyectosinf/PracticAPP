@testable import Domain
import Mocks
import Testing

struct FeatureEnabledUseCaseTests {
    private let appSettingsRepositoryMock: AppSettingsRepositoryMock = .init()

    @Test func returnsTrueWhenFeatureIsEnabledAndWithinVersionRange() async throws {
        // Given
        let sut = FeatureEnabledUseCaseImpl(appSettingsRepository: appSettingsRepositoryMock)
        let featureName = "new_feature"
        appSettingsRepositoryMock.featuresFeatureFlagReturnValue = [
            .init(name: featureName, fromVersion: .init("1.0.0"), toVersion: .init("2.0.0"), isActive: true)
        ]
        appSettingsRepositoryMock.currentVersion = .init("1.5.0")

        // When
        let result = try await sut.invoke(featureName)

        // Then
        #expect(result == true)
    }

    @Test func returnsFalseWhenFeatureIsNotEnabled() async throws {
        // Given
        let sut = FeatureEnabledUseCaseImpl(appSettingsRepository: appSettingsRepositoryMock)
        let featureName = "new_feature"
        appSettingsRepositoryMock.featuresFeatureFlagReturnValue = [
            .init(name: featureName, fromVersion: .init("1.0.0"), toVersion: .init("2.0.0"), isActive: false)
        ]
        appSettingsRepositoryMock.currentVersion = .init("1.5.0")

        // When
        let result = try await sut.invoke(featureName)

        // Then
        #expect(result == false)
    }

    @Test func returnsFalseWhenFeatureIsOutOfVersionRange() async throws {
        // Given
        let sut = FeatureEnabledUseCaseImpl(appSettingsRepository: appSettingsRepositoryMock)
        let featureName = "new_feature"
        appSettingsRepositoryMock.featuresFeatureFlagReturnValue = [
            .init(name: featureName, fromVersion: .init("1.0.0"), toVersion: .init("2.0.0"), isActive: true)
        ]
        appSettingsRepositoryMock.currentVersion = .init("2.5.0")

        // When
        let result = try await sut.invoke(featureName)

        // Then
        #expect(result == false)
    }

    @Test func returnsFalseWhenFeatureListIsEmpty() async throws {
        // Given
        let sut = FeatureEnabledUseCaseImpl(appSettingsRepository: appSettingsRepositoryMock)
        let featureName = "new_feature"
        appSettingsRepositoryMock.featuresFeatureFlagReturnValue = []
        appSettingsRepositoryMock.currentVersion = .init("2.5.0")

        // When
        let result = try await sut.invoke(featureName)

        // Then
        #expect(result == false)
    }
}
