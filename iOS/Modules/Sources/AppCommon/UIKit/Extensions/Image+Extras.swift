import UIKit

public extension UIImage {
    func toJPG(_ expectedSizeInMb: Int = 1) -> Data? {
        let sizeInBytes = expectedSizeInMb * 1024 * 1024
        var compressingValue: CGFloat = 1.0
        while compressingValue > 0.0 {
            if let data = jpegData(compressionQuality: compressingValue) {
                if data.count < sizeInBytes {
                    return data
                } else {
                    compressingValue -= 0.1
                }
            }
        }
        return nil
    }
}
