import Foundation

struct APITokenData: Codable, Hashable, Sendable {
    let accessToken: String
    let refreshToken: String
}
