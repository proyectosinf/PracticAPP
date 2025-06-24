import SwiftUI

public struct AgreementView: View {
    @Binding private var isAccepted: Bool
    private let text: String
    private let openURLAction: (URL) -> Void

    public init(text: String, isAccepted: Binding<Bool>, openURLAction: @escaping (URL) -> Void) {
        self.text = text
        _isAccepted = .init(projectedValue: isAccepted)
        self.openURLAction = openURLAction
    }

    public var body: some View {
        Toggle(
            isOn: $isAccepted,
            label: {
                Text(LocalizedStringKey(text))
                    .font(.callout)
                    .tint(.dsPrimary)
                    .environment(\.openURL, OpenURLAction { url in
                        openURLAction(url)
                        return .handled
                    })
            }
        )
        .tint(.dsPrimary)
    }
}

#if DEBUG
#Preview {
    @Previewable @State var isAccepted = true
    AgreementView(
        text: "Acepto los [términos y condiciones](url) de esta App",
        isAccepted: $isAccepted
    ) { _ in }
        .padding()
}
#endif
