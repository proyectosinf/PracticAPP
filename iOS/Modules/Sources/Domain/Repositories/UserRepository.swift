import Foundation

// sourcery: AutoMockable
public protocol UserRepository: Sendable {
        func registerStudent(_ params: RegisterStudentParams, authToken: String) async throws
        func registerWorkTutor(_ params: RegisterWorkTutorParams, authToken: String) async throws
    }
