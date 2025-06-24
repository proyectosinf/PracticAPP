import SwiftUI

struct ViewDidLoadModifier: ViewModifier {
    @State private var viewDidLoad = false
    let action: (() -> Void)?

    func body(content: Content) -> some View {
        content
            .onAppear {
                guard !viewDidLoad else { return }
                viewDidLoad = true
                action?()
            }
    }
}

public extension View {
    func onViewDidLoad(perform action: (() -> Void)? = nil) -> some View {
        modifier(ViewDidLoadModifier(action: action))
    }
}
