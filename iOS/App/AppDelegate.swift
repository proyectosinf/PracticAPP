import FirebaseCore
import UIKit

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        #if DEBUG || ADHOC
        FirebaseApp.configureFromInfoPlist("GoogleServicePRE-Info")
        #else
        FirebaseApp.configure()
        #endif
        return true
    }
}
