import SwiftUI

public struct TitleTextFieldStyle: @preconcurrency TextFieldStyle {
    public enum AccessoryView {
        case clear(Binding<String>)
        case systemImage(String)
        case button(systemImage: String, action: () -> Void)
    }

    let title: String?
    let error: String?
    let footer: String?
    let disabled: Bool
    let accessoryView: AccessoryView?

    public init(
        title: String?,
        error: String? = nil,
        footer: String? = nil,
        accessoryView: AccessoryView? = nil,
        disabled: Bool = false
    ) {
        self.title = title
        self.error = error
        self.footer = footer
        self.disabled = disabled
        self.accessoryView = accessoryView
    }

    // swiftlint:disable:next identifier_name
    @MainActor public func _body(configuration: TextField<Self._Label>) -> some View {
        VStack(alignment: .leading, spacing: 1) {
            HStack(alignment: .center, spacing: .spacingXXS) {
                VStack(alignment: .leading, spacing: 1) {
                    if let title {
                        Text(title)
                            .font(.subheadline)
                            .foregroundStyle((error?.isEmpty ?? true) ? Color.dsOnSurface : Color.dsError)
                    }
                    configuration
                        .frame(minHeight: 22)
                }

                if let accessoryView {
                    accessory(accessoryView)
                        .foregroundStyle(Color.dsOnSurfaceSecondary)
                }
            }

            if let error, !error.isEmpty {
                Text(error)
                    .font(.caption)
                    .foregroundStyle(Color.dsError)
            }
            if let footer, !footer.isEmpty {
                Text(footer)
                    .font(.caption)
                    .foregroundStyle(Color.dsOnSurfaceSecondary)
            }
        }
        .opacity(disabled ? 0.5 : 1)
        .buttonStyle(BorderlessButtonStyle())
    }

    @MainActor @ViewBuilder
    func accessory(_ accessory: AccessoryView) -> some View {
        switch accessory {
        case let .clear(text):
            Button(
                action: { text.wrappedValue = "" },
                label: { Image(systemName: "xmark.circle.fill") }
            )
            .isHidden(text.wrappedValue.isEmpty, remove: false)
        case let .systemImage(systemName):
            Image(systemName: systemName)
        case let .button(systemImage, action):
            Button(
                action: action,
                label: { Image(systemName: systemImage) }
            )
        }
    }
}

extension TextFieldStyle where Self == TitleTextFieldStyle {
    static func with(
        title: String?,
        error: String? = nil,
        footer: String? = nil,
        accessoryView: Self.AccessoryView? = nil,
        disabled: Bool = false
    ) -> Self {
        .init(
            title: title,
            error: error,
            footer: footer,
            accessoryView: accessoryView,
            disabled: disabled
        )
    }
}
