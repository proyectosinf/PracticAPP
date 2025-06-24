public enum TabBarRole: String, Sendable {
    case student
    case tutor

    public init?(rawRole: Int) {
        switch rawRole {
        case 1: self = .student
        case 2: self = .tutor
        default: return nil
        }
    }
}
