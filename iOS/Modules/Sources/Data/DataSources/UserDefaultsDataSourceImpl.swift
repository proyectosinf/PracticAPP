import Foundation

// sourcery: AutoMockable
protocol UserDefaultsDataSource: AnyObject {
    var lastInstalledVersion: String? { get set }
}

final class UserDefaultsDataSourceImpl: UserDefaultsDataSource {
    @UserDefaultEncoded(.lastInstalledVersion) var lastInstalledVersion: String?
}

enum LocalStorageKey: String {
    case lastInstalledVersion
}

extension UserDefaultEncoded {
    init(_ key: LocalStorageKey) {
        self.init(key.rawValue)
    }
}
