import SwiftUI

public struct IconButton: View {
    @Environment(\.isEnabled) private var isEnabled

    let image: Image
    let action: () -> Void

    public init(image: Image, action: @escaping () -> Void) {
        self.image = image
        self.action = action
    }

    public init(systemImage: String, action: @escaping () -> Void) {
        image = Image(systemName: systemImage)
        self.action = action
    }

    public var body: some View {
        Button(action: action) {
            image
        }
        .clipShape(.circle)
    }
}

struct IconButtonsPreview: View {
    var body: some View {
        VStack {
            HStack {
                IconButton(systemImage: "figure.run") {}

                IconButton(systemImage: "figure.run") {}
                    .disabled(true)
            }
            .buttonStyle(.primary)

            HStack {
                IconButton(systemImage: "figure.run") {}

                IconButton(systemImage: "figure.run") {}
                    .disabled(true)
            }
            .buttonStyle(.primaryMedium)

            HStack {
                IconButton(systemImage: "figure.run") {}

                IconButton(systemImage: "figure.run") {}
                    .disabled(true)
            }
            .buttonStyle(.primarySmall)

            HStack {
                IconButton(systemImage: "figure.run") {}

                IconButton(systemImage: "figure.run") {}
                    .disabled(true)
            }
            .buttonStyle(.secondary)

            HStack {
                IconButton(systemImage: "figure.run") {}

                IconButton(systemImage: "figure.run") {}
                    .disabled(true)
            }
            .buttonStyle(.tertiary)
        }
        .padding(16)
    }
}

#if DEBUG
#Preview { IconButtonsPreview() }
#endif
