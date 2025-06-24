import Foundation

// sourcery: AutoMockable
public protocol AppInfoDataSource {
    var currentVersion: String { get }
}

final class AppInfoDataSourceImpl: AppInfoDataSource {
    var currentVersion: String { Bundle.main.appVersion }

    init() {}
}

extension Bundle {
    var appVersion: String { infoDictionary?["CFBundleShortVersionString"] as? String ?? "" }
}
