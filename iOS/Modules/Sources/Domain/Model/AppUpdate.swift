import Foundation

public struct AppUpdate: Sendable {
    public let force: Bool
    public let url: String?

    public init(force: Bool, url: String?) {
        self.force = force
        self.url = url
    }
}
