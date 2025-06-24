import Domain
import Foundation

public struct ActionSheetUIState: Identifiable, Sendable {
    public let id = UUID()
    public let title: String
    public let message: String?
    public let actions: [AlertAction]
}

public extension ActionSheetUIState {
    init(title: String, message: String? = nil, actions: [AlertAction]? = nil) {
        self.title = title
        self.message = message
        self.actions = actions ?? []
    }

    init(title: String, message: String? = nil, action: AlertAction) {
        self.init(title: title, message: message, actions: [action])
    }
}
