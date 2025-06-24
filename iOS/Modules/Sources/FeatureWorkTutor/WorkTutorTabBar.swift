import AppCommon
import SwiftUI
import FeatureCompany
import Factory

public struct WorkTutorTabBar: View {
    private let userSession = Container.shared.userSession()
    public init() {}
    public var body: some View {
        if userSession.currentUser?.companyId == nil {
            RegisterCompanyNavigation(nil)
        } else {
            TabView {
                HomeCompanyNavigation(nil)
                    .tabItem {
                        Label(Strings.general.company_company_tab_text, systemImage: "building.2")
                    }
                WorkTutorOffersNavigation(nil)
                    .tabItem {
                        Label(Strings.general.offer_offer_tab_text, systemImage: "briefcase")
                    }
            }
            .tabBar(color: .dsOnPrimary, scheme: .light, visibility: .visible)
        }
    }
}
#if DEBUG
#Preview("Root") {
   WorkTutorTabBar()
}
#endif
