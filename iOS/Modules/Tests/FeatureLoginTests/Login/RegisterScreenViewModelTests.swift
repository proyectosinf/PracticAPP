import AppCommon
import Foundation
import Domain
import Factory
@testable import FeatureLogin
import Mocks
import Testing
import TestingExpectation

@Suite
struct RegisterScreenViewModelTests {
    private let container: Container = .init()
    private let authRepository: AuthRepositoryMock
    private let userRepository: UserRepositoryMock
    private let registerUseCase: RegisterUseCaseMock
    init() {
        let authMock = AuthRepositoryMock()
        let userMock = UserRepositoryMock()
        let registerMock = RegisterUseCaseMock()
        self.authRepository = authMock
        self.userRepository = userMock
        self.registerUseCase = registerMock
        container.authRepository.register { authMock }
        container.userRepository.register { userMock }
        container.registerUseCase.register { registerMock }
    }
    // MARK: - Success
    @MainActor
    @Test
    func testSubmitStudentSuccess() async throws {
        let expectation = Expectation()
        registerUseCase.invokeClosure = { (_: String, _: String, _: User) in
            expectation.fulfill()
        }
        authRepository.fetchCurrentUserClosure = {
            User(
                id: 1, uid: "uid", name: "David", surname: "Castro",
                email: "test@student.com", dni: "12345678Z",
                socialSecurityNumber: nil, pdfCV: nil, photo: nil,
                contactName: nil, contactEmail: nil, contactPhone: nil,
                companyId: nil, role: .student
            )
        }
        let sut = RegisterScreen.ViewModel(container: container)
        sut.state.email.value = "test@student.com"
        sut.state.password.value = "Password123!"
        sut.state.repeatPassword.value = "Password123!"
        sut.state.name.value = "David"
        sut.state.surname.value = "Castro"
        sut.state.dni.value = "12345678Z"
        sut.handle(.submit)
        try await Task.sleep(nanoseconds: 50_000_000)
        #expect(sut.state.navigation == .goToHome)
    }
    // MARK: - Validations
    @MainActor
    @Test
    func testSubmitFailsWithInvalidEmail() async throws {
        let sut = RegisterScreen.ViewModel(container: container)
        sut.state.email.value = "invalid-email"
        sut.state.password.value = "Password123!"
        sut.state.repeatPassword.value = "Password123!"
        sut.state.name.value = "David"
        sut.state.surname.value = "Castro"
        sut.state.dni.value = "12345678Z"

        sut.handle(.submit)
        try await Task.sleep(nanoseconds: 100_000_000)
        #expect(sut.state.navigation == nil)
    }
    @MainActor
    @Test
    func testSubmitFailsWhenPasswordsDoNotMatch() async throws {
        let sut = RegisterScreen.ViewModel(container: container)
        sut.state.email.value = "test@test.com"
        sut.state.password.value = "Password123!"
        sut.state.repeatPassword.value = "OtherPassword"
        sut.state.name.value = "David"
        sut.state.surname.value = "Castro"
        sut.state.dni.value = "12345678Z"
        sut.handle(.submit)
        try await Task.sleep(nanoseconds: 100_000_000)
        #expect(sut.state.navigation == nil)
    }
    @MainActor
    @Test
    func testSubmitFailsWhenFieldsAreEmpty() async throws {
        let sut = RegisterScreen.ViewModel(container: container)
        sut.state.email.value = ""
        sut.state.password.value = ""
        sut.state.repeatPassword.value = ""
        sut.state.name.value = ""
        sut.state.surname.value = ""
        sut.state.dni.value = ""
        sut.handle(.submit)
        try await Task.sleep(nanoseconds: 100_000_000)
        #expect(sut.state.navigation == nil)
    }
    // MARK: - UseCase Error Flow
    @MainActor
    @Test
    func testSubmitFailsWhenUseCaseThrows() async throws {
        registerUseCase.invokeClosure = { (_, _, _) in
            throw NSError(domain: "TestError", code: 999)
        }
        let sut = RegisterScreen.ViewModel(container: container)
        sut.state.email.value = "test@student.com"
        sut.state.password.value = "Password123!"
        sut.state.repeatPassword.value = "Password123!"
        sut.state.name.value = "David"
        sut.state.surname.value = "Castro"
        sut.state.dni.value = "12345678Z"
        sut.handle(.submit)
        try await Task.sleep(nanoseconds: 100_000_000)
        #expect(sut.state.alert != nil)
        #expect(sut.state.navigation == nil)
    }
    @MainActor
    @Test
    func testSubmitWorkTutorSuccess() async throws {
        let expectation = Expectation()
        registerUseCase.invokeClosure = { (_: String, _: String, _: User) in
            expectation.fulfill()
        }
        authRepository.fetchCurrentUserClosure = {
            User(
                id: 2, uid: "uid-tutor", name: "Ana", surname: "Gómez",
                email: "tutor@work.com", dni: "87654321Z",
                socialSecurityNumber: "123456789", pdfCV: nil, photo: nil,
                contactName: "Empresa", contactEmail: "empresa@empresa.com", contactPhone: "123456789",
                companyId: 123, role: .tutor
            )
        }
        let sut = RegisterScreen.ViewModel(container: container)
        sut.state.email.value = "tutor@work.com"
        sut.state.password.value = "Password123!"
        sut.state.repeatPassword.value = "Password123!"
        sut.state.name.value = "Ana"
        sut.state.surname.value = "Gómez"
        sut.state.dni.value = "87654321Z"
        sut.state.contactName.value = "Empresa"
        sut.state.contactEmail.value = "empresa@empresa.com"
        sut.state.contactPhone.value = "123456789"
        sut.state.socialSecurityNumber.value = "123456789"
        sut.state.companyId.value = 123
        sut.state.selectedRole = .tutor
        sut.handle(.submit)
        let navigationExpectation = Expectation()
        Task {
            while sut.state.navigation == nil {
                try? await Task.sleep(nanoseconds: 5_000_000)
            }
            navigationExpectation.fulfill()
        }
        await navigationExpectation.fulfillment(within: .seconds(1))
        #expect(sut.state.navigation == .goToHome)
    }
    @MainActor
    @Test
    func testSubmitFailsWhenFetchCurrentUserThrows() async throws {
        registerUseCase.invokeClosure = { _, _, _ in }

        authRepository.fetchCurrentUserClosure = {
            throw NSError(domain: "TestErrorFetch", code: 1234)
        }

        let sut = RegisterScreen.ViewModel(container: container)

        sut.state.email.value = "test@student.com"
        sut.state.password.value = "Password123!"
        sut.state.repeatPassword.value = "Password123!"
        sut.state.name.value = "David"
        sut.state.surname.value = "Castro"
        sut.state.dni.value = "12345678Z"

        sut.handle(.submit)

        let alertExpectation = Expectation()

        Task {
            while sut.state.alert == nil {
                try? await Task.sleep(nanoseconds: 5_000_000)
            }
            alertExpectation.fulfill()
        }
        await alertExpectation.fulfillment(within: .seconds(1))
        #expect(sut.state.navigation == nil)
    }
    @MainActor
    @Test
    func testSubmitFailsWhenCompanyIdIsEmptyForTutor() async throws {
        let sut = RegisterScreen.ViewModel(container: container)

        sut.state.email.value = "tutor@work.com"
        sut.state.password.value = "Password123!"
        sut.state.repeatPassword.value = "Password123!"
        sut.state.name.value = "Ana"
        sut.state.surname.value = "Gómez"
        sut.state.dni.value = "87654321Z"
        sut.state.companyId.value = nil

        sut.handle(.submit)

        try await Task.sleep(nanoseconds: 100_000_000)

        #expect(sut.state.navigation == nil)
    }

}
