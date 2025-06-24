import FoundationUtils
import SwiftUI

public struct TextFieldInput: View {
    public enum AccessoryView {
        case clear
        case systemImage(String)
        case button(systemImage: String, action: () -> Void)
    }

    public enum Style: Hashable { case form, outlined(OutlinedTextFieldStyle.Style) }

    let title: String?
    @Binding var state: InputState<String>
    let placeholder: String?
    let footer: String?
    let disabled: Bool
    let style: Style
    let isSecure: Bool
    let accessoryView: AccessoryView?

    @State private var showPassword = false

    public init(
        title: String? = nil,
        state: Binding<InputState<String>>,
        placeholder: String? = nil,
        footer: String? = nil,
        isSecure: Bool,
        style: Style = .form,
        disabled: Bool = false
    ) {
        self.title = title
        _state = state
        self.placeholder = placeholder
        self.footer = footer
        self.disabled = disabled
        self.style = style
        self.isSecure = isSecure
        accessoryView = nil
    }

    public init(
        title: String? = nil,
        state: Binding<InputState<String>>,
        placeholder: String? = nil,
        footer: String? = nil,
        accessoryView: AccessoryView? = nil,
        style: Style = .form,
        disabled: Bool = false
    ) {
        self.title = title
        _state = state
        self.placeholder = placeholder
        self.footer = footer
        self.disabled = disabled
        self.style = style
        isSecure = false
        self.accessoryView = accessoryView
    }

    public var body: some View {
        VStack {
            if !isSecure || showPassword {
                TextField(placeholder ?? title ?? "", text: $state.value.defaultValue(""))
            } else {
                SecureField(placeholder ?? title ?? "", text: $state.value.defaultValue(""))
            }
        }
        .modifier {
            switch style {
            case .form:
                $0.textFieldStyle(
                    .with(
                        title: title,
                        error: state.error,
                        footer: footer,
                        accessoryView: accessory,
                        disabled: disabled
                    )
                )
            case let .outlined(style):
                $0.textFieldStyle(
                    .outlined(
                        title: title,
                        placeholder: placeholder,
                        error: state.error,
                        footer: footer,
                        accessoryView: accessory,
                        disabled: disabled,
                        style: style
                    )
                )
            }
        }
    }

    var accessory: TitleTextFieldStyle.AccessoryView? {
        if isSecure {
            .button(
                systemImage: showPassword ? "eye.slash" : "eye",
                action: { showPassword.toggle() }
            )
        } else {
            switch accessoryView {
            case .clear:
                .clear($state.value.defaultValue(""))
            case let .button(systemImage, action):
                .button(systemImage: systemImage, action: action)
            case let .systemImage(systemImage):
                .systemImage(systemImage)
            case .none:
                nil
            }
        }
    }
}

extension TextFieldInput {
    struct Preview: View {
        @State var style: TextFieldInput.Style = .form
        @State var name: InputState<String> = .init(value: "")
        @State var surname: InputState<String> = .init(value: "")
        @State var password: InputState<String> = .init(value: "")
        @State var repeatPassword: InputState<String> = .init(value: "", error: "Las contraseñas no coinciden")
        @State var email: InputState<String> = .init(value: "dev@mobivery.com")

        var body: some View {
            Group {
                switch style {
                case .form:
                    Form { textfields }
                case let .outlined(style):
                    VStack(spacing: .spacingS) {
                        textfields
                        Spacer()
                    }
                    .padding()
                    .background(style == .white ? Color(hex: 0x5456FF) : Color.white)
                }
            }
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .principal) {
                    Picker("", selection: $style) {
                        Text("Form").tag(TextFieldInput.Style.form)
                        Text("Outlined Gray").tag(TextFieldInput.Style.outlined(.gray))
                        Text("Outlined White").tag(TextFieldInput.Style.outlined(.white))
                    }
                    .pickerStyle(.segmented)
                }
            }
        }

        var textfields: some View {
            Group {
                TextFieldInput(state: $name, placeholder: "Name", style: style)

                TextFieldInput(title: "Surname", state: $surname, placeholder: "Mandatory", style: style)

                TextFieldInput(
                    title: "Password",
                    state: $password,
                    placeholder: "Mandatory",
                    footer: "Debe tener al menos 8 caracteres.",
                    isSecure: true,
                    style: style
                )

                TextFieldInput(
                    title: "Repeat password",
                    state: $repeatPassword,
                    placeholder: "Mandatory",
                    isSecure: true,
                    style: style
                )

                TextFieldInput(
                    title: "Email",
                    state: $email,
                    placeholder: "Mandatory",
                    accessoryView: .clear,
                    style: style
                )
            }
        }
    }
}

#if DEBUG
#Preview { NavigationStack { TextFieldInput.Preview() } }
#endif
