import AppCommon
import Combine
import Data
import Factory
import FoundationUtils
import SwiftUI

struct WorkTutorOffersScreen: View {
    @State private var viewModel: ViewModel
    @Environment(\..worktutorOffersCoordinator) private var coordinator: WorkTutorOffersCoordinator?
    private let didCreateOffer: AnyPublisher<Void, Never>

    init(didCreateOffer: AnyPublisher<Void, Never>) {
            self.didCreateOffer = didCreateOffer
            _viewModel = .init(wrappedValue: .init())
        }

    var body: some View {
        Content(state: $viewModel.state, viewModel: viewModel) { event in
            viewModel.handle(event)
        }
        .alert(model: $viewModel.state.alert)
        .onReceive(didCreateOffer) { _ in
            viewModel.loadData(reset: true)
        }
        .actionSheet(model: $viewModel.state.actionSheet)
        .onChange(of: viewModel.state.navigation) { _, newValue in
            guard let newValue else { return }
            switch newValue {
            case .close:
                coordinator?.dismiss()
            case .goToCreateOffer:
                coordinator?.presentSheet(.createOffers)
            case .goToCandidacies(let offer):
                coordinator?.presentFullScreen(.candidacies(offer: offer))
            }
            viewModel.handle(.didNavigate)
        }
    }

    struct Content: View {
        @Binding var state: UIState
        let viewModel: ViewModel
        let event: (Event) -> Void
        @State private var selectedFilter: OfferStatus = .open

        private var filteredOffers: [OfferUIModel] {
            switch selectedFilter {
            case .open:
                return viewModel.offers.filter { $0.isActive }
            case .closed:
                return viewModel.offers.filter { !$0.isActive }
            }
        }

        var body: some View {
            let offers = filteredOffers
            ZStack(alignment: .bottomTrailing) {
                Color.gray.opacity(0.1)
                    .ignoresSafeArea()

                VStack(spacing: .spacingM) {
                    filterSection
                    offerListSection(offers: offers)
                }
                addButton
            }
            .onViewDidLoad { event(.onViewDidLoad) }
            .navigationTitle(Strings.general.offer_list_my_offers_text)
            .navigationBarTitleDisplayMode(.inline)
        }

        private var filterSection: some View {
            HStack(spacing: 12) {
                FilterButton(
                    title: Strings.general.offer_list_open_text,
                    isSelected: selectedFilter == .open
                ) {
                    selectedFilter = .open
                }

                FilterButton(
                    title: Strings.general.offer_list_close_text,
                    isSelected: selectedFilter == .closed
                ) {
                    selectedFilter = .closed
                }
            }
            .padding(.horizontal)
        }

        private func offerListSection(offers: [OfferUIModel]) -> some View {
            ScrollView {
                LazyVStack(spacing: 12) {
                    if viewModel.hasLoaded && offers.isEmpty {
                        emptyState
                    } else {
                        ForEach(offers) { offer in
                            OfferCardView(offer: offer)
                                .onAppear {
                                    if offer.id == offers.last?.id {
                                        event(.loadMore)
                                    }
                                }
                                .onTapGesture {
                                    event(.candidacies(offer: offer.toDomain()))
                                }
                        }
                    }
                }
                .padding(.horizontal, 10)
                .padding(.top)
            }
            .refreshable {
                event(.onRefresh)
                }
        }

        private var emptyState: some View {
            VStack(spacing: 12) {
                Image(systemName: "tray")
                    .font(.system(size: 48))
                    .foregroundColor(.gray)
                Text(Strings.general.offer_list_no_offers_text)
                    .foregroundColor(.secondary)
            }
            .frame(maxWidth: .infinity, minHeight: 300)
        }

        private var addButton: some View {
            Button {
                event(.createOfferScreen)
            } label: {
                Image(systemName: "plus")
                    .font(.title)
                    .foregroundColor(.white)
                    .padding()
                    .background(Color.dsPrimary)
                    .clipShape(Circle())
                    .shadow(radius: 4)
            }
            .padding(.bottom, 40)
            .padding(.trailing, 20)
        }
    }

    struct OfferCardView: View {
        let offer: OfferUIModel
        var body: some View {
            let statusText = offer.isActive
            ? Strings.general.offer_list_active_text
            : Strings.general.offer_list_closed_text
            VStack(alignment: .leading, spacing: 8) {
                HStack {
                    Text(offer.title)
                        .font(.headline)
                    Spacer()
                    Text(statusText)
                        .font(.caption)
                        .padding(.horizontal, 8)
                        .padding(.vertical, 4)
                        .background(offer.isActive ? Color.green.opacity(0.2) : Color.red.opacity(0.2))
                        .foregroundColor(offer.isActive ? .green : .red)
                        .clipShape(Capsule())
                }
                (Text(Strings.general.offer_list_formation_text).bold() + Text(offer.degree))
                    .font(.subheadline)
                (Text(Strings.general.offer_list_vacancies_text).bold() + Text("\(offer.vacancies)"))
                    .font(.subheadline)
                (Text(Strings.general.offer_list_modality_text).bold() + Text(offer.modality))
                    .font(.subheadline)
                HStack {
                    (Text(Strings.general.offer_list_watchers_text).bold() + Text("\(offer.views)"))
                        .font(.subheadline)
                Spacer()
                    (Text(Strings.general.candidacy_inscriptions_text).bold() + Text(" \(offer.inscriptionCandidacy)"))
                        .font(.subheadline)
                }
            }
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(Color(.systemBackground))
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(Color.gray.opacity(0.3), lineWidth: 1)
                    )
            )
        }
    }
}

extension WorkTutorOffersScreen {
    enum OfferStatus {
        case open, closed
    }

    struct FilterButton: View {
        let title: String
        let isSelected: Bool
        let action: () -> Void

        var body: some View {
            Button(action: action) {
                Text(title)
                    .fontWeight(.medium)
                    .padding(.vertical, 6)
                    .padding(.horizontal, 16)
                    .background(isSelected ? Color.dsPrimary : Color.gray.opacity(0.2))
                    .foregroundColor(isSelected ? .white : .primary)
                    .clipShape(Capsule())
            }
        }
    }
}

#if DEBUG
private extension WorkTutorOffersScreen {
    static func preview(state: Binding<WorkTutorOffersScreen.UIState>) -> some View {
        NavigationStack { Content(state: state, viewModel: ViewModel(), event: { _ in }) }
    }
}

#Preview("Initial State") {
    @Previewable @State var state: WorkTutorOffersScreen.UIState = .init()
    WorkTutorOffersScreen.preview(state: $state)
}
#endif
