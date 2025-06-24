import SwiftUI

public struct VerticalSeparatorView: View {
    public init() {}

    public var body: some View {
        Rectangle()
            .frame(width: 0.5)
            .foregroundColor(.dsOutline)
    }
}

#if DEBUG
struct SeparatorView_Previews: PreviewProvider {
    static var previews: some View {
        VerticalSeparatorView()
    }
}
#endif
