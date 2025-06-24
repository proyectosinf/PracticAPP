import Foundation

public extension TimeZone {
    // swiftlint:disable:next force_unwrapping
    static var UTC: TimeZone { TimeZone(identifier: "UTC")! }
}
