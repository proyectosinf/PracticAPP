import SwiftUI

/// A view modifier that enables a fitted bottom sheet with dynamic height adjustment.
struct DynamicSheetModifier: ViewModifier {
    /// The height of the bottom sheet, dynamically updated.
    @State private var sheetHeight: CGFloat = 100

    /// The set of presentation detents that define the possible heights of the sheet.
    let detents: Set<PresentationDetent>

    func body(content: Content) -> some View {
        content
            .readHeight($sheetHeight)
            .presentationDetents(detents.union([.height(sheetHeight)]))
    }
}

public extension View {
    /// Applies a fitted bottom sheet with dynamic height adjustment to the view.
    /// - Parameter detents: A set of presentation detents that define the possible heights of the sheet.
    /// - Returns: A modified view with the fitted sheet functionality.
    func dynamicSheet(detents: Set<PresentationDetent> = []) -> some View {
        modifier(DynamicSheetModifier(detents: detents))
    }
}

#if DEBUG
#Preview {
    @Previewable @State var isSheetPresented = true

    NavigationStack {
        Button("Show Sheet") {
            isSheetPresented.toggle()
        }
        .sheet(isPresented: $isSheetPresented) {
            Text("Hello")
                .padding()
                .dynamicSheet(detents: [.medium])
        }
    }
}
#endif
