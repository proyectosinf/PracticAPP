import Foundation
@testable import FoundationUtils
import Testing

@Suite("File Manager Tests") struct FileManagerExtrasTests {
    @Test func temporaryDirectoryFileName() {
        let fileManager = FileManager()
        let tempDir = fileManager.temporaryDirectory(fileName: "testFile.txt")
        #expect(tempDir.absoluteString == fileManager.temporaryDirectory.absoluteString + "testFile.txt")
    }

    @Test func temporaryDirectorySanitizedFileName() {
        let fileManager = FileManager.default
        let tempDir = fileManager.temporaryDirectory(fileName: "test/File*Name?.txt")
        #expect(tempDir.absoluteString == fileManager.temporaryDirectory.absoluteString + "testFileName.txt")
    }
}
