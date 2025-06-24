import SwiftUI

private struct ScrollOffsetPreferenceKey: PreferenceKey {
    static let defaultValue: CGPoint = .zero
    static func reduce(value: inout CGPoint, nextValue: () -> CGPoint) {}
}

public struct ScrollViewOffset<Content: View>: View {
    let axes: Axis.Set
    let showsIndicators: Bool
    let offsetChanged: @Sendable (CGPoint) -> Void
    let content: () -> Content

    public init(
        axes: Axis.Set = .vertical,
        showsIndicators: Bool = true,
        offsetChanged: @Sendable @escaping (CGPoint) -> Void = { _ in },
        @ViewBuilder content: @escaping () -> Content
    ) {
        self.axes = axes
        self.showsIndicators = showsIndicators
        self.offsetChanged = offsetChanged
        self.content = content
    }

    public var body: some View {
        ScrollView(axes, showsIndicators: showsIndicators) {
            content()
                .background(GeometryReader { geometry in
                    Color.clear.preference(
                        key: ScrollOffsetPreferenceKey.self,
                        value: geometry.frame(in: .named("scrollView")).origin
                    )
                })
                .onPreferenceChange(ScrollOffsetPreferenceKey.self, perform: offsetChanged)
        }
        .coordinateSpace(name: "scrollView")
    }
}
