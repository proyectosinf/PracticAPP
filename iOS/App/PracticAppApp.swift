import AppCommon
import AppNavigation
import Data
import Factory
import SwiftUI

@main
struct PracticApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            AppNavigation()
                .onViewDidLoad {
                    #if DEBUG || ADHOC
                    registerDebugDependencies()
                    #endif
                }
        }
    }

    #if DEBUG || ADHOC
    func registerDebugDependencies() {
        Container.shared.environment.register { .development }
        Container.shared.loggerConfig.register { .init(printLevel: .debug) }
    }
    #endif
}
