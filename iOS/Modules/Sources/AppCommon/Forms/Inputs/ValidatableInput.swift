import Foundation

public protocol ValidatableInput {
    @discardableResult mutating func validate() -> Bool
}
