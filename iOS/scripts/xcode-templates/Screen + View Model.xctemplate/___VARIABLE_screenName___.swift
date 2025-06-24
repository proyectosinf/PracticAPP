import AppCommon
import Data
import Factory
import FoundationUtils
import SwiftUI

struct ___VARIABLE_screenName___: View {
    @State private var viewModel: ViewModel
    @Environment(\.<#coordinator path name#>) private var coordinator: <#coordinator class name#>?

    init() {
        _viewModel = .init(wrappedValue: .init())
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
            ScrollView {
                VStack(spacing: .spacingS) {
                    Text("___VARIABLE_screenName___")
                }
            }
            .onViewDidLoad { event(.onViewDidLoad) }
            .navigationTitle("⚠️ ___VARIABLE_name___", displayMode: .automatic)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(Strings.general.common_cancel_text) { event(.cancel) }
                }
            }
        }
    }
}

#if DEBUG
private extension ___VARIABLE_screenName___ {
    static func preview(state: Binding<___VARIABLE_screenName___.UIState>) -> some View {
        NavigationStack { Content(state: state, event: { _ in }) }
    }
}

#Preview("Initial State") {
    @Previewable @State var state: ___VARIABLE_screenName___.UIState = .init()
    ___VARIABLE_screenName___.preview(state: $state)
}
#endif
