import Data
import Domain
import Foundation

public struct AppUpdateUIState {
    public let force: Bool
    public let url: String?
    public var message: String {
        force ? Strings.general.common_force_to_update_text : Strings.general.common_optional_update_text
    }
}

public extension AppUpdate {
    var toUIState: AppUpdateUIState {
        .init(force: force, url: url)
    }
}

public extension AppUpdateUIState {
    static var mockForce: AppUpdateUIState {
        .init(force: true, url: "")
    }

    static var mockOptional: AppUpdateUIState {
        .init(force: false, url: "")
    }
}
