import SwiftUI

public struct ActivityIndicatorView: UIViewRepresentable {
    let style: UIActivityIndicatorView.Style

    public init(style: UIActivityIndicatorView.Style = .large) {
        self.style = style
    }

    public func makeUIView(context: Context) -> some UIActivityIndicatorView {
        UIActivityIndicatorView(style: style)
    }

    public func updateUIView(_ uiView: UIViewType, context: Context) {
        uiView.startAnimating()
    }
}

struct LoadingModifier: ViewModifier {
    let message: String?

    func body(content: Content) -> some View {
        ZStack {
            content
            if let message {
                LoadingView(message: message)
            }
        }
    }
}

struct LoadingView: View {
    let message: String

    var body: some View {
        ZStack {
            Color.black
                .opacity(0.3)
                .edgesIgnoringSafeArea(.all)
                .blur(radius: 200)

            ZStack {
                HStack {
                    ActivityIndicatorView(style: message.isEmpty ? .large : .medium)

                    if !message.isEmpty {
                        Text(message)
                            .font(.callout)
                    }
                }
            }
            .padding(.spacingM)
            .background(Color.dsSurface)
            .cornerRadius(15)
            .padding(.spacingS)
        }
    }
}

public extension View {
    func loading(message: String?) -> some View {
        modifier(LoadingModifier(message: message))
    }

    func loading(loading: Bool) -> some View {
        modifier(LoadingModifier(message: loading ? "" : nil))
    }
}

#if DEBUG
struct LoadingView_Previews: PreviewProvider {
    static var previews: some View {
        Text("Hola JUAN")
            .loading(message: "Obteniendo ubicación actual...")
    }
}
#endif
