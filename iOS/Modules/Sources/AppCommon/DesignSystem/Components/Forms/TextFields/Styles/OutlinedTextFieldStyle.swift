import SwiftUI
@_spi(Advanced) import SwiftUIIntrospect

public struct OutlinedTextFieldStyle: @preconcurrency TextFieldStyle {
    public enum Style: CaseIterable, Hashable { case gray, white }

    let title: String?
    let placeholder: String?
    let error: String?
    let footer: String?
    let disabled: Bool
    let accessoryView: TitleTextFieldStyle.AccessoryView?
    let style: Style

    public init(
        title: String? = nil,
        placeholder: String?,
        error: String? = nil,
        footer: String? = nil,
        accessoryView: TitleTextFieldStyle.AccessoryView? = nil,
        disabled: Bool = false,
        style: Style = .gray
    ) {
        self.title = title
        self.placeholder = placeholder
        self.error = error
        self.footer = footer
        self.disabled = disabled
        self.accessoryView = accessoryView
        self.style = style
    }

    // swiftlint:disable:next identifier_name
    @MainActor public func _body(configuration: TextField<Self._Label>) -> some View {
        VStack(alignment: .leading, spacing: .spacingXXS) {
            VStack(alignment: .leading, spacing: .spacingXXS) {
                if let title {
                    Text(title)
                        .font(.subheadline)
                        .foregroundStyle((error?.isEmpty ?? true) ? style.titleColor : Color.dsError)
                }

                HStack {
                    configuration
                        .foregroundStyle(style.textColor)

                    if let accessoryView {
                        accessory(accessoryView)
                            .foregroundStyle(style.accessoryColor)
                    }
                }
                .frame(minHeight: 44)
                .padding(.horizontal, .spacingS)
                .overlay {
                    RoundedRectangle(cornerRadius: 10, style: .continuous)
                        .stroke(style.strokeColor, lineWidth: 1)
                }
                .listRowBackground(Color.clear)
                .introspect(.textField, on: .iOS(.v16...)) {
                    $0.attributedPlaceholder = NSAttributedString(
                        string: placeholder ?? "",
                        attributes: [NSAttributedString.Key.foregroundColor: UIColor(style.placeholderColor)]
                    )
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
                    .foregroundStyle(style.placeholderColor)
            }
        }
        .opacity(disabled ? 0.5 : 1)
        .colorScheme(style == .gray ? .light : .dark)
    }

    @MainActor @ViewBuilder
    func accessory(_ accessory: TitleTextFieldStyle.AccessoryView) -> some View {
        switch accessory {
        case let .clear(text):
            Button(
                action: { text.wrappedValue = "" },
                label: { Image(systemName: "xmark.circle.fill") }
            )
            .buttonStyle(BorderlessButtonStyle())
            .isHidden(text.wrappedValue.isEmpty, remove: false)
        case let .systemImage(systemName):
            Image(systemName: systemName)
        case let .button(systemImage, action):
            Button(
                action: action,
                label: { Image(systemName: systemImage) }
            )
            .buttonStyle(BorderlessButtonStyle())
        }
    }
}

extension OutlinedTextFieldStyle.Style {
    var titleColor: Color {
        switch self {
        case .gray: Color.dsOnSurface
        case .white: Color.white
        }
    }

    var strokeColor: Color {
        switch self {
        case .gray: Color.dsOutline
        case .white: Color.white
        }
    }

    var textColor: Color {
        switch self {
        case .gray: Color.dsOnSurface
        case .white: Color.white
        }
    }

    var placeholderColor: Color {
        switch self {
        case .gray: Color.dsOnSurfaceSecondary
        case .white: Color.white.opacity(0.6)
        }
    }

    var accessoryColor: Color {
        switch self {
        case .gray: Color.dsOnSurfaceSecondary
        case .white: Color.white
        }
    }
}

public extension TextFieldStyle where Self == OutlinedTextFieldStyle {
    static func outlined(
        title: String? = nil,
        placeholder: String?,
        error: String? = nil,
        footer: String? = nil,
        accessoryView: TitleTextFieldStyle.AccessoryView? = nil,
        disabled: Bool = false,
        style: Self.Style = .gray
    ) -> Self {
        .init(
            title: title,
            placeholder: placeholder,
            error: error,
            footer: footer,
            accessoryView: accessoryView,
            disabled: disabled,
            style: style
        )
    }
}

extension OutlinedTextFieldStyle {
    struct Preview: View {
        @State var username: String = ""
        let style: OutlinedTextFieldStyle.Style

        var body: some View {
            VStack(spacing: .spacingS) {
                TextField("Username", text: $username)
                    .textFieldStyle(
                        .outlined(
                            placeholder: "Username",
                            style: style
                        )
                    )
                TextField("Username", text: $username)
                    .textFieldStyle(
                        .outlined(
                            title: "Username",
                            placeholder: "Mandatory",
                            accessoryView: .clear($username),
                            style: style
                        )
                    )
                TextField("Username", text: $username)
                    .textFieldStyle(
                        .outlined(
                            title: "Username",
                            placeholder: "Mandatory",
                            footer: "Debe tener al menos 8 caracteres.",
                            accessoryView: .button(systemImage: "magnifyingglass", action: {}),
                            style: style
                        )
                    )
                TextField("Username", text: $username)
                    .textFieldStyle(
                        .outlined(
                            title: "Username",
                            placeholder: "Mandatory",
                            error: "Mandatory",
                            style: style
                        )
                    )
                TextField("Username", text: $username)
                    .textFieldStyle(
                        .outlined(
                            title: "Username",
                            placeholder: "Mandatory",
                            error: "Mandatory",
                            footer: "Debe tener al menos 8 caracteres.",
                            style: style
                        )
                    )
            }
        }
    }
}
