import SwiftUI

#if DEBUG
public extension View {
    func previewSizeOverlay(_ color: Color = Color.green.opacity(0.5)) -> some View {
        overlay(alignment: .bottomTrailing) {
            GeometryReader { proxy in
                VStack(alignment: .trailing, spacing: 0) {
                    Spacer()
                    Text(proxy.size.debugDescription)
                        .font(.system(size: 7))
                        .background(color)
                }
                .padding(.bottom, -10)
            }
        }
    }
}
#endif
