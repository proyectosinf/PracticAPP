import SwiftUI

public extension View {
    @ViewBuilder
    func listBackgroundColor(_ color: Color) -> some View {
        scrollContentBackground(.hidden)
            .background(color)
    }
}

#if DEBUG
#Preview {
    @Previewable @State var text = ""
    Form { Section { TextField("Username", text: $text) } }
        .listBackgroundColor(.red)
}
#endif
