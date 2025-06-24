import AppCommon
import Domain
import Factory
import SwiftUI

extension ___VARIABLE_screenName___ {
    enum Navigation: Equatable {
        case close
    }

    enum Event {
        case onViewDidLoad
        case didNavigate
        case cancel
    }

    struct UIState {
        var loading: Bool = false
        var alert: AlertUIState?
        var actionSheet: ActionSheetUIState?
        var navigation: Navigation?
    }

    @Observable @MainActor
    final class ViewModel {
        var state: UIState

        // dependencies here...

        init(container: Container = .shared) {
            state = .init()
            // initialize dependencies from container here...
        }

        func handle(_ event: Event) {
            switch event {
            case .onViewDidLoad:
                loadData()
            case .didNavigate:
                state.navigation = nil
            case .cancel:
                state.navigation = .close
            }
        }

        func loadData() {
            /*
             Task {
                 defer { state.loading = false }
                 state.loading = true
                 do {
                     // Call to your use case or repository here...
                 } catch {
                     // Handle error...
                     state.alert = .init(error: error)
                 }
             }
             */
        }
    }
}
