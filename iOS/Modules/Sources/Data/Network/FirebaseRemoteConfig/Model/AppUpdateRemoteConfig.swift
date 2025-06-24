import Domain
import Foundation

struct AppUpdateRemoteConfig: Codable, Hashable, Sendable {
    let isActive: Bool?
    let fromVersion: String?
    let toVersion: String?
    let force: Bool?
    let urlStore: String?
}

extension AppUpdateRemoteConfig {
    var toDomain: AvailableAppUpdate {
        .init(
            fromVersion: fromVersion?.toAppVersion,
            toVersion: toVersion?.toAppVersion,
            force: force ?? false,
            url: urlStore
        )
    }
}
