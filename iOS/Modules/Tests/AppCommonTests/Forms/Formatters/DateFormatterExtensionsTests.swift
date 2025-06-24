import AppCommon
import Foundation
import Testing

struct DateFormatterExtensionsTests {
    private let date = Date(timeIntervalSince1970: 1_737_705_175)
    private let locale = Locale(identifier: "es_ES")
    private let timeZone = TimeZone.UTC
    private let calendar = Calendar(identifier: .gregorian)

    private func sut(dateStyle: DateFormatter.Style, timeStyle: DateFormatter.Style) -> DateFormatter {
        DateFormatter(
            dateStyle: dateStyle,
            timeStyle: timeStyle,
            calendar: calendar,
            locale: locale,
            timeZone: timeZone
        )
    }

    @Test func fullDateStyleAndMediumDateStyle() async throws {
        // Given
        let sut = sut(dateStyle: .full, timeStyle: .medium)
        // Then
        #expect(sut.string(from: date) == "viernes, 24 de enero de 2025, 7:52:55")
    }

    @Test func mediumDateStyleAndMediumDateStyle() async throws {
        // Given
        let sut = sut(dateStyle: .medium, timeStyle: .medium)

        // Then
        #expect(sut.string(from: date) == "24 ene 2025, 7:52:55")
    }

    @Test func shortDateStyleAndShortDateStyle() async throws {
        // Given
        let sut = sut(dateStyle: .short, timeStyle: .short)

        // Then
        #expect(sut.string(from: date) == "24/1/25, 7:52")
    }

    @Test func fullDateStyle() async throws {
        // Given
        let sut = sut(dateStyle: .full, timeStyle: .none)

        // Then
        #expect(sut.string(from: date) == "viernes, 24 de enero de 2025")
    }

    @Test func fullTimeStyle() async throws {
        // Given
        let sut = sut(dateStyle: .none, timeStyle: .full)

        // Then
        #expect(sut.string(from: date) == "7:52:55 (hora del meridiano de Greenwich)")
    }
}
