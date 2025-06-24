import Foundation

public extension Int {
    var toString: String { "\(self)" }
}

public struct IdentifiableInt: Identifiable {
    let value: Int
    public var id: Int { value }
}
