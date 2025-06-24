import Foundation

public struct FormattedDate {
    private init() {}

    private static let backendDateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.timeZone = TimeZone(secondsFromGMT: 0)
        return formatter
    }()

    private static let uiDateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "dd-MM-yyyy"
        return formatter
    }()

    public static func format(_ date: Date) -> String {
        return uiDateFormatter.string(from: date)
    }

    public static func format(_ backendDate: String) -> String {
        guard let date = backendDateFormatter.date(from: backendDate) else {
            return backendDate
        }
        return uiDateFormatter.string(from: date)
    }
}
