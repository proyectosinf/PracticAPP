import SwiftUI

@Observable
public final class Coordinator<FullScreen: Identifiable, Sheet: Identifiable, Path: Hashable> {
    public var fullScreen: FullScreen?
    public var sheet: Sheet?
    public var path: [Path] = []
    public var isPresented: Binding<Bool>?

    public init(isPresented: Binding<Bool>?) {
        self.isPresented = isPresented
    }

    public func dismiss() {
        isPresented?.wrappedValue = false
    }

    public func push(_ path: Path) {
        self.path.append(path)
    }

    public func pop() {
        guard !path.isEmpty else { return }
        path.removeLast()
    }

    public func popToRoot() {
        path = []
    }

    public func presentSheet(_ sheet: Sheet) {
        self.sheet = sheet
    }

    public func dismissSheet() {
        sheet = nil
    }

    public func presentFullScreen(_ fullScreen: FullScreen) {
        self.fullScreen = fullScreen
    }

    public func dismissFullScreen() {
        fullScreen = nil
    }
}
