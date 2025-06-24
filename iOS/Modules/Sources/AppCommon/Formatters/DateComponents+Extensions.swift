import Foundation

public extension TimeInterval {
    func formatted(_ formatter: DateComponentsFormatter) -> String {
        formatter.string(from: self) ?? ""
    }
}
