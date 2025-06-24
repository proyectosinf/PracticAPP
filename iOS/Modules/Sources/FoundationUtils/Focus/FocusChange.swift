import Foundation

public struct FocusChange<Focus: Hashable> {
    public let oldValue: Focus?
    public let newValue: Focus?

    public init(oldValue: Focus?, newValue: Focus?) {
        self.oldValue = oldValue
        self.newValue = newValue
    }
}
