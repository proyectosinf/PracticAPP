import Foundation

public struct LoggerConfig: Sendable {
    public let printLevel: LogLevel

    public init(printLevel: LogLevel) {
        self.printLevel = printLevel
    }
}
