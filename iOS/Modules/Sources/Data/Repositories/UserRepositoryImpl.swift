import Domain
import Factory
import Foundation

public final class UserRepositoryImpl: UserRepository, Sendable {
    private let userDataSource: UserDataSource

    public init(container: Container = .shared) {
        self.userDataSource = container.userDataSource()
    }

    public func registerStudent(_ params: RegisterStudentParams, authToken: String) async throws {
        try await userDataSource.registerUser(
            user: params,
            endpoint: "/api/v1/students/",
            authToken: authToken
        )
    }

    public func registerWorkTutor(_ params: RegisterWorkTutorParams, authToken: String) async throws {
        try await userDataSource.registerUser(
            user: params,
            endpoint: "/api/v1/work_tutors/",
            authToken: authToken
        )
    }
}
