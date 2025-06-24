import Foundation
import UIKit

// sourcery: AutoMockable
public protocol CompanyRepository: Sendable {
    func getCompany() async throws -> Company
    func registerCompany(_ params: RegisterCompanyRequestParams) async throws
    func uploadCompanyImage(_ image: UIImage) async throws
}
