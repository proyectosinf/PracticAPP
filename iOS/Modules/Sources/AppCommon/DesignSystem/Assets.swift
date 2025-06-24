import SwiftUI

public enum Assets {
    public enum Images {
        case arrowshapeForwardFill
        case system(String)
    }
}

extension Assets.Images {
    var resource: ImageResource {
        switch self {
        case .arrowshapeForwardFill: .arrowshapeForwardFill
        case .system: .arrowshapeForwardFill // Should never be used
        }
    }
}

public extension Image {
    init(asset: Assets.Images) {
        switch asset {
        case let .system(name):
            self.init(systemName: name)
        default:
            self.init(asset.resource)
        }
    }
}
