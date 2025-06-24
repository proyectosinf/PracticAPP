import Factory
import FirebaseCrashlytics
import Foundation

public enum LogLevel: Int, Sendable {
    case info
    case debug
    case warning
    case error
}

public enum LogContext: String {
    case network = "📦"
    case database = "🗄️"
    case businessLogic = "⚙️"
}

// sourcery: AutoMockable
public protocol Logger: Sendable {
    func log(_ level: LogLevel, _ context: LogContext, message: String)
    func log(_ level: LogLevel, _ context: LogContext, message: String, error: Error?)
}

struct LoggerImpl: Logger {
    private let loggerConfig: LoggerConfig

    init(container: Container = .shared) {
        loggerConfig = container.loggerConfig()
    }

    func log(_ level: LogLevel, _ context: LogContext, message: String) {
        log(level, context, message: message, error: nil)
    }

    func log(_ level: LogLevel, _ context: LogContext, message: String, error: Error?) {
        if level.rawValue >= loggerConfig.printLevel.rawValue {
            print("[\(level.emoji)][\(context.rawValue)] \(message)")
        }
        if level == .error, let error {
            Crashlytics.crashlytics().log(message)
            Crashlytics.crashlytics().record(error: error)
        }
    }
}

extension LogLevel {
    var emoji: String {
        switch self {
        case .info: "ℹ️"
        case .debug: "🐞"
        case .warning: "⚠️"
        case .error: "⛔️"
        }
    }
}
