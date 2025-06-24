import Foundation

public extension FileManager {
    func temporaryDirectory(fileName: String) -> URL {
        temporaryDirectory.appendingPathComponent(fileName.sanitizedFileName)
    }
}

private extension String {
    var sanitizedFileName: String {
        components(separatedBy: .init(charactersIn: "\\/:*?\"<>|")).joined()
    }
}
