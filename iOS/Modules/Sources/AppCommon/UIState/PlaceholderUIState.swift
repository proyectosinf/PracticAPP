import Foundation

public struct PlaceholderUIState {
    public let title: String
    public let action: ActionUIState?

    public init(title: String, action: ActionUIState? = nil) {
        self.title = title
        self.action = action
    }
}
