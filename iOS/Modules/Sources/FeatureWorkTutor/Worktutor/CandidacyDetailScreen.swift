import AppCommon
import Combine
import Data
import Factory
import FoundationUtils
import SwiftUI

struct CandidacyDetailScreen: View {
    @State private var viewModel: ViewModel
    @Environment(\.dismiss) private var dismiss
    @Environment(\.candidacyDetailCoordinator) private var coordinator: CandidacyDetailCoordinator?
    private let didUpdateCandidacy: PassthroughSubject<Void, Never>
    @FocusState private var isTextFieldFocused: Bool

    init(candidacyId: Int, didUpdateCandidacy: PassthroughSubject<Void, Never>) {
        self.didUpdateCandidacy = didUpdateCandidacy
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
            if case .close = newValue {
                didUpdateCandidacy.send()
                dismiss()
            }
            viewModel.handle(.didNavigate)
        }
    }

    struct Content: View {
        @Binding var state: UIState
        @FocusState private var isTextFieldFocused: Bool
        let event: (Event) -> Void

        var body: some View {
            GeometryReader { _ in
                VStack(spacing: 0) {
                    if let candidacy = state.candidacy {
                        ScrollViewReader { proxy in
                            ScrollView {
                                VStack(spacing: 16) {
                                    VStack(spacing: .spacingS) {
                                        if let photo = candidacy.studentPhoto, !photo.isEmpty {
                                            RemoteImage(url: photo)
                                                .frame(width: 120, height: 120)
                                                .clipShape(Circle())
                                                .shadow(radius: 4)
                                        } else {
                                            ZStack {
                                                Circle()
                                                    .fill(Color.gray.opacity(0.2))
                                                    .frame(width: 120, height: 120)
                                                Image(systemName: "person.fill")
                                                    .font(.system(size: 50))
                                                    .foregroundColor(.gray)
                                            }
                                        }

                                        Text("\(candidacy.studentName) \(candidacy.studentSurname)")
                                            .font(.title3.bold())
                                        Text(candidacy.studentEmail)
                                            .font(.subheadline)
                                            .foregroundColor(.secondary)
                                        Text("\(Strings.general.candidacy_postulation_text) \(FormattedDate.format(candidacy.postulationDate))")
                                            .font(.footnote)
                                    }

                                    if !candidacy.presentationCard.isEmpty {
                                        VStack(alignment: .leading, spacing: .spacingXS) {
                                            Text(Strings.general.candidacy_presentation_card_text)
                                                .font(.title3.bold())
                                            ScrollView {
                                                Text(candidacy.presentationCard)
                                                    .font(.body)
                                                    .multilineTextAlignment(.leading)
                                                    .padding(.vertical, 8)
                                            }
                                            .frame(minHeight: 150, maxHeight: 300)
                                        }
                                    }

                                    if candidacy.status == 1 {
                                        VStack(alignment: .leading, spacing: 8) {
                                            Text(Strings.general.candidacy_additional_note_hint_text)
                                                .font(.subheadline)
                                            TextEditor(text: $state.additionalNote)
                                                .focused($isTextFieldFocused)
                                                .frame(minHeight: 80)
                                                .padding(8)
                                                .background(
                                                    RoundedRectangle(cornerRadius: 8)
                                                        .stroke(Color.gray.opacity(0.5))
                                                )
                                                .id("noteField")
                                        }
                                        .padding(.top, 16)
                                    }

                                    Spacer(minLength: 300)
                                }
                                .padding()
                                .frame(maxWidth: .infinity)
                                .background(
                                    RoundedRectangle(cornerRadius: 20)
                                        .fill(Color(.systemBackground))
                                        .shadow(color: .black.opacity(0.1), radius: 6, x: 0, y: 3)
                                )
                                .padding(.horizontal)
                                .padding(.top)
                            }
                            .ignoresSafeArea(.keyboard)
                            .onTapGesture {
                                hideKeyboard()
                            }
                            .onChange(of: isTextFieldFocused) { _, newValue in
                                if newValue {
                                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
                                        withAnimation {
                                            proxy.scrollTo("noteField", anchor: .center)
                                        }
                                    }
                                }
                            }
                        }

                        if candidacy.status == 1 {
                            Divider()
                            HStack(spacing: 16) {
                                Button(Strings.general.candidacy_reject_button) {
                                    event(.rejected)
                                }
                                .buttonStyle(.primary)
                                .frame(maxWidth: .infinity)

                                Button(Strings.general.candidacy_accept_button) {
                                    event(.toAcceptCandidacy)
                                }
                                .buttonStyle(.primary)
                                .frame(maxWidth: .infinity)
                            }
                            .padding()
                            .background(Color(.systemBackground).ignoresSafeArea(edges: .bottom))
                            .shadow(radius: 3)
                        }

                    } else {
                        ProgressView(Strings.general.common_loading_text)
                            .frame(maxWidth: .infinity, maxHeight: .infinity)
                    }
                }
                .navigationTitle(state.candidacy?.offerTitle ?? "")
                .navigationBarTitleDisplayMode(.inline)
                .toolbar {
                    ToolbarItem(placement: .navigationBarLeading) {
                        Button {
                            event(.cancel)
                        } label: {
                            Image(systemName: "chevron.backward")
                                .font(.body.weight(.medium))
                        }
                    }
                }
                .onViewDidLoad {
                    event(.onViewDidLoad)
                }
            }
        }

        private func hideKeyboard() {
            UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
        }
    }
}
