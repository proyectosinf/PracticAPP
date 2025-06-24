import AppCommon
import SwiftUI

public struct StudentTabBar: View {

    public init() {}

    public var body: some View {
        TabView {

            StudentOffersNavigation(nil)
                .tabItem {
                    Label(Strings.general.offer_offer_tab_text, systemImage: "briefcase")
                }

            StudentCandidacyNavigation(nil)
                .tabItem {
                    Label(Strings.general.offer_my_candidacy_text, systemImage: "person.2")
                }
        }
        .tabBar(color: .dsOnPrimary, scheme: .light, visibility: .visible)
    }
}

#if DEBUG
#Preview {
    StudentTabBar()
}
#endif
