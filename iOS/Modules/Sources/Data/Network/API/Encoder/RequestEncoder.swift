import Foundation

public protocol RequestEncoder: Sendable {
    var contentType: String { get }
    func encode<E: Encodable>(_ value: E) throws -> Data
}
