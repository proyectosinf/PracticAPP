import AppCommon
import FeatureLogin
import FeatureStudent
import FeatureWorkTutor
import FeatureCompany
import SwiftUI
import Factory

public struct AppNavigation: View {
    @Bindable private var coordinator = Container.shared.appCoordinator()
    public init() {}
    public var body: some View {
        Group {
            switch coordinator.root {
            case .login:
                LoginNavigation(
                    .constant(true),
                    registerScreen: { RegisterScreen() }
                )
            case .tabBar(let role):
                switch role {
                case .student:
                    StudentTabBar()
                case .tutor:
                    WorkTutorTabBar()
                }
            case .registerCompany:
                RegisterCompanyNavigation(nil)
            case .splash:
                SplashScreen()
            }
        }
        .transition(.opacity)
        .environment(\.appCoordinator, coordinator)
        .onViewDidLoad { coordinator.start() }
    }
}
#if DEBUG
#Preview { AppNavigation() }
#endif
