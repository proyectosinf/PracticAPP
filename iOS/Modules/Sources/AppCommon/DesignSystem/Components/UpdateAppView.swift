import SwiftUI

struct UpdateAppView: View {
    private let appUpdate: AppUpdateUIState
    private let openURLAction: () -> Void
    private let dismissAction: () -> Void

    init(
        appUpdate: AppUpdateUIState,
        openURLAction: @escaping () -> Void,
        dismissAction: @escaping () -> Void
    ) {
        self.appUpdate = appUpdate
        self.openURLAction = openURLAction
        self.dismissAction = dismissAction
    }

    var body: some View {
        ZStack {
            VStack(spacing: .spacingS) {
                VStack(spacing: .spacingXS) {
                    Text(Strings.general.common_update_app_text)
                        .font(.title3)
                        .bold()
                        .foregroundStyle(Color.dsOnSurface)

                    Text(appUpdate.message)
                        .font(.subheadline)
                        .foregroundStyle(Color.dsOnSurfaceSecondary)
                        .multilineTextAlignment(.center)
                }

                Button(Strings.general.common_update_button) { openURLAction() }
                    .buttonStyle(.primary)

                if !appUpdate.force {
                    Button(Strings.general.common_cancel_text) { dismissAction() }
                        .buttonStyle(.tertiaryMedium)
                }
            }
            .padding(.all, .spacingM)
            .card(cornerRadius: 16.0)
            .padding(.horizontal, .spacingL)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color.dsOnBackground.opacity(0.2).edgesIgnoringSafeArea(.all))
    }
}

struct UpdateAppModifier: ViewModifier {
    private let appUpdate: AppUpdateUIState?
    private let openURLAction: () -> Void
    private let dismissAction: () -> Void

    init(
        appUpdate: AppUpdateUIState?,
        openURLAction: @escaping () -> Void,
        dismissAction: @escaping () -> Void
    ) {
        self.appUpdate = appUpdate
        self.openURLAction = openURLAction
        self.dismissAction = dismissAction
    }

    func body(content: Content) -> some View {
        ZStack {
            content
            if let appUpdate {
                UpdateAppView(appUpdate: appUpdate, openURLAction: openURLAction, dismissAction: dismissAction)
            }
        }
    }
}

public extension View {
    func updateApp(
        appUpdate: AppUpdateUIState?,
        openURLAction: @escaping () -> Void,
        dismissAction: @escaping () -> Void
    ) -> some View {
        modifier(UpdateAppModifier(appUpdate: appUpdate, openURLAction: openURLAction, dismissAction: dismissAction))
    }
}

#if DEBUG
#Preview("Force Update") {
    Text("Hi!")
        .background(Color.dsBackground)
        .updateApp(appUpdate: .mockForce, openURLAction: {}, dismissAction: {})
}

#Preview("Force Update optional") {
    Text("Hi!")
        .updateApp(appUpdate: .mockOptional, openURLAction: {}, dismissAction: {})
}
#endif
