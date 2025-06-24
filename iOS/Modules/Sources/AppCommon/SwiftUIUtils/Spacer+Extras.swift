import SwiftUI

public struct VSpacer: View {
    public let height: CGFloat?

    public init(height: CGFloat?) {
        self.height = height
    }

    public var body: some View {
        Spacer()
            .frame(height: height)
    }
}

public struct HSpacer: View {
    public let width: CGFloat?

    public init(width: CGFloat?) {
        self.width = width
    }

    public var body: some View {
        Spacer()
            .frame(width: width)
    }
}
