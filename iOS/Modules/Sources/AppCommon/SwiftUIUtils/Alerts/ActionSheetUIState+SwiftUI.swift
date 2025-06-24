import FoundationUtils
import SwiftUI

struct ActionSheetModifier: ViewModifier {
    @Binding var actionSheet: ActionSheetUIState?

    func body(content: Content) -> some View {
        if let actionSheet {
            if let message = actionSheet.message {
                content
                    .confirmationDialog(
                        actionSheet.title,
                        isPresented: _actionSheet.toBool(),
                        titleVisibility: .visible,
                        actions: {
                            ForEach(actionSheet.actions) { action in
                                Button(action.title, role: action.buttonType.toSwiftUI, action: action.action)
                            }
                        },
                        message: { Text(message) }
                    )
            } else {
                content
                    .confirmationDialog(
                        actionSheet.title,
                        isPresented: _actionSheet.toBool(),
                        titleVisibility: .visible,
                        actions: {
                            ForEach(actionSheet.actions) { action in
                                Button(action.title, role: action.buttonType.toSwiftUI, action: action.action)
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
    func actionSheet(model actionSheet: Binding<ActionSheetUIState?>) -> some View {
        modifier(ActionSheetModifier(actionSheet: actionSheet))
    }
}

#if DEBUG
#Preview {
    @Previewable @State var actionSheet: ActionSheetUIState?

    VStack(spacing: .spacingS) {
        Button("Show action sheet") {
            actionSheet = .init(
                title: "Title",
                actions: [.default(title: "Option 1"), .default(title: "Option 2"), .cancel]
            )
        }

        Button("Show action sheet with message") {
            actionSheet = .init(
                title: "Title",
                message: "Message",
                actions: [.default(title: "Option 1"), .default(title: "Option 2"), .cancel]
            )
        }
    }
    .actionSheet(model: $actionSheet)
}
#endif
