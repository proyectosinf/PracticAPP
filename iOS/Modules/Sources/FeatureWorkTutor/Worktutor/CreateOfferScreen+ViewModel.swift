import AppCommon
import Domain
import Factory
import SwiftUI

extension CreateOfferScreen {
    enum Navigation: Equatable {
        case close
    }

    enum Event {
        case onViewDidLoad
        case didNavigate
        case cancel
        case submit
    }

    struct UIState {
        var loading: Bool = false
        var alert: AlertUIState?
        var actionSheet: ActionSheetUIState?
        var navigation: Navigation?
        var degrees: [Degree] = []
        var degreeSearchText: String = ""
        var title: InputState<String> = .init(
            validator: .and(
                .mandatory(message: Strings.general.offer_mandatory_title_text),
                .maxLength(length: 100, message: Strings.general.register_max_length_name_surname_text)
            )
        )

        var description: InputState<String> = .init(
            validator: .and(.mandatory(message: Strings.general.offer_mandatory_description_text))
        )

        var vacancies: InputState<String> = .init(
            validator: .and(
                .mandatory(message: Strings.general.offer_mandatory_vacancy_text),
                .optional(.maxLength(length: 3, message: Strings.general.offer_max_length_vacances_text))
            )
        )

        var postalCode: InputState<String> = .init(
            validator: .and(
                .mandatory(message: Strings.general.offer_mandatory_cp_text),
                .maxLength(length: 5, message: Strings.general.offer_invalid_postal_code_text),
                .regex(#"^\d{5}$"#)
            )
        )

        var contactName: InputState<String> = .init(
            validator: .and(
                .mandatory(message: Strings.general.offer_mandatory_name_text),
                .maxLength(length: 100, message: Strings.general.register_max_length_name_surname_text)
            )
        )

        var contactEmail: InputState<String> = .init(
            validator: .and(
                .mandatory(message: Strings.general.common_mandatory_text, ignoreSpaces: true),
                .email(message: Strings.general.register_invalid_email_text)
            )
        )
        var startDate: InputState<Date> = .init(
            validator: .and(
                .mandatory(message: Strings.general.offer_required_dates_text)
            )
        )
        var endDate: InputState<Date> = .init(
            validator: .and(
                .mandatory(message: Strings.general.offer_required_dates_text)
            )
        )
        var address: InputState<String> = .init(
            validator: .mandatory(
                message: Strings.general.offer_mandatory_address_text
            )
        )
        var contactPhone: InputState<String> = .init(
            validator: .and(
                .optional(
                    .minLength(length: 9, message: Strings.general.offer_phone_format_text)
                ),
                .optional(
                    .maxLength(length: 9, message: Strings.general.offer_phone_format_text)
                )
            )
        )
        var selectedOfferType: PickerInputState<OfferTypeOption> = .init(
            validator: .and(
                .mandatory(message: Strings.general.offer_required_type_text)
            )
        )
        var selectedDegreeOption: PickerInputState<DegreeOption> = .init(
            validator: .and(
                .mandatory(message: Strings.general.offer_required_degree_text)
            )
        )
    }

    @Observable @MainActor
    final class ViewModel {
        var state: UIState
        var degreeRepository: DegreeRepository
        var offerRepository: OfferRepository
        var companyrepo: CompanyRepository
        var authRepository: AuthRepository
        // dependencies here...

        init(container: Container = .shared) {
            state = .init()
            degreeRepository = container.degreesRepository()
            offerRepository = container.offerRepository()
            companyrepo = container.companyRepository()
            authRepository = container.authRepository()
            // initialize dependencies from container here...
            loadData()
        }

        var degreeOptions: [DegreeOption] {
            state.degrees.map { DegreeOption(degree: $0) }
        }
        var filteredDegreeOptions: [DegreeOption] {
            if state.degreeSearchText.isEmpty {
                return degreeOptions
            } else {
                return degreeOptions.filter {
                    $0.pickerOptionRepresentation.localizedCaseInsensitiveContains(state.degreeSearchText)
                }
            }
        }
        var offerTypeOptions: [OfferTypeOption] {
            state.selectedOfferType.options
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
            }
        }

        private func validate() -> Bool {
            let isValid = [
                state.title.validate(),
                state.description.validate(),
                state.vacancies.validate(),
                state.address.validate(),
                state.postalCode.validate(),
                state.contactName.validate(),
                state.contactEmail.validate()
            ].allSatisfy { $0 }

            guard isValid else {
                state.alert = .init(
                    title: Strings.general.offer_mandatory_fields_text,
                    message: Strings.general.offer_fields_selected_text
                )
                return false
            }
            guard state.selectedOfferType.value != nil else {
                state.alert = .init(
                    title: Strings.general.offer_required_type_text,
                    message: Strings.general.offer_select_type_offer_text
                )
                return false
            }
            guard state.selectedDegreeOption.value != nil else {
                state.alert = .init(
                    title: Strings.general.offer_required_degree_text,
                    message: Strings.general.offer_select_degree_text
                )
                return false
            }
            guard let startDate = state.startDate.value,
                  let endDate = state.endDate.value else {
                state.alert = .init(
                    title: Strings.general.offer_required_dates_text,
                    message: Strings.general.offer_select_dates_text
                )
                return false
            }
            guard startDate <= endDate else {
                state.alert = .init(
                    title: Strings.general.offer_invalid_date_text,
                    message: Strings.general.offer_error_dates_text
                )
                return false
            }
            return true
        }
        func submit() {
            guard validate() else { return }

            guard let type = state.selectedOfferType.value?.type,
                  let degreeId = state.selectedDegreeOption.value?.degree.id,
                  let startDate = state.startDate.value,
                  let endDate = state.endDate.value else {
                return
            }

            let formatter = DateFormatter()
            formatter.dateFormat = "yyyy-MM-dd"

            guard
                let title = state.title.value,
                let description = state.description.value,
                let vacanciesStr = state.vacancies.value,
                let vacancies = Int(vacanciesStr),
                let address = state.address.value,
                let postalCode = state.postalCode.value,
                let contactName = state.contactName.value,
                let contactEmail = state.contactEmail.value
            else {
                return
            }
            let contactPhone = state.contactPhone.value
            let offer = CreateOfferRequestParams(
                title: title,
                description: description,
                vacanciesNumber: vacancies,
                startDate: formatter.string(from: startDate),
                endDate: formatter.string(from: endDate),
                type: type.rawValue,
                address: address,
                postalCode: postalCode,
                contactName: contactName,
                contactEmail: contactEmail,
                contactPhone: contactPhone,
                degreeId: degreeId
            )

            Task {
                state.loading = true
                defer { state.loading = false }

                do {
                    _ = try await companyrepo.getCompany()
                    try await offerRepository.createOffer(offer)
                    self.state.navigation = .close
                } catch {
                    state.alert = .init(error: error)
                }
            }
        }

        func loadData() {
            Task {
                defer { state.loading = false }
                state.loading = true
                do {
                    let degrees = try await degreeRepository.getDegrees()
                    state.degrees = degrees
                    state.selectedDegreeOption = PickerInputState(
                        value: nil,
                        options: degrees.map { DegreeOption(degree: $0) }
                    )
                    state.selectedOfferType = PickerInputState(
                        value: nil,
                        options: OfferType.allCases.map { OfferTypeOption(type: $0) }
                    )
                } catch {
                    state.alert = .init(error: error)
                }
            }
        }
    }
}
