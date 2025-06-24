import Combine
import Foundation

@propertyWrapper struct UserDefaultEncoded<T: Codable> {
    let key: String
    private let subject: CurrentValueSubject<T?, Never>

    var projectedValue: AnyPublisher<T?, Never> {
        subject.eraseToAnyPublisher()
    }

    init(_ key: String) {
        self.key = key
        subject = .init(UserDefaults.standard.decoded(key: key))
    }

    var wrappedValue: T? {
        get { UserDefaults.standard.decoded(key: key) }
        set {
            UserDefaults.standard.encoded(key: key, value: newValue)
            subject.send(newValue)
        }
    }
}

extension UserDefaults {
    func decoded<T: Decodable>(key: String) -> T? {
        guard let jsonString = string(forKey: key) else { return nil }
        guard let jsonData = jsonString.data(using: .utf8) else { return nil }
        guard let value = try? JSONDecoder().decode(T.self, from: jsonData) else { return nil }
        return value
    }

    func encoded(key: String, value: some Encodable) {
        let encoder = JSONEncoder()
        encoder.outputFormatting = [.prettyPrinted, .sortedKeys]
        guard let jsonData = try? encoder.encode(value) else { return }
        let jsonString = String(bytes: jsonData, encoding: .utf8)
        set(jsonString, forKey: key)
    }
}

@propertyWrapper struct UserDefaultBool {
    let key: LocalStorageKey
    let defaultValue: Bool

    init(_ key: LocalStorageKey, _ defaultValue: Bool) {
        self.key = key
        self.defaultValue = defaultValue
    }

    var wrappedValue: Bool {
        get { (UserDefaults.standard.object(forKey: key.rawValue) as? Bool) ?? defaultValue }
        set { UserDefaults.standard.set(newValue, forKey: key.rawValue) }
    }
}

@propertyWrapper struct UserDefault<T> {
    let key: LocalStorageKey

    init(_ key: LocalStorageKey) {
        self.key = key
    }

    var wrappedValue: T? {
        get { UserDefaults.standard.object(forKey: key.rawValue) as? T }
        set { UserDefaults.standard.set(newValue, forKey: key.rawValue) }
    }
}
