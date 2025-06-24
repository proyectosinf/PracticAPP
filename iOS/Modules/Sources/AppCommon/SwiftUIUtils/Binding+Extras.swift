import SwiftUI

public extension Binding {
    func defaultValue<T: Sendable>(_ value: T) -> Binding<T> where Value == T? {
        Binding<T> { wrappedValue ?? value } set: { wrappedValue = $0 }
    }

    func toBool<Wrapped: Sendable>() -> Binding<Bool> where Value == Wrapped? {
        .init(get: { self.wrappedValue != nil }, set: { _ in self.wrappedValue = nil })
    }
}

public struct HashableBinding<T: Hashable & Sendable>: Hashable, Sendable {
    public let id: UUID = .init()
    public let binding: Binding<T>

    public init(binding: Binding<T>) {
        self.binding = binding
    }

    public static func == (lhs: HashableBinding, rhs: HashableBinding) -> Bool {
        lhs.id == rhs.id
    }

    public func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
}
