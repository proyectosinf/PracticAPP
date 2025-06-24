import SwiftUI

public extension EdgeInsets {
    static func horizontal(_ spacing: CGFloat) -> EdgeInsets {
        .init(top: 0, leading: spacing, bottom: 0, trailing: spacing)
    }

    static func vertical(_ spacing: CGFloat) -> EdgeInsets {
        .init(top: spacing, leading: 0, bottom: spacing, trailing: 0)
    }

    static func axis(h horizontal: CGFloat, v vertical: CGFloat) -> EdgeInsets {
        .init(top: vertical, leading: horizontal, bottom: vertical, trailing: horizontal)
    }
}
