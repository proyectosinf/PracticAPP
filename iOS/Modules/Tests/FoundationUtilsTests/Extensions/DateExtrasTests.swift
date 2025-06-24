import Foundation
@testable import FoundationUtils
import Testing

@Suite("Date Extras Tests") struct DateExtrasTests {
    @Test func startOfDay() {
        // Create a date with known time components
        var components = DateComponents()
        components.year = 2024
        components.month = 7
        components.day = 30
        components.hour = 15
        components.minute = 45
        components.second = 30

        let date = Calendar.current.date(from: components)!
        let startOfDay = date.startOfDay

        // Expected start of the day
        components.hour = 0
        components.minute = 0
        components.second = 0
        let expectedStartOfDay = Calendar.current.date(from: components)!

        #expect(startOfDay == expectedStartOfDay)
    }

    @Test func endOfDay() {
        // Create a date with known time components
        var components = DateComponents()
        components.year = 2024
        components.month = 7
        components.day = 30
        components.hour = 15
        components.minute = 45
        components.second = 30

        let date = Calendar.current.date(from: components)!
        let endOfDay = date.endOfDay

        // Expected end of the day
        components.hour = 23
        components.minute = 59
        components.second = 59
        let expectedEndOfDay = Calendar.current.date(from: components)!

        #expect(endOfDay == expectedEndOfDay)
    }

    @Test func addingComponent() {
        // Create a date with known time components
        var components = DateComponents()
        components.year = 2024
        components.month = 7
        components.day = 20

        let date = Calendar.current.date(from: components)!

        // Add days
        let newDate = date.adding(.day, value: 5)
        components.day = 25
        let expectedDate = Calendar.current.date(from: components)

        #expect(newDate == expectedDate)
    }

    @Test func year() {
        // Create a date with known time components
        var components = DateComponents()
        components.year = 2024
        components.month = 7
        components.day = 30

        let date = Calendar.current.date(from: components)!

        #expect(date.year == 2024)
    }

    @Test func month() {
        // Create a date with known time components
        var components = DateComponents()
        components.year = 2024
        components.month = 7
        components.day = 30

        let date = Calendar.current.date(from: components)!

        #expect(date.month == 7)
    }

    @Test func initWithYearMonthDay() {
        let date = Date(year: 2024, month: 7, day: 30)

        #expect(date != nil)
        #expect(date?.year == 2024)
        #expect(date?.month == 7)
        #expect(Calendar.current.component(.day, from: date!) == 30)
    }

    @Test func initWithYear() {
        let date = Date(year: 2024)

        #expect(date != nil)
        #expect(date?.year == 2024)
        #expect(date?.month == 1)
        #expect(Calendar.current.component(.day, from: date!) == 1)
    }
}
