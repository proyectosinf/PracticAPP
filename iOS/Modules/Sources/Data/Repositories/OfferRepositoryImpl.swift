import Domain
import FirebaseAuth
import Factory
import Foundation

public final class OfferRepositoryImpl: OfferRepository, Sendable {
    private let firebaseAuthDataSource: FirebaseAuthDataSource
    private let offerDataSource: OfferDataSource

    public init(container: Container = .shared) {
        self.firebaseAuthDataSource = container.firebaseAuthDataSource()
        self.offerDataSource = container.offerDataSource()
    }
    public func createOffer(_ params: Domain.CreateOfferRequestParams) async throws {
        guard let token = try await Auth.auth().currentUser?.getIDToken(forcingRefresh: true) else {
            throw AppError.authenticationError("No se pudo obtener el token actualizado.")
        }
        let dataModel = CreateOfferRequestParams(from: params)
        try await offerDataSource.createOffer(dataModel, authToken: token)
    }
    public func getOffers(page: Int) async throws -> PaginatedOffers {
        let token = try await firebaseAuthDataSource.currentToken()
        let response = try await offerDataSource.getOffers(authToken: token, page: page)
        let domainOffers = response.items.map { $0.toDomain() }
        return PaginatedOffers(items: domainOffers, total: response.total)
    }
    public func getOfferDetail(id: Int) async throws -> Domain.Offer {
        let token = try await firebaseAuthDataSource.currentToken()
        return try await offerDataSource.getOfferDetail(id: id, token: token)
    }
}
