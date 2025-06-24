import SwiftUI

public extension Button where Label == ActionBarImage {
    init(actionBarImage: String, action: @escaping () -> Void) {
        self.init(action: action, label: { ActionBarImage(systemImage: actionBarImage) })
    }
}

public struct ActionBarImage: View {
    let systemImage: String

    public var body: some View {
        Image(systemName: systemImage)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(width: 26, height: 26)
            .foregroundColor(.dsPrimary)
    }
}
