import Foundation
import SwiftUI

public enum AlertButtonRole: Sendable {
    case `default`, cancel, destructive
}

extension AlertButtonRole {
    var toSwiftUI: ButtonRole? {
        switch self {
        case .default:
            nil
        case .cancel:
            .cancel
        case .destructive:
            .destructive
        }
    }
}
