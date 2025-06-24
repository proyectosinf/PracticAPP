import FoundationUtils
import SwiftUI

struct TextFieldAlertModifier: ViewModifier {
    @Binding var alert: TextFieldAlertUIState?

    func body(content: Content) -> some View {
        if let alert {
            if let message = alert.message {
                content
                    .alert(
                        alert.title,
                        isPresented: _alert.toBool(),
                        actions: {
                            TextField(alert.placeholder, text: alert.text)
                            ForEach(alert.actions) { action in
                                Button(action.title, role: action.buttonType.toSwiftUI) {
                                    action.action(alert.text.wrappedValue)
                                }
                            }
                        }, message: {
                            Text(message)
                        }
                    )
            } else {
                content
                    .alert(
                        alert.title,
                        isPresented: _alert.toBool(),
                        actions: {
                            TextField(alert.placeholder, text: alert.text)
                            ForEach(alert.actions) { action in
                                Button(action.title, role: action.buttonType.toSwiftUI) {
                                    action.action(alert.text.wrappedValue)
                                }
                            }
                        }
                    )
            }
        } else {
            content
        }
    }
}

public extension View {
    func textFieldAlert(model alert: Binding<TextFieldAlertUIState?>) -> some View {
        modifier(TextFieldAlertModifier(alert: alert))
    }
}

#if DEBUG
#Preview {
    @Previewable @State var alert: TextFieldAlertUIState?
    @Previewable @State var name = ""
    @Previewable @State var surname = ""

    VStack(spacing: .spacingS) {
        Button("Set your name") {
            alert = .init(
                title: "Nombre",
                message: "Escribe tu nombre",
                text: $name,
                placeholder: "Tu nombre",
                actions: [
                    .accept(action: { _ in }),
                    .cancel
                ]
            )
        }

        Text("Nombre: \(name)")

        Button("Set your surname") {
            alert = .init(
                title: "Apellido",
                message: "Escribe tu apellido",
                text: $surname,
                placeholder: "Tu apellido",
                actions: [
                    .accept(action: { _ in }),
                    .cancel
                ]
            )
        }

        Text("Apellido: \(surname)")
    }
    .textFieldAlert(model: $alert)
}
#endif
