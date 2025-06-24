import SwiftUI

struct CardModifier: ViewModifier {
    let backgroundColor: Color
    let cornerRadius: CGFloat

    func body(content: Content) -> some View {
        content
            .background(backgroundColor)
            .cornerRadius(cornerRadius)
            .clipped()
            .background {
                RoundedRectangle(cornerRadius: cornerRadius)
                    .shadow(color: Color(hex: 0x74808D, alpha: 0.16), radius: 10.5, x: 0, y: 9)
            }
    }
}

public extension View {
    func card(_ backgroundColor: Color = .dsSurface, cornerRadius: CGFloat = 0) -> some View {
        modifier(CardModifier(backgroundColor: backgroundColor, cornerRadius: cornerRadius))
    }
}
