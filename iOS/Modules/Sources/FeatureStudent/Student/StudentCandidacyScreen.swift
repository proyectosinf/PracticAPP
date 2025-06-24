import AppCommon
import Data
import Factory
import FoundationUtils
import SwiftUI
import Domain

struct StudentCandidacyScreen: View {
    @State private var viewModel: ViewModel
    @Environment(\.studentcandidacyCoordinator) private var coordinator: StudentCandidacyCoordinator?
    init() {
        _viewModel = .init(wrappedValue: .init())
    }
    var body: some View {
        Content(state: $viewModel.state, candidacies: viewModel.candidacies) { event in
            viewModel.handle(event)
        }
        .alert(model: $viewModel.state.alert)
        .actionSheet(model: $viewModel.state.actionSheet)
        .onChange(of: viewModel.state.navigation) { _, newValue in
            guard let newValue else { return }
            switch newValue {
            case .close:
                coordinator?.dismiss()
            case .goToDetaill(let candidacyId):
                coordinator?.presentSheet(.detailCandidacy(candidacyId: candidacyId))
            }
            viewModel.handle(.didNavigate)
        }
    }
    struct Content: View {
        @Binding var state: UIState
        let candidacies: [CandidacyListItem]
        let event: (Event) -> Void
        var body: some View {
            ZStack {
                Color.gray.opacity(0.1)
                    .ignoresSafeArea()
                VStack {
                    if state.loading {
                        ProgressView(Strings.general.common_loading_text)
                            .padding()
                    } else if candidacies.isEmpty {
                        ContentUnavailableView(
                            Strings.general.candidacy_not_candidacy_text,
                            systemImage: "person.3"
                        )
                    } else {
                        ScrollView {
                            LazyVStack(spacing: 16) {
                                ForEach(candidacies) { item in
                                    Button {
                                        event(.didSelectCandidacy(id: item.id))
                                    } label: {
                                        CandidacyStudentCardView(item: item)
                                    }
                                    .buttonStyle(.plain)
                                    .onAppear {
                                        if item == candidacies.last {
                                            event(.loadNextPage)
                                        }
                                    }
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
            .onViewDidLoad {
                event(.onViewDidLoad)
            }
            .onAppear {
                if candidacies.isEmpty {
                    event(.onViewDidLoad)
                }
            }
            .navigationTitle(Strings.general.candidacy_my_tittle_text, displayMode: .inline)
        }
    }
}
#Preview("CandidacyStudentCardView - Mock") {
    let mockItem = CandidacyListItem(
        id: 1,
        offerTitle: "Prácticas iOS Developer",
        studentName: "",
        studentSurname: "",
        postulationDate: "12-02-2025",
        status: 1,
        companyName: "MicroInfor"
    )

    CandidacyStudentCardView(item: mockItem)

}
