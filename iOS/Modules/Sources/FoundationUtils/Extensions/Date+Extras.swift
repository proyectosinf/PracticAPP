import Foundation

public extension Date {
    var startOfDay: Date { Calendar.current.startOfDay(for: self) }

    var endOfDay: Date {
        var components = DateComponents()
        components.day = 1
        components.second = -1
        return Calendar.current.date(byAdding: components, to: startOfDay) ?? self
    }

    func adding(_ component: Calendar.Component, value: Int) -> Date {
        Calendar.current.date(byAdding: component, value: value, to: self) ?? self
    }

    var year: Int { Calendar.current.component(.year, from: self) }

    var month: Int { Calendar.current.component(.month, from: self) }

    init?(year: Int, month: Int = 1, day: Int = 1, timeZone: TimeZone = .current) {
        var components = DateComponents()
        components.day = day
        components.month = month
        components.year = year
        components.calendar = .current
        components.timeZone = timeZone
        guard let date = components.date else { return nil }
        self = date
    }
}
