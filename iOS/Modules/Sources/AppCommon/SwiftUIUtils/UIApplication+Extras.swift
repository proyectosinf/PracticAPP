import UIKit

public extension UIApplication {
    var keyWindow: UIWindow? {
        connectedScenes
            .filter { $0.activationState == .foregroundActive }
            .first(where: { $0 is UIWindowScene })
            .flatMap { $0 as? UIWindowScene }?.windows
            .first(where: \.isKeyWindow)
    }

    static var topViewController: UIViewController? {
        let window = UIApplication.shared.keyWindow
        let rootVC = window?.rootViewController
        return rootVC?.top()
    }
}

private extension UIViewController {
    func top() -> UIViewController {
        if let nav = self as? UINavigationController {
            nav.visibleViewController?.top() ?? nav
        } else if let tab = self as? UITabBarController {
            tab.selectedViewController ?? tab
        } else {
            presentedViewController?.top() ?? self
        }
    }
}
