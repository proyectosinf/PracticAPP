import SwiftUI

public extension View {
    func navigationBar(
        color: Color,
        scheme: ColorScheme = .light,
        visibility: Visibility = .visible
    ) -> some View {
        toolbarBackground(visibility, for: .navigationBar)
            .toolbarBackground(color, for: .navigationBar)
            .toolbarColorScheme(scheme, for: .navigationBar)
    }
}

private struct TestPreview: View {
    let title: String
    let color: Color
    let scheme: ColorScheme

    var body: some View {
        NavigationLink(title) {
            Text(title)
                .navigationBar(color: color, scheme: scheme, visibility: .visible)
                .navigationTitle(title)
        }
    }
}

#Preview("Purple dark") {
    NavigationStack {
        List {
            TestPreview(title: "Red Light", color: .red, scheme: .light)
            TestPreview(title: "Orange Light", color: .orange, scheme: .light)
            TestPreview(title: "Indigo Dark", color: .indigo, scheme: .dark)
            TestPreview(title: "Brown Dark", color: .brown, scheme: .dark)
        }
        .listStyle(.plain)
        .navigationTitle("Navigation Bar Colors", displayMode: .large)
        .navigationBar(color: .purple, scheme: .dark, visibility: .visible)
    }
}
