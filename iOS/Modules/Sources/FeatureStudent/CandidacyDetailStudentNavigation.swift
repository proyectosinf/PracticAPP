import AppCommon
import SwiftUI

public struct CandidacyDetailStudentNavigation: View {
    @State private var coordinator: CandidacyDetailStudentCoordinator
    @State private var candidacyId: Int

    public init(candidacyId: Int, isPresented: Binding<Bool>?) {
        coordinator = .init(isPresented: isPresented)
        _candidacyId = State(initialValue: candidacyId)
    }

    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .candidacyDetaillStudent(candidacyId: candidacyId))
                .navigationDestination(for: CandidacyDetailStudentPath.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.candidacyDetailStudentCoordinator, coordinator)
    }

    @ViewBuilder
    func build(screen: CandidacyDetailStudentPath) -> some View {
        switch screen {
        case .candidacyDetaillStudent(let candidacyId):
            CandidacyDetailScreenStudent(candidacyId: candidacyId)
        }
    }

    @ViewBuilder
    func build(sheet: CandidacyDetailStudentSheet) -> some View {
        switch sheet {
        case .candidacyDetaillStudent(let candidacyId):
            CandidacyDetailStudentNavigation(candidacyId: candidacyId, isPresented: $coordinator.sheet.toBool())
        }
    }

    @ViewBuilder
    func build(fullScreen: CandidacyDetailStudentFullScreen) -> some View {
        switch fullScreen {
        case .mock:
            CandidacyDetailStudentNavigation(
                candidacyId: candidacyId,
                isPresented: $coordinator.sheet.toBool()
            )
        }
    }
}
