import Foundation

// sourcery: AutoMockable
public protocol ___VARIABLE_useCaseName:identifier___UseCase {
    func invoke() async throws
}

public struct ___VARIABLE_useCaseName:identifier___UseCaseImpl: ___VARIABLE_useCaseName:identifier___UseCase {

    private let <#repository#>: <#Repository#>

    public init(<#repository#>: <#Repository#>) {
        self.<#repository#> = <#repository#>
    }

    public func invoke() async throws {

    }
}
