import Foundation

public struct AlertAction: Identifiable, Sendable {
    public let id = UUID()
    public let buttonType: AlertButtonRole
    public let title: String
    public let action: @MainActor @Sendable () -> Void
}

public extension AlertAction {
    static func `default`(title: String, action: @escaping (@MainActor @Sendable () -> Void) = {}) -> Self {
        self.init(buttonType: .default, title: title, action: action)
    }

    static var cancel: Self {
        self.init(buttonType: .cancel, title: Strings.general.common_cancel_text, action: {})
    }

    static func cancel(
        title: String = Strings.general.common_cancel_text,
        action: @escaping @Sendable () -> Void = {}
    ) -> Self {
        self.init(buttonType: .cancel, title: title, action: action)
    }

    static func destructive(title: String, action: @escaping @MainActor @Sendable () -> Void = {}) -> Self {
        self.init(buttonType: .destructive, title: title, action: action)
    }

    static var accept: Self {
        self.init(buttonType: .default, title: Strings.general.common_accept_text, action: {})
    }

    static func accept(action: @escaping @MainActor @Sendable () -> Void) -> Self {
        self.init(buttonType: .default, title: Strings.general.common_accept_text, action: action)
    }
}
