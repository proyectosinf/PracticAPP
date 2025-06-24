import FoundationUtils
import SwiftUI

struct AlertModifier: ViewModifier {
    @Binding var alert: AlertUIState?

    func body(content: Content) -> some View {
        if let alert {
            if let message = alert.message {
                content.alert(alert.title, isPresented: _alert.toBool()) {
                    ForEach(alert.actions) { action in
                        Button(action.title, role: action.buttonType.toSwiftUI, action: action.action)
                    }
                } message: {
                    Text(message)
                }
            } else {
                content.alert(alert.title, isPresented: _alert.toBool()) {
                    ForEach(alert.actions) { action in
                        Button(action.title, role: action.buttonType.toSwiftUI, action: action.action)
                    }
                }
            }
        } else {
            content
        }
    }
}

public extension View {
    func alert(model alert: Binding<AlertUIState?>) -> some View {
        modifier(AlertModifier(alert: alert))
    }
}

#if DEBUG
#Preview {
    @Previewable @State var alert: AlertUIState?

    Button("Show alert") {
        alert = .init(title: "Title", message: "Message", actions: [.accept, .cancel])
    }
    .alert(model: $alert)
}
#endif
