import AppCommon
import Combine
import Data
import Factory
import FoundationUtils
import SwiftUI

public struct StudentOffersScreen: View {
    @State private var viewModel: ViewModel
    private let didCreateCandidacy: AnyPublisher<Int, Never>
    @Environment(\.studentoffersCoordinator) private var coordinator: StudentOffersCoordinator?

    public init(didCreateCandidacy: AnyPublisher<Int, Never>) {
        self.didCreateCandidacy = didCreateCandidacy
        let coordinator = Container.shared.appCoordinator()
        _viewModel = .init(wrappedValue: .init(appCoordinator: coordinator, didCreateCandidacy: didCreateCandidacy))
    }

    public var body: some View {
        VStack {
            Content(
                state: $viewModel.state,
                viewModel: viewModel,
                event: { event in viewModel.handle(event) }
            )
        }
        .alert(model: $viewModel.state.alert)
        .actionSheet(model: $viewModel.state.actionSheet)
        .onChange(of: viewModel.state.navigation) { _, newValue in
            guard let newValue else { return }
            switch newValue {
            case .close:
                coordinator?.dismiss()
            case .goToOfferDetail(let id):
                coordinator?.presentFullScreen(.offerDetail(id))
            }
            viewModel.handle(.didNavigate)
        }
    }

    struct Content: View {
        @Binding var state: UIState
        let viewModel: ViewModel
        let event: (Event) -> Void

        private func shouldLoadMore(offer: StudentOfferUIModel) -> Bool {
            guard let lastId = viewModel.offers.last?.id else { return false }
            return offer.id == lastId && viewModel.canLoadMore && !viewModel.isLoadingMore
        }

        var body: some View {
            ZStack {
                Color.gray.opacity(0.1)
                    .ignoresSafeArea()

                VStack {
                    ScrollView {
                        VStack(spacing: 10) {
                            ForEach(viewModel.offers) { offer in
                                Button(
                                    action: {
                                        event(.offerDetail(offer.id))
                                    },
                                    label: {
                                        StudentOfferCardView(
                                            offer: offer,
                                            degreeName: offer.formation,
                                            companyName: offer.companyName,
                                            companyLogoUrl: offer.companyLogoUrl
                                        )
                                    }
                                )
                                .buttonStyle(PlainButtonStyle())
                                .onAppear {
                                    if shouldLoadMore(offer: offer) {
                                        viewModel.loadData(reset: false)
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
            }
            .onViewDidLoad { event(.onViewDidLoad) }
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button {
                        event(.logoutConfirmationRequested)
                    } label: {
                        Image(systemName: "rectangle.portrait.and.arrow.right")
                            .foregroundColor(.red)
                    }
                    .accessibilityLabel(Strings.general.logout_confirmation_title_text)
                }
            }
            .navigationTitle(Strings.general.offer_list_offer_list_nav_text)
        }
    }
}
