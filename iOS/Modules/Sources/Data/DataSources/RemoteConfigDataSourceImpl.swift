import Domain
import Factory
import FirebaseRemoteConfig
import Foundation

// sourcery: AutoMockable
protocol RemoteConfigDataSource {
    var fetched: Bool { get }
    var featureFlags: InfoRemoteConfig? { get }
    func featureFlags() async -> InfoRemoteConfig?
}

final class RemoteConfigDataSourceImpl: RemoteConfigDataSource {
    let remoteConfig: RemoteConfig
    var featureFlags: InfoRemoteConfig? { fetchedFeatureFlags() }
    var fetched: Bool = false
    private let container: Container
    private var environment: APIEnvironment { container.environment() }
    private let appInfoDataSource: AppInfoDataSource

    init(container: Container = .shared) {
        self.container = container
        appInfoDataSource = container.appInfoDataSource()
        let remoteConfig = RemoteConfig.remoteConfig()
        let settings = RemoteConfigSettings()
        settings.fetchTimeout = 2
        remoteConfig.configSettings = settings
        self.remoteConfig = remoteConfig
        remoteConfig.addOnConfigUpdateListener { [weak self] _, error in
            guard error == nil else { return }
            self?.remoteConfig.activate()
        }
    }

    private func fetch() async {
        guard !fetched else { return }
        do {
            let status = try await remoteConfig.fetchAndActivate()
            guard status != .error else { return }
            fetched = true
        } catch {
            fetched = false
        }
    }

    private func fetchedFeatureFlags() -> InfoRemoteConfig? {
        let key = environment == .production ? "features_ios" : "features_ios_pre"
        let config = remoteConfig.configValue(forKey: key)
        return try? config.decoded(asType: InfoRemoteConfig.self)
    }

    func featureFlags() async -> InfoRemoteConfig? {
        await fetch()
        return fetchedFeatureFlags()
    }
}
