import Factory
import Foundation

public extension Container {
    var loggerConfig: Factory<LoggerConfig> { Factory(self) { .init(printLevel: .error) } }
    var logger: Factory<Logger> { Factory(self) { LoggerImpl() } }
}
