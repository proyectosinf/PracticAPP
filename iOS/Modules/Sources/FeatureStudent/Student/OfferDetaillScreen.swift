import SwiftUI
import Combine
import AppCommon

public struct OfferDetaillScreen: View {
    @Environment(\.dismiss) private var dismiss
    @Environment(\.offerDetailCoordinator) private var coordinator: OfferDetailCoordinator?
    @State private var viewModel: ViewModel
    private let didCreateCandidacy: PassthroughSubject<Int, Never>
    public init(offerId: Int, didCreateCandidacy: PassthroughSubject<Int, Never>) {
        self.didCreateCandidacy = didCreateCandidacy
        _viewModel = .init(wrappedValue: .init(offerId: offerId))
    }
    public var body: some View {
        Content(state: $viewModel.state) { event in
            viewModel.handle(event)
        }
        .alert(model: $viewModel.state.alert)
        .onChange(of: viewModel.state.navigation) { _, nav in
            guard let nav else { return }
            switch nav {
            case .close:
                dismiss()
            case .closeIfRegister:
                didCreateCandidacy.send(viewModel.offerId)
                dismiss()
            }
        }
    }
    struct Content: View {
        @Binding var state: UIState
        let event: (Event) -> Void
        var body: some View {
            ZStack(alignment: .top) {
                if state.loading {
                    ProgressView(Strings.general.common_loading_text)
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                } else if let offer = state.offer {
                    GeometryReader { geometry in
                        let frameWidth = geometry.size.width
                        let frameHeight = geometry.size.height
                        let trimmedUrl = (offer.companyPhoto ?? "").trimmingCharacters(in: .whitespacesAndNewlines)
                        if trimmedUrl.isEmpty {
                            Image("practicapp")
                                .resizable()
                                .scaledToFill()
                                .frame(width: frameWidth, height: frameHeight)
                                .clipped()
                        } else {
                            RemoteImage(url: trimmedUrl, contentMode: .fill)
                                .frame(width: frameWidth, height: frameHeight)
                                .aspectRatio(contentMode: .fill)
                                .clipped()
                        }
                    }
                    .frame(height: UIScreen.main.bounds.height * 0.25)
                    VStack(spacing: 0) {
                        ScrollView {
                            VStack(spacing: 16) {
                                VStack(alignment: .leading, spacing: 8) {
                                    Text(offer.title)
                                        .font(.title)
                                        .fontWeight(.bold)
                                        .padding(.bottom, 4)
                                    Text(offer.company)
                                        .font(.title3)
                                        .fontWeight(.bold)
                                        .padding(.bottom, 4)
                                    infoRow(title: Strings.general.offer_list_formation_text, value: offer.degree)
                                    infoRow(
                                        title: Strings.general.offer_list_date_offer_text,
                                        value: "\(FormattedDate.format(offer.startDate)) - " +
                                        "\(FormattedDate.format(offer.endDate))"
                                    )
                                    infoRow(
                                        title: Strings.general.offer_list_modality_text,
                                        value: offer.type.description
                                    )
                                    Divider()
                                    VStack(alignment: .leading, spacing: 4) {
                                        Text(Strings.general.offer_description_text)
                                            .bold()
                                        Text(offer.description)
                                            .font(.body)
                                            .multilineTextAlignment(.leading)
                                    }
                                    Divider()
                                    infoRow(
                                        title: Strings.general.offer_list_vacancies_text,
                                        value: "\(offer.vacanciesNumber)"
                                    )
                                    infoRow(title: Strings.general.offer_list_watchers_text, value: "\(offer.views)")
                                    Divider()
                                    infoRow(title: Strings.general.offer_list_contact_text, value: offer.contactName)
                                    infoRow(title: Strings.general.offer_list_mail_text, value: offer.contactEmail)
                                    if let phone = offer.contactPhone, !phone.isEmpty {
                                        infoRow(title: Strings.general.offer_list_phone_text, value: phone)
                                    }
                                    infoRow(title: Strings.general.offer_list_address_text, value: offer.address)
                                    infoRow(title: Strings.general.offer_list_postal_code_text, value: offer.postalCode)
                                    if offer.inscribe == false {
                                        Divider()
                                        VStack(alignment: .leading, spacing: 12) {
                                            Text(Strings.general.candidacy_presentation_card_text)
                                                .font(.headline)
                                            TextField(
                                                Strings.general.candidacy_type_presentation_card_text,
                                                text: $state.presentationCard, axis: .vertical
                                            )
                                            .lineLimit(5...)
                                            .padding()
                                            .overlay(
                                                RoundedRectangle(cornerRadius: 8).stroke(Color.gray.opacity(0.5))
                                            )
                                        }
                                    }
                                }
                                .padding()
                                .background(Color(.systemBackground))
                                .cornerRadius(16)
                                .shadow(radius: 5)
                                .padding(.horizontal, 10)
                                .offset(y: -20)
                            }
                            .padding(.top, UIScreen.main.bounds.height * 0.20)
                        }
                        if offer.inscribe == false {
                            Button(Strings.general.offer_list_register_text) {
                                event(.submit)
                            }
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(Color.dsPrimary)
                            .foregroundColor(.white)
                            .cornerRadius(10)
                            .padding(.horizontal)
                            .padding(.bottom, 16)
                        } else {
                            Label(Strings.general.offer_list_inscribe_text, systemImage: "checkmark.seal.fill")
                                .foregroundColor(.green)
                                .font(.subheadline)
                                .padding(.bottom, 16)
                        }
                    }
                } else {
                    Text(Strings.general.common_loading_text)
                        .foregroundColor(.secondary)
                        .padding()
                }
            }
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(Strings.general.common_cancel_text) {
                        event(.cancel)
                    }
                }
            }
            .onViewDidLoad { event(.onViewDidLoad) }
        }
    }
}
    private func infoRow(title: String, value: String) -> some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(title)
                .font(.subheadline)
                .fontWeight(.bold)
            Text(value)
                .font(.body)
                .multilineTextAlignment(.leading)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
