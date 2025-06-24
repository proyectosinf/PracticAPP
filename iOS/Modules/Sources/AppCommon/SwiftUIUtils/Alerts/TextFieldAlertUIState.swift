import Foundation
import SwiftUI

public struct TextFieldAlertUIState: Identifiable, Sendable {
    public struct Action: Sendable, Identifiable {
        public let id = UUID()
        public let buttonType: AlertButtonRole
        public let title: String
        public let action: @Sendable (String) -> Void

        private init(
            buttonType: AlertButtonRole = .default,
            title: String,
            action: @escaping @Sendable (String) -> Void = { _ in }
        ) {
            self.buttonType = buttonType
            self.title = title
            self.action = action
        }
    }

    public let id = UUID()
    public let title: String
    public let message: String?
    public let text: Binding<String>
    public let placeholder: String
    public let actions: [Action]

    public init(
        title: String,
        message: String? = nil,
        text: Binding<String>,
        placeholder: String = "",
        actions: [Action]? = nil
    ) {
        self.title = title
        self.message = message
        self.text = text
        self.placeholder = placeholder
        self.actions = actions ?? []
    }
}

public extension TextFieldAlertUIState.Action {
    static func `default`(title: String, action: @escaping (@Sendable (String) -> Void) = { _ in }) -> Self {
        self.init(buttonType: .default, title: title, action: action)
    }

    static var cancel: Self {
        self.init(buttonType: .cancel, title: Strings.general.common_cancel_text)
    }

    static func cancel(
        title: String = Strings.general.common_cancel_text,
        action: @escaping @Sendable (String) -> Void = { _ in }
    ) -> Self {
        self.init(buttonType: .cancel, title: title, action: action)
    }

    static func destructive(title: String, action: @escaping @Sendable (String) -> Void = { _ in }) -> Self {
        self.init(buttonType: .destructive, title: title, action: action)
    }

    static var accept: Self {
        self.init(buttonType: .default, title: Strings.general.common_accept_text)
    }

    static func accept(action: @escaping @Sendable (String) -> Void) -> Self {
        self.init(buttonType: .default, title: Strings.general.common_accept_text, action: action)
    }
}
