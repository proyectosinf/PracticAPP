import SwiftUI

public extension View {
    func tabBar(
        color: Color,
        scheme: ColorScheme = .light,
        visibility: Visibility = .visible
    ) -> some View {
        toolbarBackground(visibility, for: .tabBar)
            .toolbarBackground(color, for: .tabBar)
            .toolbarColorScheme(scheme, for: .tabBar)
    }
}

#Preview("Purple dark") {
    TabView {
        Group {
            Text("Hola")
                .tabItem { Label("Home", systemImage: "house") }

            Text("Adios")
                .tabItem { Label("Components", systemImage: "heart") }
        }
        .tabBar(color: .dsOnPrimary, scheme: .dark, visibility: .visible)
    }
}
