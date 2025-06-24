import SwiftUI

public extension Button where Label == Image {
    init(systemImageName: String, action: @escaping () -> Void) {
        self.init(action: action, label: { Image(systemName: systemImageName) })
    }
}
