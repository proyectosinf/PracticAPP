import AppCommon
import Combine
import Data
import Domain
import Factory
import FoundationUtils
import SwiftUI
import UIKit

struct CreateOfferScreen: View {
    @State private var viewModel: ViewModel
    @Environment(\.createOfferCoordinator) private var coordinator: CreateOfferCoordinator?
    private let didCreateOffer: PassthroughSubject<Void, Never>

    init(didCreateOffer: PassthroughSubject<Void, Never>) {
        self.didCreateOffer = didCreateOffer
        _viewModel = .init(wrappedValue: .init())
    }

    var body: some View {
        Content(
            state: $viewModel.state,
            event: { viewModel.handle($0) },
            filteredDegreeOptions: viewModel.filteredDegreeOptions,
            offerTypeOptions: viewModel.offerTypeOptions
        )
        .alert(model: $viewModel.state.alert)
        .actionSheet(model: $viewModel.state.actionSheet)
        .onChange(of: viewModel.state.navigation) { _, newValue in
            guard let newValue else { return }
            switch newValue {
            case .close:
                didCreateOffer.send(())
                coordinator?.dismiss()
            }
            viewModel.handle(.didNavigate)
        }
    }

    struct Content: View {
        @Binding var state: UIState
        let event: (Event) -> Void
        let filteredDegreeOptions: [DegreeOption]
        let offerTypeOptions: [OfferTypeOption]

        var body: some View {
            ScrollView {
                VStack(spacing: .spacingXL) {
                    offerDetailsSection
                    contactDetailsSection
                }
                .padding()
            }
            .navigationTitle(Strings.general.offer_create_offer_text)
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button {
                        event(.cancel)
                    } label: {
                        Image(systemName: "chevron.backward")
                            .font(.body.weight(.medium))
                    }
                }
            }
        }

        private var offerDetailsSection: some View {
            VStack(spacing: .spacingL) {
                sectionTitle(Strings.general.offer_offer_detaill_text)

                TextFieldInput(
                    title: "\(Strings.general.offer_title_text) \(Strings.general.common_mandatory_placeholder_text)",
                    state: $state.title,
                    placeholder: "",
                    style: .outlined(.gray)
                )

                TextFieldInput(
                    title: "\(Strings.general.offer_description_text) " +
                           Strings.general.common_mandatory_placeholder_text,
                    state: $state.description,
                    placeholder: Strings.general.offer_description_offer_text,
                    style: .outlined(.gray)
                )

                TextFieldInput(
                    title: "\(Strings.general.offer_vacances_text) " +
                           Strings.general.common_mandatory_placeholder_text,
                    state: $state.vacancies,
                    placeholder: Strings.general.offer_example_vacancy_text,
                    style: .outlined(.gray)
                )
                .keyboardType(.numberPad)

                DatePickerInput(
                    title: Strings.general.offer_start_date_text,
                    state: $state.startDate,
                    displayedComponents: .date,
                    in: Date()...
                )

                DatePickerInput(
                    title: Strings.general.offer_end_date_text,
                    state: $state.endDate,
                    displayedComponents: .date,
                    in: (state.startDate.value ?? Date())...
                )

                Menu {
                    ForEach(offerTypeOptions, id: \.self) { option in
                        Button(option.pickerOptionRepresentation) {
                            state.selectedOfferType.value = option
                        }
                    }
                } label: {
                    HStack {
                        Text(Strings.general.offer_offer_type_text)
                            .foregroundColor(.primary)
                        Spacer()
                        Text(
                            state.selectedOfferType.value?.pickerOptionRepresentation
                            ?? Strings.general.offer_select_text
                        )
                        .foregroundColor(.primary)
                        Image(systemName: "chevron.down")
                            .foregroundStyle(.secondary)
                    }
                    .contentShape(Rectangle())
                    .padding(.vertical, 8)
                }

                VStack(alignment: .leading, spacing: .spacingXS) {
                    HStack(spacing: 2) {
                        PickerInput(
                            title: Strings.general.offer_formation_text,
                            state: $state.selectedDegreeOption,
                            placeholder: Strings.general.offer_select_degree_text,
                            canSearch: true
                        )
                        .fixedSize(horizontal: false, vertical: false)

                        Image(systemName: "chevron.right")
                            .foregroundStyle(.secondary)
                            .font(.subheadline)
                    }
                }
                .padding(.top, .spacingS)
                .contentShape(Rectangle())
            }
        }

        private var contactDetailsSection: some View {
            VStack(spacing: .spacingL) {
                sectionTitle(Strings.general.offer_contact_detaill_text)

                TextFieldInput(
                    title: "\(Strings.general.offer_addrees_text) " +
                           Strings.general.common_mandatory_placeholder_text,
                    state: $state.address,
                    placeholder: "",
                    style: .outlined(.gray)
                )

                TextFieldInput(
                    title: "\(Strings.general.offer_postal_code_text) " +
                           Strings.general.common_mandatory_placeholder_text,
                    state: $state.postalCode,
                    placeholder: Strings.general.offer_example_cp_text,
                    style: .outlined(.gray)
                )

                TextFieldInput(
                    title: "\(Strings.general.offer_contact_name_text) " +
                           Strings.general.common_mandatory_placeholder_text,
                    state: $state.contactName,
                    placeholder: "",
                    style: .outlined(.gray)
                )

                TextFieldInput(
                    title: "\(Strings.general.offer_contact_email_text) " +
                           Strings.general.common_mandatory_placeholder_text,
                    state: $state.contactEmail,
                    placeholder: Strings.general.offer_example_email_text,
                    style: .outlined(.gray)
                )
                .keyboardType(.emailAddress)

                TextFieldInput(
                    title: Strings.general.offer_phone_text,
                    state: $state.contactPhone,
                    placeholder: Strings.general.offer_optional_text,
                    style: .outlined(.gray)
                )
                .keyboardType(.phonePad)

                Button(Strings.general.offer_create_offer_text) {
                    event(.submit)
                }
                .buttonStyle(.primary)
                .padding(.horizontal, 20)
                .disabled(state.loading)
            }
        }

        private func sectionTitle(_ title: String) -> some View {
            Text(title)
                .font(.footnote)
                .foregroundColor(.blue)
                .frame(maxWidth: .infinity, alignment: .leading)
                .textCase(.uppercase)
                .padding(.horizontal)
        }
    }
}
