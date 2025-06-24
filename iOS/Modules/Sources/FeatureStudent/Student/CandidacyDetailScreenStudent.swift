import AppCommon
import Data
import Factory
import FoundationUtils
import SwiftUI

struct CandidacyDetailScreenStudent: View {
    @State private var viewModel: ViewModel
    @Environment(\.candidacyDetailStudentCoordinator) private var coordinator: CandidacyDetailStudentCoordinator?

    init(candidacyId: Int) {
        _viewModel = .init(wrappedValue: .init(candidacyId: candidacyId))
    }

    var body: some View {
        Content(state: $viewModel.state) { event in
            viewModel.handle(event)
        }
        .alert(model: $viewModel.state.alert)
        .actionSheet(model: $viewModel.state.actionSheet)
        .onChange(of: viewModel.state.navigation) { _, newValue in
            guard let newValue else { return }
            switch newValue {
            case .close:
                coordinator?.dismiss()
            }
            viewModel.handle(.didNavigate)
        }
    }
    struct Content: View {
        @Binding var state: UIState
        let event: (Event) -> Void
        var body: some View {
            if let candidacy = state.candidacy {
                ScrollView {
                    VStack(spacing: .spacingL) {
                        VStack(alignment: .center, spacing: .spacingS) {
                            if !candidacy.companyPhoto.isEmpty {
                                RemoteImage(url: candidacy.companyPhoto)
                                    .scaledToFill()
                                    .frame(maxWidth: 300, maxHeight: 180)
                                    .clipped()
                                    .clipShape(Rectangle())
                                    .shadow(radius: 4)
                            } else {
                                ZStack {
                                    RoundedRectangle(cornerRadius: 12)
                                        .fill(Color.gray.opacity(0.1))
                                        .frame(height: 250)
                                        .frame(maxWidth: 300)
                                        .shadow(radius: 4)
                                    Image(systemName: "xmark.octagon.fill")
                                        .resizable()
                                        .scaledToFit()
                                        .frame(width: 60, height: 60)
                                        .foregroundColor(.gray)
                                }
                                .padding(.top, .spacingS)
                            }
                            Text(candidacy.companyName)
                                .font(.title2.bold())
                                .multilineTextAlignment(.center)
                            HStack {
                                Text(Strings.general.candidacy_postulation_text).bold()
                                Text(FormattedDate.format(candidacy.postulationDate))
                                    .font(.subheadline)
                            }
                        }

                        VStack(alignment: .leading, spacing: .spacingM) {
                            HStack {
                                Image(systemName: "person.fill")
                                Text(candidacy.contactName)
                            }
                            HStack {
                                Image(systemName: "envelope.fill")
                                Text(candidacy.contactEmail)
                            }
                            if let phone = candidacy.contactPhone, !phone.isEmpty {
                                HStack {
                                    Image(systemName: "phone.fill")
                                    Text(phone)
                                }
                            }
                        }
                        .font(.subheadline)
                        .padding(.horizontal)
                        Divider()
                        if let notes = candidacy.additionalNotes, !notes.isEmpty {
                            VStack(alignment: .leading, spacing: .spacingM) {
                                Text(Strings.general.candidacy_additional_note_text)
                                    .font(.headline)
                                Text(notes)
                                    .font(.body)
                                    .foregroundColor(.primary)
                            }
                            .padding(.horizontal)
                        }
                    }
                    .padding(.top)
                }
                .navigationTitle(candidacy.offerTitle)
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
                .onViewDidLoad { event(.onViewDidLoad) }
            } else {
                ProgressView(Strings.general.common_loading_text)
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .onViewDidLoad { event(.onViewDidLoad) }
            }
        }
    }
}
#if DEBUG
private extension CandidacyDetailScreenStudent {
    static func preview(state: Binding<CandidacyDetailScreenStudent.UIState>) -> some View {
        NavigationStack { Content(state: state, event: { _ in }) }
    }
}

#Preview("Initial State") {
    @Previewable @State var state: CandidacyDetailScreenStudent.UIState = .init()
    CandidacyDetailScreenStudent.preview(state: $state)
}
#endif
