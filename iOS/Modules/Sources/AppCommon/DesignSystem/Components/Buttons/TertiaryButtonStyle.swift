import SwiftUI

public struct TertiaryButtonStyle: ButtonStyle {
    public enum Size { case small, medium, big }

    @Environment(\.isEnabled) private var isEnabled

    let size: Size

    public init(size: Size = .big) {
        self.size = size
    }

    public func makeBody(configuration: Configuration) -> some View {
        configuration
            .label
            .font(size.font)
            .multilineTextAlignment(.center)
            .foregroundColor(
                isEnabled ? configuration.isPressed ? .dsPrimaryVariant : .dsPrimary : Color.dsOnSurfaceSecondary
            )
            .padding(.horizontal, 16)
            .frame(minHeight: size.height)
    }

    func foregroundColor(isPressed: Bool) -> Color {
        guard isEnabled else { return Color.dsOnSurfaceSecondary }
        return isPressed ? Color.dsPrimaryVariant : Color.dsPrimary
    }
}

extension TertiaryButtonStyle.Size {
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

public extension ButtonStyle where Self == TertiaryButtonStyle {
    static var tertiary: TertiaryButtonStyle { TertiaryButtonStyle() }
    static var tertiaryMedium: TertiaryButtonStyle { TertiaryButtonStyle(size: .medium) }
    static var tertiarySmall: TertiaryButtonStyle { TertiaryButtonStyle(size: .small) }
    static func tertiary(size: TertiaryButtonStyle.Size) -> TertiaryButtonStyle {
        TertiaryButtonStyle(size: size)
    }
}

struct TertiaryButtonsPreview: View {
    var body: some View {
        VStack {
            PreviewButtons(title: "BIG")
                .buttonStyle(.tertiary)
            Spacer().frame(height: .spacingL)
            PreviewButtons(title: "MEDIUM")
                .buttonStyle(.tertiaryMedium)
            Spacer().frame(height: .spacingL)
            PreviewButtons(title: "SMALL")
                .buttonStyle(.tertiarySmall)
        }
        .padding()
    }
}

#if DEBUG
#Preview { TertiaryButtonsPreview() }
#endif
