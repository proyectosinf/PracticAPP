import Data
import Domain
import Factory
import Foundation

public extension Container {
    var appSettingsRepository: Factory<AppSettingsRepository> {
        self { AppSettingsRepositoryImpl() }.cached
    }

    var authRepository: Factory<AuthRepository> {
        self { AuthRepositoryImpl() }.cached
    }

    var userRepository: Factory<UserRepository> {
        self { UserRepositoryImpl() }
    }
    var companyRepository: Factory<CompanyRepository> {
        self { CompanyRepositoryImpl() }.cached
    }

    var degreesRepository: Factory<DegreeRepository> {
        self { DegreeRepositoryImpl() }.cached
    }
    var offerRepository: Factory<OfferRepository> {
        self { OfferRepositoryImpl() }.cached
    }

    var userSession: Factory<UserSession> {
            self { UserSession() }.singleton
        }
    var candidacyRepository: Factory<CandidacyRepository> {
        self { CandidacyRepositoryImpl() }.cached
    }
    var registerUseCase: Factory<RegisterUseCase> {
        self {
            RegisterUseCaseImpl(
                authRepository: self.authRepository(),
                userRepository: self.userRepository()
            )
        }
    }

}
