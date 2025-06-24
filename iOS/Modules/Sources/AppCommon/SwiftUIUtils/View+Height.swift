import SwiftUI

struct HeightPreferenceKey: PreferenceKey {
    static let defaultValue: CGFloat = 0

    static func reduce(value: inout CGFloat?, nextValue: () -> CGFloat?) {
        guard let nextValue = nextValue() else { return }
        value = nextValue
    }
}

private struct ReadHeightModifier: ViewModifier {
    let height: Binding<CGFloat>

    private var sizeView: some View {
        GeometryReader { geometry in
            Color.clear.preference(
                key: HeightPreferenceKey.self,
                value: geometry.size.height
            )
        }
    }

    func body(content: Content) -> some View {
        content
            .background(sizeView)
            .onPreferenceChange(HeightPreferenceKey.self) { [height] value in
                guard let value else { return }
                height.wrappedValue = value
            }
    }
}

public extension View {
    func readHeight(_ height: Binding<CGFloat>) -> some View {
        modifier(ReadHeightModifier(height: height))
    }
}
