import SwiftUI

public struct TrailingImageLabel: View {
    let title: String
    let systemImage: String
    let spacing: CGFloat?

    public init(title: String, systemImage: String, spacing: CGFloat? = nil) {
        self.title = title
        self.systemImage = systemImage
        self.spacing = spacing
    }

    public var body: some View {
        Label(title, systemImage: systemImage).labelStyle(TrailingImageLabelStyle(spacing: spacing))
    }
}

public struct TrailingImageLabelStyle: LabelStyle {
    let spacing: CGFloat?
    init(spacing: CGFloat? = nil) {
        self.spacing = spacing
    }

    public func makeBody(configuration: Configuration) -> some View {
        HStack(spacing: spacing) {
            configuration.title
            configuration.icon
        }
    }
}

public extension LabelStyle where Self == TrailingImageLabelStyle {
    static var trailingImage: Self { TrailingImageLabelStyle() }
    static func trailingImage(spacing: CGFloat?) -> Self { TrailingImageLabelStyle(spacing: spacing) }
}

public extension Button where Label == TrailingImageLabel {
    @MainActor init(
        _ title: String,
        trailingSystemImage: String,
        spacing: CGFloat? = nil,
        action: @escaping () -> Void
    ) {
        self.init(
            action: action,
            label: { TrailingImageLabel(title: title, systemImage: trailingSystemImage, spacing: spacing) }
        )
    }
}

#Preview {
    VStack(alignment: .leading) {
        TrailingImageLabel(title: "Spacing 0", systemImage: "clock", spacing: 0)
        TrailingImageLabel(title: "Spacing 2", systemImage: "clock", spacing: 2)
        TrailingImageLabel(title: "Spacing 4", systemImage: "clock", spacing: 4)
        TrailingImageLabel(title: "Spacing 6", systemImage: "clock", spacing: 6)
        TrailingImageLabel(title: "Spacing -", systemImage: "clock")
    }
}
