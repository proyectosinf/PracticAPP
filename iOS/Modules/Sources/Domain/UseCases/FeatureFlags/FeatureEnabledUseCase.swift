import Foundation

// sourcery: AutoMockable
public protocol FeatureEnabledUseCase {
    func invoke(_ name: String) async throws -> Bool
}

public final class FeatureEnabledUseCaseImpl: FeatureEnabledUseCase {
    private let appSettingsRepository: AppSettingsRepository

    public init(appSettingsRepository: AppSettingsRepository) {
        self.appSettingsRepository = appSettingsRepository
    }

    public func invoke(_ name: String) async throws -> Bool {
        let features = try await appSettingsRepository.features()
        guard let feature = features.first(where: { $0.name == name }) else { return false }
        guard feature.isActive ?? false else { return false }
        let currentVersion = appSettingsRepository.currentVersion
        if let fromVersion = feature.fromVersion, currentVersion < fromVersion { return false }
        if let toVersion = feature.toVersion, currentVersion > toVersion { return false }
        return true
    }
}
