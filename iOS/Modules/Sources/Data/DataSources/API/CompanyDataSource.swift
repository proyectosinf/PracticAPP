import Foundation
import UIKit
import Domain
import Factory

// sourcery: AutoMockable
protocol CompanyDataSource: Sendable {
    func getCompany(authToken: String) async throws -> CompanyData
    func registerCompany(_ params: RegisterCompanyRequestParams, authToken: String) async throws
    func uploadImage(_ image: UIImage, authToken: String, model: Int) async throws
}

final class CompanyDataSourceImpl: CompanyDataSource {
    private let apiService: APIService

    public init(apiService: APIService = Container.shared.apiService()) {
        self.apiService = apiService
    }

    public func getCompany(authToken: String) async throws -> CompanyData {
        let response = try await apiService.get(
            "/api/v1/companies/get_current_user_company",
            query: nil,
            headers: ["Authorization": "Bearer \(authToken)"]
        )
        try response.validate()
        return try response.decoded()
    }

    func registerCompany(_ params: RegisterCompanyRequestParams, authToken: String) async throws {
        do {
            _ = try await apiService.post(
                "/api/v1/companies/",
                body: params,
                headers: ["Authorization": "Bearer \(authToken)"]
            ).validate()
        } catch let error as APIErrorData {
            if error.apiResponse != nil {
            }
            throw error.toRegisterCompanyDomainError
        }
    }
    func uploadImage(_ image: UIImage, authToken: String, model: Int) async throws {
        guard let imageData = image.jpegData(compressionQuality: 0.8) else {
            throw AppError.validation(.invalidData)
        }

        guard var components = URLComponents(
            string: Container.shared.apiService().baseURL
            + "/api/v1/firebase/upload-image"
        )
        else {
            throw AppError.validation(.invalidData)
        }

        components.queryItems = [URLQueryItem(name: "model", value: "\(model)")]

        guard let url = components.url else {
            throw AppError.validation(.invalidData)
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("Bearer \(authToken)", forHTTPHeaderField: "Authorization")

        let boundary = UUID().uuidString
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")

        var body = Data()

        func append(_ string: String) {
            body.append(Data(string.utf8))
        }

        append("--\(boundary)\r\n")
        append("Content-Disposition: form-data; name=\"file\"; filename=\"logo.jpg\"\r\n")
        append("Content-Type: image/jpeg\r\n\r\n")
        body.append(imageData)
        append("\r\n")
        append("--\(boundary)--\r\n")

        request.httpBody = body

        let (data, response) = try await URLSession.shared.data(for: request)

        guard let httpResponse = response as? HTTPURLResponse,
              (200..<300).contains(httpResponse.statusCode) else {
            let responseString = String(data: data, encoding: .utf8) ?? "Respuesta vacía"
            throw NSError(domain: "Upload", code: 1, userInfo: [NSLocalizedDescriptionKey: responseString])
        }
    }
}
