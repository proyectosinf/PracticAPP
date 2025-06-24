import SwiftUI
import Kingfisher

public struct RemoteImage<Placeholder: View>: View {
    public let url: String
    private let placeholder: () -> Placeholder
    private let contentMode: SwiftUI.ContentMode

    public init(url: String,
                contentMode: SwiftUI.ContentMode = .fill,
                placeholder: @escaping () -> Placeholder) {
        self.url = url
        self.contentMode = contentMode
        self.placeholder = placeholder
    }

    public var body: some View {
        KFImage(URL(string: url))
            .placeholder(placeholder)
            .cancelOnDisappear(true)
            .resizable()
            .aspectRatio(contentMode: contentMode)
            .clipped()
    }
}

public extension RemoteImage where Placeholder == ProgressView<EmptyView, EmptyView> {
    init(url: String, contentMode: SwiftUI.ContentMode = .fill) {
        self.url = url
        self.contentMode = contentMode
        self.placeholder = { ProgressView() }
    }
}

#if DEBUG
#Preview {
    VStack {
        RemoteImage(url: "https://www.google.es/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png")

        RemoteImage(url: "https://www.google.es/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png")
            .frame(width: 50, height: 50)

        RemoteImage(
            url: "https://www.google.es/images/branding/googlelogo",
            placeholder: { Image(systemName: "fuelpump.fill").resizable() }
        )
        .frame(width: 50, height: 50)
    }
}
#endif
