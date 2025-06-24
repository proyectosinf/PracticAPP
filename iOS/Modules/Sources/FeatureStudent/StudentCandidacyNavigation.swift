import AppCommon
import SwiftUI

public struct StudentCandidacyNavigation: View {
    @State private var coordinator: StudentCandidacyCoordinator

    public init(_ isPresented: Binding<Bool>?) {
        coordinator = .init(isPresented: isPresented)
    }

    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .candidacies)
                .navigationDestination(for: StudentCandidacyPath.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.studentcandidacyCoordinator, coordinator)
    }

    @ViewBuilder
    func build(screen: StudentCandidacyPath) -> some View {
        switch screen {
        case .candidacies:
            StudentCandidacyScreen()
        }
    }

    @ViewBuilder
    func build(sheet: StudentCandidacySheet) -> some View {
        switch sheet {
        case .mock:
            StudentCandidacyNavigation($coordinator.sheet.toBool())
        case .detailCandidacy(let candidacyId):
            CandidacyDetailStudentNavigation(candidacyId: candidacyId, isPresented: $coordinator.sheet.toBool())
        }
    }

    @ViewBuilder
    func build(fullScreen: StudentCandidacyFullScreen) -> some View {
        switch fullScreen {
        case .mock:
            StudentCandidacyNavigation($coordinator.fullScreen.toBool())
        }
    }
}

#if DEBUG
#Preview("Root") {
    StudentCandidacyNavigation(nil)
}

#Preview("Sheet") {
    @Previewable @State var isPresented = true
    Button("Present") { isPresented.toggle() }
        .sheet(isPresented: $isPresented) {
            StudentCandidacyNavigation($isPresented)
        }
}
#endif
