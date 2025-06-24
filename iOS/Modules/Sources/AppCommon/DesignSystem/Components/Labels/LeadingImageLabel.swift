import SwiftUI

public struct LeadingImageLabel: View {
    let title: String
    let systemImage: String
    let spacing: CGFloat?

    public init(title: String, systemImage: String, spacing: CGFloat? = nil) {
        self.title = title
        self.systemImage = systemImage
        self.spacing = spacing
    }

    public var body: some View {
        Label(title, systemImage: systemImage).labelStyle(LeadingImageLabelStyle(spacing: spacing))
    }
}

public struct LeadingImageLabelStyle: LabelStyle {
    let spacing: CGFloat?
    init(spacing: CGFloat? = nil) {
        self.spacing = spacing
    }

    public func makeBody(configuration: Configuration) -> some View {
        HStack(spacing: spacing) {
            configuration.icon
            configuration.title
        }
    }
}

public extension LabelStyle where Self == LeadingImageLabelStyle {
    static var button: Self { LeadingImageLabelStyle() }
    static var leadingImage: Self { LeadingImageLabelStyle() }
    static func leadingImage(spacing: CGFloat?) -> Self { LeadingImageLabelStyle(spacing: spacing) }
}

public extension Button where Label == LeadingImageLabel {
    @MainActor init(_ title: String, systemImage: String, spacing: CGFloat? = nil, action: @escaping () -> Void) {
        self.init(
            action: action,
            label: { LeadingImageLabel(title: title, systemImage: systemImage, spacing: spacing) }
        )
    }
}

#Preview {
    VStack(alignment: .leading) {
        LeadingImageLabel(title: "0 Spacing", systemImage: "clock", spacing: 0)
        LeadingImageLabel(title: "2 Spacing", systemImage: "clock", spacing: 2)
        LeadingImageLabel(title: "4 Spacing", systemImage: "clock", spacing: 4)
        LeadingImageLabel(title: "6 Spacing", systemImage: "clock", spacing: 6)
        LeadingImageLabel(title: "- Spacing", systemImage: "clock")
    }
}
