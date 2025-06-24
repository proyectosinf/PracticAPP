import SwiftUI

public struct SecondaryButtonStyle: ButtonStyle {
    public enum Size { case small, medium, big }

    @Environment(\.isEnabled) private var isEnabled

    let size: Size
    let isFullWidth: Bool

    init(size: Size = .big, isFullWidth: Bool = true) {
        self.size = size
        self.isFullWidth = isFullWidth
    }

    public func makeBody(configuration: Configuration) -> some View {
        configuration
            .label
            .font(size.font)
            .multilineTextAlignment(.center)
            .foregroundStyle(foregroundColor(isPressed: configuration.isPressed))
            .padding(.horizontal, 16)
            .frame(minHeight: size.height)
            .frame(maxWidth: isFullWidth ? .infinity : nil)
            .background(backgroundColor(isPressed: configuration.isPressed))
            .clipShape(RoundedRectangle(cornerRadius: 12.0))
    }

    func foregroundColor(isPressed: Bool) -> Color {
        guard isEnabled else { return Color.dsOnBackgroundVariant2 }
        return isPressed ? Color.dsPrimaryVariant : Color.dsOnPrimaryContainer
    }

    func backgroundColor(isPressed: Bool) -> Color {
        guard isEnabled else { return Color.dsBackgroundVariant }
        return isPressed ? Color.dsPrimaryContainer : Color.dsPrimaryContainer
    }
}

extension SecondaryButtonStyle.Size {
    var height: CGFloat {
        switch self {
        case .small: 28
        case .medium: 38
        case .big: 54
        }
    }

    var font: Font {
        switch self {
        case .small: .subheadline
        case .medium: .body
        case .big: .body
        }
    }
}

public extension ButtonStyle where Self == SecondaryButtonStyle {
    static var secondary: SecondaryButtonStyle { SecondaryButtonStyle() }
    static var secondaryMedium: SecondaryButtonStyle { SecondaryButtonStyle(size: .medium) }
    static var secondarySmall: SecondaryButtonStyle { SecondaryButtonStyle(size: .small) }
    static func secondary(size: SecondaryButtonStyle.Size, isFullWidth: Bool = true) -> SecondaryButtonStyle {
        SecondaryButtonStyle(size: size, isFullWidth: isFullWidth)
    }
}

struct SecondaryButtonsPreview: View {
    var body: some View {
        VStack {
            PreviewButtons(title: "BIG")
                .buttonStyle(.secondary)
            Spacer().frame(height: .spacingL)
            PreviewButtons(title: "MEDIUM")
                .buttonStyle(.secondaryMedium)
            Spacer().frame(height: .spacingL)
            PreviewButtons(title: "SMALL")
                .buttonStyle(.secondarySmall)
        }
        .padding()
    }
}

#if DEBUG
#Preview { SecondaryButtonsPreview() }
#endif
