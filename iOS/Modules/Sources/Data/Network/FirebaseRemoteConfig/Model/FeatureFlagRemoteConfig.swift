import Domain
import Foundation

struct FeatureFlagRemoteConfig: Codable, Hashable {
    let name: String?
    let isActive: Bool?
    let fromVersion: String?
    let toVersion: String?
}

extension FeatureFlagRemoteConfig {
    var toDomain: FeatureFlag {
        .init(
            name: name ?? "",
            fromVersion: fromVersion?.toAppVersion,
            toVersion: toVersion?.toAppVersion,
            isActive: isActive
        )
    }
}
