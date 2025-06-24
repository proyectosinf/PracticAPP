import Foundation

// sourcery: AutoMockable
public protocol AppSettingsRepository: AnyObject {
    var currentVersion: AppVersion { get }
    var updatingVersion: AppVersion? { get }
    func availableAppUpdate() async -> AvailableAppUpdate?
    func features() async throws -> [FeatureFlag]
}
