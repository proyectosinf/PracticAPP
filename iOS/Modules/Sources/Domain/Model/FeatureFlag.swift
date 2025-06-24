import Foundation

public struct FeatureFlag: Sendable {
    public let name: String
    public let fromVersion: AppVersion?
    public let toVersion: AppVersion?
    public let isActive: Bool?

    public init(name: String, fromVersion: AppVersion?, toVersion: AppVersion?, isActive: Bool?) {
        self.name = name
        self.fromVersion = fromVersion
        self.toVersion = toVersion
        self.isActive = isActive
    }
}
