import Domain
import Foundation

public struct AlertUIState: Identifiable, Sendable {
    public let id = UUID()
    public let title: String
    public let message: String?
    public let actions: [AlertAction]
}
public extension AlertUIState {
    init(title: String, message: String? = nil, actions: [AlertAction]? = nil) {
        self.title = title
        self.message = message
        self.actions = actions ?? []
    }
    init(title: String, message: String? = nil, action: AlertAction) {
        self.init(title: title, message: message, actions: [action])
    }
    static var defaultError: Self {
        .init(
            title: Strings.general.common_error_text,
            message: Strings.general.common_default_server_error_text,
            action: .default(title: Strings.general.common_accept_text)
        )
    }
    init(error: Error) {
        var errorMessage = Strings.general.common_default_server_error_text
        if let appError = error as? AppError {
            switch appError {
            case .serverError(let serverError):
                if let message = serverError?.message?.emptyToNil {
                    errorMessage = message
                } else if let detail = serverError?.detail?.emptyToNil {
                    errorMessage = detail
                }
            case .authenticationError(let msg),
                    .unknown(let msg):
                errorMessage = msg
            case .networkError:
                errorMessage = Strings.general.error_no_internet_text
            case .sessionExpired:
                errorMessage = Strings.general.error_session_expired_text
            case .internalError(let errorIn):
                errorMessage = errorIn?.localizedDescription ?? errorMessage
            case .validation(let reason):
                switch reason {
                case .companyNameExists:
                    errorMessage = Strings.general.error_if_exist_name_text
                case .companyCifExists:
                    errorMessage = Strings.general.error_if_exist_cif_text
                case .invalidData:
                    errorMessage = Strings.general.error_if_invalid_date_text
                }
            }
        }
        let title = Strings.general.common_error_text
        let action = AlertAction.default(title: Strings.general.common_accept_text)
        self.init(title: title, message: errorMessage, actions: [action])
    }
}
