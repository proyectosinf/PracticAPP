import Foundation

public protocol RawRepresentableDefaultValue: Decodable & RawRepresentable where RawValue: Decodable {
    static var `default`: Self { get }
}

public extension RawRepresentableDefaultValue {
    init(from decoder: Decoder) throws {
        let rawValue = try decoder.singleValueContainer().decode(RawValue.self)
        if let type = Self(rawValue: rawValue) {
            self = type
        } else {
            #if DEBUG
            fatalError("⚠️⚠️⚠️⚠️ Add new value \(rawValue) to \(Self.self) ⚠️⚠️⚠️⚠️")
            #else
            self = Self.default
            #endif
        }
    }
}
