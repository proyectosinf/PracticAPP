import Foundation

// sourcery: AutoMockable
public protocol UpdateAppUseCase {
    func invoke() async throws -> AppUpdate?
}

public final class UpdateAppUseCaseImpl: UpdateAppUseCase {
    private let appSettingsRepository: AppSettingsRepository

    public init(appSettingsRepository: AppSettingsRepository) {
        self.appSettingsRepository = appSettingsRepository
    }

    public func invoke() async throws -> AppUpdate? {
        let currentVersion = appSettingsRepository.currentVersion
        guard let update = await appSettingsRepository.availableAppUpdate() else { return nil }
        if let fromVersion = update.fromVersion, currentVersion < fromVersion { return nil }
        if let toVersion = update.toVersion, currentVersion > toVersion { return nil }
        return .init(force: update.force, url: update.url)
    }
}
