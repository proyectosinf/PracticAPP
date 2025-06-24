import Foundation
import Security
import FoundationUtils

// sourcery: AutoMockable
protocol KeychainDataSource: Sendable {
    @discardableResult func save(token: APITokenData, service: String, account: String) -> Bool
    func token(service: String, account: String) -> APITokenData?
    func saveFirebaseToken(_ token: String)
    func firebaseToken() -> String?
    func deleteFirebaseToken()
    func isLoggedIn() -> Bool
    @discardableResult func deleteToken(service: String, account: String) -> Bool
}

extension KeychainDataSource {

    func apiToken() -> APITokenData? {
        token(service: "access-token", account: "api")
    }

    @discardableResult func saveAPIToken(_ token: APITokenData) -> Bool {
        save(token: token, service: "access-token", account: "api")
    }

    @discardableResult func deleteAPIToken() -> Bool {
        deleteToken(service: "access-token", account: "api")
    }
}

final class KeychainDataSourceImpl: KeychainDataSource {
    private let firebaseTokenKey = "firebase_token"
    public func saveFirebaseToken(_ token: String) {
        KeychainHelper.save(key: firebaseTokenKey, value: token)
    }

    public func firebaseToken() -> String? {
        KeychainHelper.get(key: firebaseTokenKey)
    }

    public func deleteFirebaseToken() {
        KeychainHelper.delete(key: firebaseTokenKey)
    }

    public func isLoggedIn() -> Bool {
        firebaseToken() != nil
    }
    @discardableResult
    func save(token: APITokenData, service: String, account: String) -> Bool {
        guard let data = try? JSONEncoder().encode(token) else { return false }

        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: account,
            kSecValueData as String: data
        ]

        SecItemDelete(query as CFDictionary) // Delete any existing item before adding new one

        let status = SecItemAdd(query as CFDictionary, nil)
        return status == errSecSuccess
    }

    func token(service: String, account: String) -> APITokenData? {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: account,
            // swiftlint:disable:next force_unwrapping
            kSecReturnData as String: kCFBooleanTrue!,
            kSecMatchLimit as String: kSecMatchLimitOne
        ]

        var dataTypeRef: AnyObject?
        let status = SecItemCopyMatching(query as CFDictionary, &dataTypeRef)

        guard status == errSecSuccess, let data = dataTypeRef as? Data else { return nil }
        return try? JSONDecoder().decode(APITokenData.self, from: data)
    }

    @discardableResult
    func deleteToken(service: String, account: String) -> Bool {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: account
        ]

        let status = SecItemDelete(query as CFDictionary)
        return status == errSecSuccess
    }
}
