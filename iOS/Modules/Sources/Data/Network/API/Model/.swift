import Domain
import Foundation

struct UserDatab: Codable, Sendable {
   let name: String
   let email: String
}

extension UserDatab {
   var toDomain: User {
       .init(name: name, email: email)
   }
}
