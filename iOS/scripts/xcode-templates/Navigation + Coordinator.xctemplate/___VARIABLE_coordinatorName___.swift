import SwiftUI

#warning("//TODO: Move to Sources/AppCommon/Coordinators")
public enum ___VARIABLE_name___FullScreen: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum ___VARIABLE_name___Sheet: Hashable, Identifiable, Sendable {
    case mock
    public var id: Self { self }
}

public enum ___VARIABLE_name___Path: Hashable, Sendable {
    case mock
}

public typealias ___VARIABLE_coordinatorName___ = Coordinator<
    ___VARIABLE_name___FullScreen,
    ___VARIABLE_name___Sheet,
    ___VARIABLE_name___Path
>

public extension EnvironmentValues {
    @Entry var ___VARIABLE_coordinatorEnvironmentName___Coordinator: ___VARIABLE_coordinatorName___?
}
