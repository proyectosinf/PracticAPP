import SwiftUI

public struct PrimaryButtonStyle: ButtonStyle {
    public enum Size { case small, medium, big }

    @Environment(\.isEnabled) private var isEnabled

    let size: Size
    let isFullWidth: Bool

    public init(
        size: Size = .big,
        isFullWidth: Bool = true
    ) {
        self.size = size
        self.isFullWidth = isFullWidth
    }

    public func makeBody(configuration: Configuration) -> some View {
        configuration
            .label
            .font(size.font)
            .multilineTextAlignment(.center)
            .foregroundColor(foregroundColor(isPressed: configuration.isPressed))
            .padding(.horizontal, 16)
            .frame(minHeight: size.height)
            .frame(maxWidth: isFullWidth ? .infinity : nil)
            .background(backgroundColor(isPressed: configuration.isPressed))
            .clipShape(RoundedRectangle(cornerRadius: 12.0))
    }

    func foregroundColor(isPressed: Bool) -> Color {
        guard isEnabled else { return Color.dsOnBackgroundVariant }
        return isPressed ? Color.dsOnPrimaryVariant : Color.dsOnPrimary
    }

    func backgroundColor(isPressed: Bool) -> Color {
        guard isEnabled else { return Color.dsBackgroundVariant }
        return isPressed ? .dsPrimaryVariant : .dsPrimary
    }
}

extension PrimaryButtonStyle.Size {
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

public extension ButtonStyle where Self == PrimaryButtonStyle {
    static var primary: PrimaryButtonStyle { PrimaryButtonStyle() }
    static var primaryMedium: PrimaryButtonStyle { PrimaryButtonStyle(size: .medium) }
    static var primarySmall: PrimaryButtonStyle { PrimaryButtonStyle(size: .small) }
    static func primary(
        size: PrimaryButtonStyle.Size = .big,
        isFullWidth: Bool = true
    ) -> PrimaryButtonStyle {
        PrimaryButtonStyle(size: size, isFullWidth: isFullWidth)
    }
}

struct PreviewButtons: View {
    let title: String
    var body: some View {
        VStack {
            HStack {
                Button(title) {}
                Button("DISABLED") {}
                    .disabled(true)
            }

            HStack {
                Button(title, systemImage: "figure.run") {}
                Button("DISABLED", systemImage: "figure.run") {}
                    .disabled(true)
            }

            HStack {
                Button(title, trailingSystemImage: "play.fill") {}
                Button("DISABLED", trailingSystemImage: "play.fill") {}
                    .disabled(true)
            }

            HStack {
                IconButton(systemImage: "figure.run") {}
                IconButton(systemImage: "figure.run") {}
                    .disabled(true)
            }
        }
    }
}

struct PrimaryButtonsPreview: View {
    var body: some View {
        VStack {
            PreviewButtons(title: "BIG")
                .buttonStyle(.primary)
            Spacer().frame(height: .spacingL)
            PreviewButtons(title: "MEDIUM")
                .buttonStyle(.primaryMedium)
            Spacer().frame(height: .spacingL)
            PreviewButtons(title: "SMALL")
                .buttonStyle(.primarySmall)
        }
        .padding()
    }
}

#if DEBUG
#Preview { PrimaryButtonsPreview() }
#endif
