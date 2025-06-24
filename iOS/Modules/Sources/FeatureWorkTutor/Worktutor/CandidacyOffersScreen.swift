import AppCommon
import Combine
import Data
import Domain
import Factory
import FoundationUtils
import SwiftUI

struct CandidacyOffersScreen: View {
    @State private var viewModel: ViewModel
    @Environment(\.candidacyOffersCoordinator) private var coordinator: CandidacyOffersCoordinator?
    private let didUpdateCandidacy: AnyPublisher<Void, Never>
    init(offer: Offer, didUpdateCandidacy: AnyPublisher<Void, Never>) {
            self.didUpdateCandidacy = didUpdateCandidacy
            _viewModel = .init(wrappedValue: .init(offer: offer))
        }
    var body: some View {
        Content(offer: viewModel.offer, state: $viewModel.state, candidacies: viewModel.candidacies) { event in
            viewModel.handle(event)
        }
        .alert(model: $viewModel.state.alert)
        .onReceive(didUpdateCandidacy) { _ in
            viewModel.loadData()
        }
        .actionSheet(model: $viewModel.state.actionSheet)
        .onChange(of: viewModel.state.navigation) { _, newValue in
            guard let newValue else { return }
            switch newValue {
            case .close:
                coordinator?.dismiss()
            case .goToDetailCandidacy(let candidacyId):
                coordinator?.presentSheet(.candidacyDetail(candidacyId: candidacyId))
            }
            viewModel.handle(.didNavigate)
        }
    }
    struct Content: View {
        let offer: Offer
        @Binding var state: UIState
        let candidacies: [CandidacyListItem]
        let event: (Event) -> Void
        var body: some View {
            ZStack {
                Color.gray.opacity(0.1)
                    .ignoresSafeArea()
                VStack {
                    if state.loading {
                        ProgressView(Strings.general.candidacy_loading_text)
                            .padding()
                    } else if candidacies.isEmpty {
                        ContentUnavailableView(
                            Strings.general.candidacy_not_candidacy_text,
                            systemImage: "person.3",
                        )
                    } else {
                        ScrollView {
                            LazyVStack(spacing: 16) {
                                ForEach(candidacies) { item in
                                    Button {
                                        event(.detailCandidacy(candidacyId: item.id))
                                    } label: {
                                        CandidacyCardView(item: item)
                                    }
                                    .buttonStyle(.plain)
                                }
                            }
                            .padding()
                        }
                        .refreshable {
                            event(.onRefresh)
                        }
                    }
                }
            }
            .onViewDidLoad { event(.onViewDidLoad) }
            .navigationTitle(offer.title, displayMode: .inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(Strings.general.common_cancel_text) {
                        event(.cancel)
                    }
                }
            }
        }
    }
}
