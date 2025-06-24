import AppCommon
import Domain
import Factory
import SwiftUI

extension RegisterCompanyScreen {
    enum Navigation: Equatable {
        case close
        case goToHome
        case goToLogin
    }

    enum Event {
        case onViewDidLoad
        case didNavigate
        case cancel
        case submit
        case logout
        case logoutConfirmationRequested
    }

    struct UIState {
        var name: InputState<String> = .init(
            value: "",
            validator: .and(
                .mandatory(message: Strings.general.register_name_label_text),
                .maxLength(length: 100, message: Strings.general.register_max_length_name_surname_text)
            )
        )

        var sector: InputState<String> = .init(
            value: "",
            validator: .mandatory(message: Strings.general.common_mandatory_text)
        )

        var web: InputState<String> = .init(
            value: "",
            validator: .optional(.url(message: Strings.general.company_url_not_valid_text))
        )
        var cif: InputState<String> = .init(
            value: "",
            validator: .and(
                .mandatory(message: Strings.general.common_mandatory_text),
                .cif(message: Strings.general.company_cif_valid_format_text)
            )
        )
        var logo: InputState<String> = .init()
        var loading: Bool = false
        var alert: AlertUIState?
        var actionSheet: ActionSheetUIState?
        var navigation: Navigation?
    }

    @Observable @MainActor
    final class ViewModel {
        var state: UIState
        private var companyRepository: CompanyRepository
        private var authRepository: AuthRepository
        @Injected(\.userSession) @ObservationIgnored private var userSession
        // dependencies here...
        init(container: Container = .shared) {
            state = .init()
            companyRepository = container.companyRepository()
            authRepository = container.authRepository()
            // initialize dependencies from container here...
        }
        func handle(_ event: Event) {
            switch event {
            case .onViewDidLoad:
                loadData()
            case .didNavigate:
                state.navigation = nil
            case .cancel:
                state.navigation = .close
            case .submit:
                submit()
            case .logout:
                logout()
            case .logoutConfirmationRequested:
                state.actionSheet = ActionSheetUIState(
                    title: Strings.general.company_logout_text_text,
                    message: Strings.general.company_areyousure_text,
                    actions: [
                        .destructive(title: Strings.general.company_logout_text_text, action: { self.logout() }),
                        .cancel()
                    ]
                )
            }
        }
        private func validate() -> Bool {
            return [
                state.name.validate(),
                state.sector.validate(),
                state.cif.validate(),
                state.web.validate()
            ].allSatisfy { $0 }
        }
        func submit() {
            guard validate() else {
                state.alert = .init(
                    title: Strings.general.company_incomplete_fields_text,
                    message: Strings.general.company_mandatory_fields_text
                )
                return
            }
            Task {
                state.loading = true
                defer { state.loading = false }
                do {
                    let company = RegisterCompanyRequestParams(
                        name: state.name.value ?? "",
                        sector: state.sector.value ?? "",
                        web: state.web.value?.isEmpty == true ? nil : state.web.value,
                        cif: state.cif.value ?? "",
                        logo: nil
                    )
                    try await companyRepository.registerCompany(company)
                    let updatedUser = try await authRepository.fetchCurrentUser()
                    userSession.set(user: updatedUser)

                    self.state.navigation = .goToHome
                } catch let error as AppError {
                    state.alert = .init(
                        title: Strings.general.common_error_text,
                        message: error.message
                    )
                } catch {
                    state.alert = .init(
                        title: Strings.general.common_error_text,
                        message: Strings.general.common_error_text
                    )
                }
            }
        }

        func loadData() {
            /*
             Task {
             defer { state.loading = false }
             state.loading = true
             do {
             // Call to your use case or repository here...
             } catch {
             // Handle error...
             state.alert = .init(error: error)
             }
             }
             */
        }
        private func logout() {
            Task {
                try? await authRepository.signOut()
                userSession.clear()
                state.navigation = .goToLogin
            }
        }
    }
}
