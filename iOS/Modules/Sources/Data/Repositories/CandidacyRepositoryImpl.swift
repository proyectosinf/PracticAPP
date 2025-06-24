import Domain
import Factory
import Foundation

public final class CandidacyRepositoryImpl: CandidacyRepository, Sendable {

    private let candidacyDataSource: CandidacyDataSource
    private let firebaseDataSource: FirebaseAuthDataSource

    public init(container: Container = .shared) {
        self.firebaseDataSource = container.firebaseAuthDataSource()
        self.candidacyDataSource = container.candidacyDataSource()
    }

    public func createCandidacy(_ params: CandidacyRequestParams) async throws {
        let token = try await firebaseDataSource.currentToken()
        try await candidacyDataSource.createCandidacy(params, authToken: token)
    }
    public func getCandidacies(id: Int, page: Int) async throws -> PaginatedCandidacies {
        let token = try await firebaseDataSource.currentToken()
        let response = try await candidacyDataSource.getCandidacies(id: id, token: token, page: page)
        let domainItems = response.items.map { $0.toDomain() }
        return PaginatedCandidacies(items: domainItems, total: response.total)
    }
    public func getMeAllCandidacies(page: Int) async throws -> PaginatedCandidacies {
        let token = try await firebaseDataSource.currentToken()
        let response = try await candidacyDataSource.getMeAllCandidacies(token: token, page: page)
        let domainItems = response.items.map { $0.toDomain() }
        return PaginatedCandidacies(items: domainItems, total: response.total)
    }
    public func getCandidacyById(id: Int) async throws -> Domain.Candidacy {
        let token = try await firebaseDataSource.currentToken()
        return try await candidacyDataSource.getCandidacyById(token: token, id: id)
    }
    public func updateCandidacyState(id: Int, request: UpdateCandidacyStateRequest) async throws {
        let token = try await firebaseDataSource.currentToken()
        try await candidacyDataSource.updateCandidacyState(id: id, body: request.toData(), token: token)
    }
}
