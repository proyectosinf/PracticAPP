import Foundation

// sourcery: AutoMockable
public protocol RegisterUseCase: Sendable {
    func invoke(email: String, password: String, user: User) async throws
}

public struct RegisterUseCaseImpl: RegisterUseCase {
    private let authRepository: AuthRepository
    private let userRepository: UserRepository
    public init(authRepository: AuthRepository, userRepository: UserRepository) {
        self.authRepository = authRepository
        self.userRepository = userRepository
    }
    public func invoke(email: String, password: String, user: User) async throws {
        let uid = try await authRepository.register(email: email, password: password)
        guard !uid.isEmpty else { throw NSError(domain: "InvalidUID", code: -1) }

        let token = try await authRepository.firebaseToken()
        guard !token.isEmpty else { throw NSError(domain: "InvalidToken", code: -1) }

        do {
            switch user.role {
            case .student:
                let request = RegisterStudentParams(
                    uid: uid,
                    name: user.name,
                    surname: user.surname,
                    email: user.email,
                    role: user.role.rawValue,
                    dni: user.dni,
                    socialSecurityNumber: user.socialSecurityNumber,
                    pdfCv: user.pdfCV,
                    contactName: user.contactName,
                    contactEmail: user.contactEmail,
                    contactPhone: user.contactPhone,
                    photo: user.photo
                )
                try await userRepository.registerStudent(request, authToken: token)

            case .tutor:
                let request = RegisterWorkTutorParams(
                    uid: uid,
                    name: user.name,
                    surname: user.surname,
                    email: user.email,
                    role: user.role.rawValue,
                    photo: user.photo,
                    companyId: user.companyId
                )
                try await userRepository.registerWorkTutor(request, authToken: token)
            }
        } catch {
            try? await authRepository.deleteCurrentUser()
            throw error
        }
    }

}
