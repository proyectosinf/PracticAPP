// Generated using Sourcery 2.2.5 — https://github.com/krzysztofzablocki/Sourcery
// DO NOT EDIT
// swiftlint:disable line_length
// swiftlint:disable variable_name

import Foundation
#if os(iOS) || os(tvOS) || os(watchOS)
import UIKit
#elseif os(OSX)
import AppKit
#endif

@testable import Data
@testable import Domain
@testable import Logger

public class AppInfoDataSourceMock: AppInfoDataSource {
    public init() {}

    public var currentVersion: String {
        get { underlyingCurrentVersion }
        set(value) { underlyingCurrentVersion = value }
    }

    public var underlyingCurrentVersion: String!
}

public class AppSettingsRepositoryMock: AppSettingsRepository {
    public init() {}

    public var currentVersion: AppVersion {
        get { underlyingCurrentVersion }
        set(value) { underlyingCurrentVersion = value }
    }

    public var underlyingCurrentVersion: AppVersion!
    public var updatingVersion: AppVersion?

    // MARK: - availableAppUpdate

    public var availableAppUpdateAvailableAppUpdateCallsCount = 0
    public var availableAppUpdateAvailableAppUpdateCalled: Bool {
        availableAppUpdateAvailableAppUpdateCallsCount > 0
    }

    public var availableAppUpdateAvailableAppUpdateReturnValue: AvailableAppUpdate?
    public var availableAppUpdateAvailableAppUpdateClosure: (() async -> AvailableAppUpdate?)?

    public func availableAppUpdate() async -> AvailableAppUpdate? {
        availableAppUpdateAvailableAppUpdateCallsCount += 1
        if let availableAppUpdateAvailableAppUpdateClosure {
            return await availableAppUpdateAvailableAppUpdateClosure()
        } else {
            return availableAppUpdateAvailableAppUpdateReturnValue
        }
    }

    // MARK: - features

    public var featuresFeatureFlagThrowableError: (any Error)?
    public var featuresFeatureFlagCallsCount = 0
    public var featuresFeatureFlagCalled: Bool {
        featuresFeatureFlagCallsCount > 0
    }

    public var featuresFeatureFlagReturnValue: [FeatureFlag]!
    public var featuresFeatureFlagClosure: (() async throws -> [FeatureFlag])?

    public func features() async throws -> [FeatureFlag] {
        featuresFeatureFlagCallsCount += 1
        if let error = featuresFeatureFlagThrowableError {
            throw error
        }
        if let featuresFeatureFlagClosure {
            return try await featuresFeatureFlagClosure()
        } else {
            return featuresFeatureFlagReturnValue
        }
    }
}

class AuthAPIDataSourceMock: AuthAPIDataSource, @unchecked Sendable {
    
    // MARK: - signIn

    var signInUserStringPasswordStringAPITokenDataThrowableError: (any Error)?
    var signInUserStringPasswordStringAPITokenDataCallsCount = 0
    var signInUserStringPasswordStringAPITokenDataCalled: Bool {
        signInUserStringPasswordStringAPITokenDataCallsCount > 0
    }

    var signInUserStringPasswordStringAPITokenDataReceivedArguments: (user: String, password: String)?
    var signInUserStringPasswordStringAPITokenDataReceivedInvocations: [(user: String, password: String)] = []
    var signInUserStringPasswordStringAPITokenDataReturnValue: APITokenData!
    var signInUserStringPasswordStringAPITokenDataClosure: ((String, String) async throws -> APITokenData)?

    func signIn(user: String, password: String) async throws -> APITokenData {
        signInUserStringPasswordStringAPITokenDataCallsCount += 1
        signInUserStringPasswordStringAPITokenDataReceivedArguments = (user: user, password: password)
        signInUserStringPasswordStringAPITokenDataReceivedInvocations.append((user: user, password: password))
        if let error = signInUserStringPasswordStringAPITokenDataThrowableError {
            throw error
        }
        if let signInUserStringPasswordStringAPITokenDataClosure {
            return try await signInUserStringPasswordStringAPITokenDataClosure(user, password)
        } else {
            return signInUserStringPasswordStringAPITokenDataReturnValue
        }
    }

    // MARK: - signUp

    var signUpRequestSignUpRequestDataVoidThrowableError: (any Error)?
    var signUpRequestSignUpRequestDataVoidCallsCount = 0
    var signUpRequestSignUpRequestDataVoidCalled: Bool {
        signUpRequestSignUpRequestDataVoidCallsCount > 0
    }

    var signUpRequestSignUpRequestDataVoidReceivedRequest: SignUpRequestData?
    var signUpRequestSignUpRequestDataVoidReceivedInvocations: [SignUpRequestData] = []
    var signUpRequestSignUpRequestDataVoidClosure: ((SignUpRequestData) async throws -> Void)?

    func signUp(request: SignUpRequestData) async throws {
        signUpRequestSignUpRequestDataVoidCallsCount += 1
        signUpRequestSignUpRequestDataVoidReceivedRequest = request
        signUpRequestSignUpRequestDataVoidReceivedInvocations.append(request)
        if let error = signUpRequestSignUpRequestDataVoidThrowableError {
            throw error
        }
        try await signUpRequestSignUpRequestDataVoidClosure?(request)
    }

    // MARK: - refreshToken

    var refreshTokenRefreshTokenStringAPITokenDataThrowableError: (any Error)?
    var refreshTokenRefreshTokenStringAPITokenDataCallsCount = 0
    var refreshTokenRefreshTokenStringAPITokenDataCalled: Bool {
        refreshTokenRefreshTokenStringAPITokenDataCallsCount > 0
    }

    var refreshTokenRefreshTokenStringAPITokenDataReceivedRefreshToken: String?
    var refreshTokenRefreshTokenStringAPITokenDataReceivedInvocations: [String] = []
    var refreshTokenRefreshTokenStringAPITokenDataReturnValue: APITokenData?
    var refreshTokenRefreshTokenStringAPITokenDataClosure: ((String) async throws -> APITokenData?)?

    func refreshToken(refreshToken: String) async throws -> APITokenData? {
        refreshTokenRefreshTokenStringAPITokenDataCallsCount += 1
        refreshTokenRefreshTokenStringAPITokenDataReceivedRefreshToken = refreshToken
        refreshTokenRefreshTokenStringAPITokenDataReceivedInvocations.append(refreshToken)
        if let error = refreshTokenRefreshTokenStringAPITokenDataThrowableError {
            throw error
        }
        if let refreshTokenRefreshTokenStringAPITokenDataClosure {
            return try await refreshTokenRefreshTokenStringAPITokenDataClosure(refreshToken)
        } else {
            return refreshTokenRefreshTokenStringAPITokenDataReturnValue
        }
    }

    // MARK: - user

    var userUserDataThrowableError: (any Error)?
    var userUserDataCallsCount = 0
    var userUserDataCalled: Bool {
        userUserDataCallsCount > 0
    }

    var userUserDataReturnValue: UserData!
    var userUserDataClosure: (() async throws -> UserData)?

    func user() async throws -> UserData {
        userUserDataCallsCount += 1
        if let error = userUserDataThrowableError {
            throw error
        }
        if let userUserDataClosure {
            return try await userUserDataClosure()
        } else {
            return userUserDataReturnValue
        }
    }
}

public class UserRepositoryMock: UserRepository, @unchecked Sendable {
    public init() {}

    public var registerStudentClosure: ((RegisterStudentParams, String) async throws -> Void)?
    public func registerStudent(_ params: RegisterStudentParams, authToken: String) async throws {
        try await registerStudentClosure?(params, authToken)
    }

    public var registerWorkTutorClosure: ((RegisterWorkTutorParams, String) async throws -> Void)?
    public func registerWorkTutor(_ params: RegisterWorkTutorParams, authToken: String) async throws {
        try await registerWorkTutorClosure?(params, authToken)
    }
}
public class AuthRepositoryMock: AuthRepository, @unchecked Sendable {
    public init() {}

    public var fetchCurrentUserClosure: (() async throws -> User)?
    public func fetchCurrentUser() async throws -> User {
        if let fetchCurrentUserClosure = fetchCurrentUserClosure {
            return try await fetchCurrentUserClosure()
        } else {
            return User(
                id: 1,
                uid: "mock-uid",
                name: "David",
                surname: "Castro",
                email: "mock@user.com",
                dni: nil,
                socialSecurityNumber: nil,
                pdfCV: nil,
                photo: nil,
                contactName: nil,
                contactEmail: nil,
                contactPhone: nil,
                companyId: 1,
                role: .tutor
            )
        }
    }

    // MARK: - deleteCurrentUser
    public var deleteCurrentUserCalled = false
    public func deleteCurrentUser() async throws {
        deleteCurrentUserCalled = true
    }

    // MARK: - refreshFirebaseToken
    public var refreshFirebaseTokenCalled = false
    public func refreshFirebaseToken() async throws {
        refreshFirebaseTokenCalled = true
    }

    public var signInFirebaseCallsCount = 0
    public var signInFirebaseClosure: ((String, String) async throws -> Void)?
    public func signInFirebase(email: String, password: String) async throws {
        signInFirebaseCallsCount += 1
        try await signInFirebaseClosure?(email, password)
    }

    public var resetPasswordClosure: ((String) async throws -> Void)?
    public func sendResetPassword(email: String) async throws {
        try await resetPasswordClosure?(email)
    }

    public var firebaseToken: (() async throws -> String)?
    public func firebaseToken() async throws -> String {
        if let firebaseToken = firebaseToken {
            return try await firebaseToken()
        } else {
            return "mock-token"
        }
    }

    public var signInClosure: ((String, String) async throws -> Void)?
    public func signIn(user: String, password: String) async throws {
        try await signInClosure?(user, password)
    }

    public var signUpClosure: ((SignUpRequest) async throws -> Void)?
    public func signUp(request: SignUpRequest) async throws {
        try await signUpClosure?(request)
    }

    public var registerClosure: ((String, String) async throws -> String)?
    public func register(email: String, password: String) async throws -> String {
        try await registerClosure?(email, password) ?? "mock-uid"
    }

    public var signOutClosure: (() async throws -> Void)?
    public func signOut() async throws {
        try await signOutClosure?()
    }

    public var isUserLoggedInReturnValue: Bool = false
    public func isUserLoggedIn() async -> Bool {
        return isUserLoggedInReturnValue
    }

    public var signedInUserStreamClosure: (() async -> AsyncStream<Bool>)?
    public var signedInUserStream: AsyncStream<Bool> {
        get async {
            if let closure = signedInUserStreamClosure {
                return await closure()
            }
            return AsyncStream { continuation in
                continuation.yield(false)
                continuation.finish()
            }
        }
    }

    public var userReturnValue: User?
    public func user() async throws -> User {
        if let user = userReturnValue {
            return user
        } else {
            throw NSError(domain: "AuthRepositoryMock", code: 1, userInfo: nil)
        }
    }
}
public class RegisterUseCaseMock: RegisterUseCase, @unchecked Sendable{
    public init() {}
    public var invokeClosure: ((String, String, User) async throws -> Void)?
    public func invoke(email: String, password: String, user: User) async throws {
        try await invokeClosure?(email, password, user)
    }
    
}

public class FeatureEnabledUseCaseMock: FeatureEnabledUseCase {
    public init() {}

    // MARK: - invoke

    public var invokeNameStringBoolThrowableError: (any Error)?
    public var invokeNameStringBoolCallsCount = 0
    public var invokeNameStringBoolCalled: Bool {
        invokeNameStringBoolCallsCount > 0
    }

    public var invokeNameStringBoolReceivedName: String?
    public var invokeNameStringBoolReceivedInvocations: [String] = []
    public var invokeNameStringBoolReturnValue: Bool!
    public var invokeNameStringBoolClosure: ((String) async throws -> Bool)?

    public func invoke(_ name: String) async throws -> Bool {
        invokeNameStringBoolCallsCount += 1
        invokeNameStringBoolReceivedName = name
        invokeNameStringBoolReceivedInvocations.append(name)
        if let error = invokeNameStringBoolThrowableError {
            throw error
        }
        if let invokeNameStringBoolClosure {
            return try await invokeNameStringBoolClosure(name)
        } else {
            return invokeNameStringBoolReturnValue
        }
    }
}
class KeychainDataSourceMock: KeychainDataSource, @unchecked Sendable {
private var firebaseTokenStorage: String?

   func saveFirebaseToken(_ token: String) {
       firebaseTokenStorage = token
   }

   func firebaseToken() -> String? {
       return firebaseTokenStorage
   }

   func deleteFirebaseToken() {
       firebaseTokenStorage = nil
   }

   func isLoggedIn() -> Bool {
       return firebaseTokenStorage != nil
   }
    
    // MARK: - save

    var saveTokenAPITokenDataServiceStringAccountStringBoolCallsCount = 0
    var saveTokenAPITokenDataServiceStringAccountStringBoolCalled: Bool {
        saveTokenAPITokenDataServiceStringAccountStringBoolCallsCount > 0
    }

    var saveTokenAPITokenDataServiceStringAccountStringBoolReceivedArguments: (
        token: APITokenData,
        service: String,
        account: String
    )?
    var saveTokenAPITokenDataServiceStringAccountStringBoolReceivedInvocations: [(
        token: APITokenData,
        service: String,
        account: String
    )] = []
    var saveTokenAPITokenDataServiceStringAccountStringBoolReturnValue: Bool!
    var saveTokenAPITokenDataServiceStringAccountStringBoolClosure: ((APITokenData, String, String) -> Bool)?

    @discardableResult
    func save(token: APITokenData, service: String, account: String) -> Bool {
        saveTokenAPITokenDataServiceStringAccountStringBoolCallsCount += 1
        saveTokenAPITokenDataServiceStringAccountStringBoolReceivedArguments = (
            token: token,
            service: service,
            account: account
        )
        saveTokenAPITokenDataServiceStringAccountStringBoolReceivedInvocations.append((
            token: token,
            service: service,
            account: account
        ))
        if let saveTokenAPITokenDataServiceStringAccountStringBoolClosure {
            return saveTokenAPITokenDataServiceStringAccountStringBoolClosure(token, service, account)
        } else {
            return saveTokenAPITokenDataServiceStringAccountStringBoolReturnValue
        }
    }

    // MARK: - token

    var tokenServiceStringAccountStringAPITokenDataCallsCount = 0
    var tokenServiceStringAccountStringAPITokenDataCalled: Bool {
        tokenServiceStringAccountStringAPITokenDataCallsCount > 0
    }

    var tokenServiceStringAccountStringAPITokenDataReceivedArguments: (service: String, account: String)?
    var tokenServiceStringAccountStringAPITokenDataReceivedInvocations: [(service: String, account: String)] = []
    var tokenServiceStringAccountStringAPITokenDataReturnValue: APITokenData?
    var tokenServiceStringAccountStringAPITokenDataClosure: ((String, String) -> APITokenData?)?

    func token(service: String, account: String) -> APITokenData? {
        tokenServiceStringAccountStringAPITokenDataCallsCount += 1
        tokenServiceStringAccountStringAPITokenDataReceivedArguments = (service: service, account: account)
        tokenServiceStringAccountStringAPITokenDataReceivedInvocations.append((service: service, account: account))
        if let tokenServiceStringAccountStringAPITokenDataClosure {
            return tokenServiceStringAccountStringAPITokenDataClosure(service, account)
        } else {
            return tokenServiceStringAccountStringAPITokenDataReturnValue
        }
    }

    // MARK: - deleteToken

    var deleteTokenServiceStringAccountStringBoolCallsCount = 0
    var deleteTokenServiceStringAccountStringBoolCalled: Bool {
        deleteTokenServiceStringAccountStringBoolCallsCount > 0
    }

    var deleteTokenServiceStringAccountStringBoolReceivedArguments: (service: String, account: String)?
    var deleteTokenServiceStringAccountStringBoolReceivedInvocations: [(service: String, account: String)] = []
    var deleteTokenServiceStringAccountStringBoolReturnValue: Bool!
    var deleteTokenServiceStringAccountStringBoolClosure: ((String, String) -> Bool)?

    @discardableResult
    func deleteToken(service: String, account: String) -> Bool {
        deleteTokenServiceStringAccountStringBoolCallsCount += 1
        deleteTokenServiceStringAccountStringBoolReceivedArguments = (service: service, account: account)
        deleteTokenServiceStringAccountStringBoolReceivedInvocations.append((service: service, account: account))
        if let deleteTokenServiceStringAccountStringBoolClosure {
            return deleteTokenServiceStringAccountStringBoolClosure(service, account)
        } else {
            return deleteTokenServiceStringAccountStringBoolReturnValue
        }
    }
}

public class LoggerMock: Logger, @unchecked Sendable {
    public init() {}

    // MARK: - log

    public var logLevelLogLevelContextLogContextMessageStringVoidCallsCount = 0
    public var logLevelLogLevelContextLogContextMessageStringVoidCalled: Bool {
        logLevelLogLevelContextLogContextMessageStringVoidCallsCount > 0
    }

    public var logLevelLogLevelContextLogContextMessageStringVoidReceivedArguments: (
        level: LogLevel,
        context: LogContext,
        message: String
    )?
    public var logLevelLogLevelContextLogContextMessageStringVoidReceivedInvocations: [(
        level: LogLevel,
        context: LogContext,
        message: String
    )] = []
    public var logLevelLogLevelContextLogContextMessageStringVoidClosure: ((LogLevel, LogContext, String) -> Void)?

    public func log(_ level: LogLevel, _ context: LogContext, message: String) {
        logLevelLogLevelContextLogContextMessageStringVoidCallsCount += 1
        logLevelLogLevelContextLogContextMessageStringVoidReceivedArguments = (
            level: level,
            context: context,
            message: message
        )
        logLevelLogLevelContextLogContextMessageStringVoidReceivedInvocations.append((
            level: level,
            context: context,
            message: message
        ))
        logLevelLogLevelContextLogContextMessageStringVoidClosure?(level, context, message)
    }

    // MARK: - log

    public var logLevelLogLevelContextLogContextMessageStringErrorErrorVoidCallsCount = 0
    public var logLevelLogLevelContextLogContextMessageStringErrorErrorVoidCalled: Bool {
        logLevelLogLevelContextLogContextMessageStringErrorErrorVoidCallsCount > 0
    }

    public var logLevelLogLevelContextLogContextMessageStringErrorErrorVoidReceivedArguments: (
        level: LogLevel,
        context: LogContext,
        message: String,
        error: Error?
    )?
    public var logLevelLogLevelContextLogContextMessageStringErrorErrorVoidReceivedInvocations: [(
        level: LogLevel,
        context: LogContext,
        message: String,
        error: Error?
    )] = []
    public var logLevelLogLevelContextLogContextMessageStringErrorErrorVoidClosure: ((
        LogLevel,
        LogContext,
        String,
        Error?
    ) -> Void)?

    public func log(_ level: LogLevel, _ context: LogContext, message: String, error: Error?) {
        logLevelLogLevelContextLogContextMessageStringErrorErrorVoidCallsCount += 1
        logLevelLogLevelContextLogContextMessageStringErrorErrorVoidReceivedArguments = (
            level: level,
            context: context,
            message: message,
            error: error
        )
        logLevelLogLevelContextLogContextMessageStringErrorErrorVoidReceivedInvocations.append((
            level: level,
            context: context,
            message: message,
            error: error
        ))
        logLevelLogLevelContextLogContextMessageStringErrorErrorVoidClosure?(level, context, message, error)
    }
}

class RemoteConfigDataSourceMock: RemoteConfigDataSource {
    var fetched: Bool {
        get { underlyingFetched }
        set(value) { underlyingFetched = value }
    }

    var underlyingFetched: Bool!
    var featureFlags: InfoRemoteConfig?

    // MARK: - featureFlags

    var featureFlagsInfoRemoteConfigCallsCount = 0
    var featureFlagsInfoRemoteConfigCalled: Bool {
        featureFlagsInfoRemoteConfigCallsCount > 0
    }

    var featureFlagsInfoRemoteConfigReturnValue: InfoRemoteConfig?
    var featureFlagsInfoRemoteConfigClosure: (() async -> InfoRemoteConfig?)?

    func featureFlags() async -> InfoRemoteConfig? {
        featureFlagsInfoRemoteConfigCallsCount += 1
        if let featureFlagsInfoRemoteConfigClosure {
            return await featureFlagsInfoRemoteConfigClosure()
        } else {
            return featureFlagsInfoRemoteConfigReturnValue
        }
    }
}

public class UpdateAppUseCaseMock: UpdateAppUseCase {
    public init() {}

    // MARK: - invoke

    public var invokeAppUpdateThrowableError: (any Error)?
    public var invokeAppUpdateCallsCount = 0
    public var invokeAppUpdateCalled: Bool {
        invokeAppUpdateCallsCount > 0
    }

    public var invokeAppUpdateReturnValue: AppUpdate?
    public var invokeAppUpdateClosure: (() async throws -> AppUpdate?)?

    public func invoke() async throws -> AppUpdate? {
        invokeAppUpdateCallsCount += 1
        if let error = invokeAppUpdateThrowableError {
            throw error
        }
        if let invokeAppUpdateClosure {
            return try await invokeAppUpdateClosure()
        } else {
            return invokeAppUpdateReturnValue
        }
    }
}

class UserDefaultsDataSourceMock: UserDefaultsDataSource {
    var lastInstalledVersion: String?
}
