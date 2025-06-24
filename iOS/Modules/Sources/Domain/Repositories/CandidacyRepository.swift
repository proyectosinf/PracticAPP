import Foundation

// sourcery: AutoMockable
public protocol CandidacyRepository: Sendable {
    func createCandidacy(_ params: CandidacyRequestParams) async throws
    func getCandidacies(id: Int, page: Int) async throws -> PaginatedCandidacies
    func getMeAllCandidacies(page: Int) async throws -> PaginatedCandidacies
    func getCandidacyById(id: Int) async throws -> Candidacy
    func updateCandidacyState(id: Int, request: UpdateCandidacyStateRequest) async throws

}
