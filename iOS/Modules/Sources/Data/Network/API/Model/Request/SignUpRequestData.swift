import Domain
import Foundation

struct SignUpRequestData: Encodable {
    public let email: String
    public let password: String
}

extension SignUpRequestData {
    public init(request: SignUpRequest) {
        email = request.email
        password = request.password
    }
}
