import Foundation

// sourcery: AutoMockable
public protocol DegreeRepository: Sendable {
    func getDegrees() async throws -> [Degree]
}
