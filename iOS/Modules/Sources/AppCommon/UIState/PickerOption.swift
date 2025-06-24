import Foundation

public protocol PickerOption: Hashable {
    var pickerOptionRepresentation: String { get }
}

extension String: PickerOption {
    public var pickerOptionRepresentation: String { self }
}

extension Int: PickerOption {
    public var pickerOptionRepresentation: String { "\(self)" }
}

public struct IdentifiablePickerOption<ID: Hashable>: PickerOption, Identifiable {
    public let id: ID
    public let pickerOptionRepresentation: String

    public init(id: ID, pickerOptionRepresentation: String) {
        self.id = id
        self.pickerOptionRepresentation = pickerOptionRepresentation
    }
}
