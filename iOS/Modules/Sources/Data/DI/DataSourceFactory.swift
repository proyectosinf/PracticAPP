import Domain
import Factory
import Foundation

extension Container {
    public var environment: Factory<APIEnvironment> {
        self { .production }
    }

    var appInfoDataSource: Factory<AppInfoDataSource> {
        self { AppInfoDataSourceImpl() }
    }

    var authAPIDataSource: Factory<AuthAPIDataSource> {
        self { AuthAPIDataSourceImpl() }
    }

    var userDefaultsDataSource: Factory<UserDefaultsDataSource> {
        self { UserDefaultsDataSourceImpl() }
    }

    var keychainDataSource: Factory<KeychainDataSource> {
        self { KeychainDataSourceImpl() }.singleton
    }

    var cacheDataSource: Factory<CacheDataSource> {
        self { CacheDataSourceImpl() }.singleton
    }

    var firebaseRemoteConfigDataSource: Factory<RemoteConfigDataSource> {
        self { RemoteConfigDataSourceImpl() }.cached
    }

    var apiService: Factory<APIService> {
        self {
            APIService(baseURL: "https://fct25-backend.onrender.com", session: URLSession.shared)
        }.singleton
    }

    var privateSession: Factory<URLSession> {
        self {
            let configuration = URLSessionConfiguration.default
            configuration.httpAdditionalHeaders = self.authAPI().httpAdditionalHeaders
            return URLSession(configuration: configuration)
        }.scope(.session)
    }

    var publicSession: Factory<URLSession> {
        self { URLSession(configuration: .default) }.cached
    }

    var firebaseAuthDataSource: Factory<FirebaseAuthDataSource> {
        self { FirebaseAuthDataSourceImpl() }
    }

    var userDataSource: Factory<UserDataSource> {
        self { UserDataSourceImpl(apiService: self.apiService()) }
    }
    var companyDataSource: Factory<CompanyDataSource> {
            self { CompanyDataSourceImpl() }
        }
    var candidacyDataSource: Factory<CandidacyDataSource> {
        self { CandidacyDataSourceImpl() }
    }
    var sampleAPI: Factory<SampleAPI> {
        self {
            SampleAPI(
                apiService: APIService(
                    baseURL: self.environment().apiDomain,
                    session: self.privateSession(),
                    interceptors: [APILogger(), self.authAPI()]
                )
            )
        }
    }
    var degreeDataSource: Factory<DegreeDataSource> {
        self { DegreeDataSourceImpl(apiService: self.apiService()) }
    }
    var offerDataSource: Factory<OfferDataSource> {
        self { OfferDataSourceImpl(apiService: self.apiService()) }
    }
    var authAPI: Factory<AuthAPI> {
        self {
            AuthAPI(
                apiService: APIService(
                    baseURL: self.environment().apiDomain,
                    session: self.publicSession(),
                    interceptors: [APILogger()]
                )
            )
        }
    }
}

extension Scope {
    static let session = Cached()
}
