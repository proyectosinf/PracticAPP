import Foundation

public extension TimeInterval {
    func formatDuration(_ units: NSCalendar.Unit = [.hour, .minute, .second]) -> String {
        let formatter = DateComponentsFormatter()
        formatter.unitsStyle = .positional
        formatter.allowedUnits = units
        formatter.zeroFormattingBehavior = .pad

        let formattedDuration = formatter.string(from: self)
        return formattedDuration ?? ""
    }
}
