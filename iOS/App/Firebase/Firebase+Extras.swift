import Firebase
import Foundation

extension FirebaseApp {
    static func configureFromInfoPlist(_ fileName: String) {
        guard
            let filePath = Bundle.main.path(forResource: fileName, ofType: "plist"),
            let options = FirebaseOptions(contentsOfFile: filePath)
        else {
            fatalError("GoogleServiceFile key or value is missing in the Info.plist")
        }
        configure(options: options)
    }
}
