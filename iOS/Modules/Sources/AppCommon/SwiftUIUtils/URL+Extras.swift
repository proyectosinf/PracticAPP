import Foundation
import SwiftUI

public extension URL {
    @MainActor var canBeOpened: Bool { UIApplication.shared.canOpenURL(self) }
}
