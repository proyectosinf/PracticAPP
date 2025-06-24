import AppCommon
import Foundation
@testable import Data
import Factory
@testable import FeatureLogin
import Mocks
import Testing
import TestingExpectation

struct LoginScreenViewModelTests {
    private let container = Container()
    private var authRepositoryMock: AuthRepositoryMock!

    init() {
        let authRepositoryMock: AuthRepositoryMock! = .init()
        self.authRepositoryMock = authRepositoryMock
        container.authRepository.register { authRepositoryMock }
    }

    @MainActor
    @Test func loginSuccess() async throws {
        let signInExpectation = Expectation(expectedCount: 1)
        let navigationExpectation = Expectation(expectedCount: 1)

        authRepositoryMock.signInFirebaseClosure = { _, _ in
            signInExpectation.fulfill()
        }

        let sut = LoginScreen.ViewModel(container: container)
        sut.state.username.value = "test@test.com"
        sut.state.password.value = "Password123!"

        Task {
            while sut.state.navigation != .loginSuccess {
                try? await Task.sleep(nanoseconds: 50_000_000)
            }
            navigationExpectation.fulfill()
        }

        sut.handle(.submit)

        await signInExpectation.fulfillment(within: .seconds(2))
        await navigationExpectation.fulfillment(within: .seconds(2))
        #expect(sut.state.navigation == .loginSuccess)
    }

    @MainActor
    @Test func loginError() async throws {
        let signInExpectation = Expectation(expectedCount: 1)
        let alertExpectation = Expectation(expectedCount: 1)

        authRepositoryMock.signInFirebaseClosure = { _, _ in
            signInExpectation.fulfill()
            throw NSError(domain: "", code: -1)
        }

        let sut = LoginScreen.ViewModel(container: container)
        sut.state.username.value = "test@test.com"
        sut.state.password.value = "Password123!"

        Task {
            while sut.state.alert == nil {
                try? await Task.sleep(nanoseconds: 50_000_000)
            }
            alertExpectation.fulfill()
        }

        sut.handle(.submit)

        await signInExpectation.fulfillment(within: .seconds(2))
        await alertExpectation.fulfillment(within: .seconds(2))

        #expect(sut.state.navigation == nil)
        #expect(sut.state.alert != nil)
        #expect(sut.state.alert?.title == Strings.general.common_error_text)
    }

    @MainActor
    @Test func loginSetsLoadingStateCorrectly() async throws {
        let loadingStartExpectation = Expectation(expectedCount: 1)
        let loadingEndExpectation = Expectation(expectedCount: 1)

        let sut = LoginScreen.ViewModel(container: container)

        authRepositoryMock.signInFirebaseClosure = { _, _ in
            if sut.state.loading {
                loadingStartExpectation.fulfill()
            }
            try await Task.sleep(nanoseconds: 100_000_000)
        }

        sut.state.username.value = "test@test.com"
        sut.state.password.value = "Password123!"

        sut.handle(.submit)

        await loadingStartExpectation.fulfillment(within: .seconds(1))

        Task {
            while sut.state.loading {
                try? await Task.sleep(nanoseconds: 50_000_000)
            }
            loadingEndExpectation.fulfill()
        }

        await loadingEndExpectation.fulfillment(within: .seconds(2))
        #expect(sut.state.loading == false)
    }
    @MainActor
    @Test func testLoginFailsWhenEmailIsEmpty() async throws {
        var wasCalled = false

        authRepositoryMock.signInFirebaseClosure = { _, _ in
            wasCalled = true
        }

        let sut = LoginScreen.ViewModel(container: container)
        sut.state.username.value = ""  // Email vacío
        sut.state.password.value = "Password123!"

        sut.handle(.submit)

        try await Task.sleep(nanoseconds: 100_000_000)

        #expect(wasCalled == false)
        #expect(sut.state.alert == nil)
        #expect(sut.state.navigation == nil)
    }
    @MainActor
    @Test func testLoginFailsWhenEmailIsInvalid() async throws {
        var wasCalled = false

        authRepositoryMock.signInFirebaseClosure = { _, _ in
            wasCalled = true
        }

        let sut = LoginScreen.ViewModel(container: container)
        sut.state.username.value = "invalidemail"
        sut.state.password.value = "Password123!"

        sut.handle(.submit)

        try await Task.sleep(nanoseconds: 100_000_000)

        #expect(wasCalled == false)
        #expect(sut.state.alert == nil)
        #expect(sut.state.navigation == nil)
    }
    @MainActor
    @Test func testLoginFailsWhenPasswordIsInvalid() async throws {
        var wasCalled = false

        authRepositoryMock.signInFirebaseClosure = { _, _ in
            wasCalled = true
        }

        let sut = LoginScreen.ViewModel(container: container)
        sut.state.username.value = "test@test.com"
        sut.state.password.value = "12"  // Muy corta

        sut.handle(.submit)

        try await Task.sleep(nanoseconds: 100_000_000)

        #expect(wasCalled == false)
        #expect(sut.state.alert == nil)
        #expect(sut.state.navigation == nil)
    }

}
