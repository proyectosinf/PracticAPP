import Domain
import UIKit
import Factory
import Foundation

public final class CompanyRepositoryImpl: CompanyRepository, Sendable {
    private let firebaseAuthDataSource: FirebaseAuthDataSource
    private let companyDataSource: CompanyDataSource

    public init(container: Container = .shared) {
        self.firebaseAuthDataSource = container.firebaseAuthDataSource()
        self.companyDataSource = container.companyDataSource()
    }

    public func getCompany() async throws -> Company {
        let token = try await firebaseAuthDataSource.currentToken()
        let companyData = try await companyDataSource.getCompany(authToken: token)
        return companyData.toDomain()
    }
    public func registerCompany(_ company: RegisterCompanyRequestParams) async throws {
        let token = try await firebaseAuthDataSource.currentToken()
        try await companyDataSource.registerCompany(company, authToken: token)
    }
    public func uploadCompanyImage(_ image: UIImage) async throws {
        let token = try await firebaseAuthDataSource.currentToken()
        try await companyDataSource.uploadImage(image, authToken: token, model: 3)
    }
}
