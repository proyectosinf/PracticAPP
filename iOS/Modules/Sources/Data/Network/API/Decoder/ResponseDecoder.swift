import Foundation

public protocol ResponseDecoder: Sendable {
    func decode<D: Decodable>(_ type: D.Type, from: Data) throws -> D
}
