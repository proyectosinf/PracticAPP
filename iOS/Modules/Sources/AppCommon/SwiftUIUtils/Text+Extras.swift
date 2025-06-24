import Foundation
import SwiftUI

public extension Text {
    func leading(systemImage: String) -> Text {
        Text(Image(systemName: systemImage)) + Text(" ") + self
    }

    func trailing(systemImage: String) -> Text {
        self + Text(" ") + Text(Image(systemName: systemImage))
    }
}
