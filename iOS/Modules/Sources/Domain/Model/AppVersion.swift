import Foundation
import FoundationUtils

public struct AppVersion: Sendable {
    public let major: Int
    public let minor: Int
    public let patch: Int

    public init(major: Int, minor: Int, patch: Int) {
        self.major = major
        self.minor = minor
        self.patch = patch
    }

    public init(_ version: String) {
        let components = version.removeVersionSuffix()?.components(separatedBy: ".")
        major = Int(components?[safe: 0] ?? "") ?? 0
        minor = Int(components?[safe: 1] ?? "") ?? 0
        patch = Int(components?[safe: 2] ?? "") ?? 0
    }
}

private extension String {
    func removeVersionSuffix() -> String? {
        components(separatedBy: "-").first?.emptyToNil
    }
}

extension AppVersion: Comparable {
    public static func < (lhs: AppVersion, rhs: AppVersion) -> Bool {
        if lhs.major < rhs.major {
            return true
        } else if lhs.major > rhs.major {
            return false
        }
        if lhs.minor < rhs.minor {
            return true
        } else if lhs.minor > rhs.minor {
            return false
        }
        return lhs.patch < rhs.patch
    }

    public static func == (lhs: Self, rhs: Self) -> Bool {
        lhs.major == rhs.major && lhs.minor == rhs.minor && lhs.patch == rhs.patch
    }
}

extension AppVersion: CustomDebugStringConvertible {
    public var debugDescription: String { "\(major).\(minor).\(patch)" }
}

public extension String {
    var toAppVersion: AppVersion { .init(self) }
}
