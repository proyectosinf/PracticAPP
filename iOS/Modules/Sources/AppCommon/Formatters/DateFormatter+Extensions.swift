import Foundation

public extension DateFormatter {
    convenience init(
        dateStyle: DateFormatter.Style,
        timeStyle: DateFormatter.Style,
        calendar: Calendar = .current,
        locale: Locale = .current,
        timeZone: TimeZone = .autoupdatingCurrent
    ) {
        self.init()
        self.dateStyle = dateStyle
        self.timeStyle = timeStyle
        self.calendar = calendar
        self.locale = locale
        self.timeZone = timeZone
    }
}

public extension Date {
    func formatted(_ formatter: DateFormatter) -> String {
        formatter.string(from: self)
    }
}
