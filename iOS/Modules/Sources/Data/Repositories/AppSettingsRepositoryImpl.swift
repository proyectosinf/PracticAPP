import Domain
import Factory
import Foundation

public final class AppSettingsRepositoryImpl: AppSettingsRepository {
    public var currentVersion: AppVersion { appInfoDataSource.currentVersion.toAppVersion }
    public private(set) var updatingVersion: AppVersion?

    private let appInfoDataSource: AppInfoDataSource
    private let remoteConfigDataSource: RemoteConfigDataSource
    private let userDefaultsDataSource: UserDefaultsDataSource

    public init(container: Container = .shared) {
        appInfoDataSource = container.appInfoDataSource()
        remoteConfigDataSource = container.firebaseRemoteConfigDataSource()
        userDefaultsDataSource = container.userDefaultsDataSource()
        let currentVersion = appInfoDataSource.currentVersion
        if let updatingVersion = userDefaultsDataSource.lastInstalledVersion?.toAppVersion,
           updatingVersion < currentVersion.toAppVersion {
            self.updatingVersion = updatingVersion
        }
        userDefaultsDataSource.lastInstalledVersion = currentVersion
    }

    public func availableAppUpdate() async -> AvailableAppUpdate? {
        await remoteConfigDataSource.featureFlags()?.update?.toDomain
    }

    public func features() async throws -> [FeatureFlag] {
        try await normalizeError {
            await remoteConfigDataSource.featureFlags()?.features?.map(\.toDomain) ?? []
        }
    }
}
