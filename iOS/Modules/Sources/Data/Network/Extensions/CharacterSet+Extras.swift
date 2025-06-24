import Foundation

extension CharacterSet {
    static var fixedUrlQueryAllowed: CharacterSet {
        var allowedQueryParamAndKey = NSCharacterSet.urlQueryAllowed
        allowedQueryParamAndKey.remove(charactersIn: ";/?:@&=+$, ")
        return allowedQueryParamAndKey
    }
}
