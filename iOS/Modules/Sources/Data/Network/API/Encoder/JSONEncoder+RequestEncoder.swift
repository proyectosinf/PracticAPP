import Foundation

extension RequestEncoder where Self == JSONEncoder {
    static func json(_ encoder: JSONEncoder) -> Self {
        encoder
    }
}

extension JSONEncoder: RequestEncoder {
    public var contentType: String { "application/json" }
}
