import Foundation

public struct AvailableAppUpdate: Sendable {
    public let fromVersion: AppVersion?
    public let toVersion: AppVersion?
    public let force: Bool
    public let url: String?

    public init(fromVersion: AppVersion?, toVersion: AppVersion?, force: Bool, url: String?) {
        self.fromVersion = fromVersion
        self.toVersion = toVersion
        self.force = force
        self.url = url
    }
}
