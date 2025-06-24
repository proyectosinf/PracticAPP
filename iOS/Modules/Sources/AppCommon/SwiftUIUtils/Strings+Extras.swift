import Foundation

public extension String {
    @MainActor var openableURL: URL? {
        guard let url = toURL, url.canBeOpened else { return nil }
        return url
    }
}
