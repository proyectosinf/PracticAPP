import SwiftUI

public extension View {
    @ViewBuilder func isHidden(_ hidden: Bool, remove: Bool = false) -> some View {
        if hidden {
            if !remove { self.hidden() }
        } else {
            self
        }
    }

    func modifier(@ViewBuilder _ modifier: (Self) -> some View) -> some View {
        modifier(self)
    }

    @ViewBuilder func `if`(_ condition: Bool, _ transform: (Self) -> some View) -> some View {
        if condition {
            transform(self)
        } else {
            self
        }
    }

    func navigationTitle(_ title: String, displayMode: NavigationBarItem.TitleDisplayMode) -> some View {
        navigationTitle(title)
            .navigationBarTitleDisplayMode(displayMode)
    }
}
