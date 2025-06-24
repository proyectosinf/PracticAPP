import Domain
import Factory
import Foundation

public final class DegreeRepositoryImpl: DegreeRepository {
    private let firebaseAuthDataSource: FirebaseAuthDataSource
    private let degreeDataSource: DegreeDataSource

    public init(container: Container = .shared) {
        self.firebaseAuthDataSource = container.firebaseAuthDataSource()
        self.degreeDataSource = container.degreeDataSource()
    }

    public func getDegrees() async throws -> [Domain.Degree] {
        let token = try await firebaseAuthDataSource.currentToken()
        return try await degreeDataSource.getDegrees(authToken: token)
    }
}
