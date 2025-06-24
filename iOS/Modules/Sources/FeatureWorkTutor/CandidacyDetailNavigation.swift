import AppCommon
import Combine
import SwiftUI

public struct CandidacyDetailNavigation: View {
    @State private var coordinator: CandidacyDetailCoordinator
    @State private var candidacyId: Int
    public let didUpdateCandidacy: PassthroughSubject<Void, Never>

    public init(candidacyId: Int, isPresented: Binding<Bool>?, didUpdateCandidacy: PassthroughSubject<Void, Never>) {
        self.didUpdateCandidacy = didUpdateCandidacy
        coordinator = .init(isPresented: isPresented)
        _candidacyId = State(initialValue: candidacyId)
    }

    public var body: some View {
        NavigationStack(path: $coordinator.path) {
            build(screen: .candidacyDetail(candidacyId: candidacyId))
                .navigationDestination(for: CandidacyDetailPath.self) { destination in
                    build(screen: destination)
                }
                .fullScreenCover(item: $coordinator.fullScreen) { item in
                    build(fullScreen: item)
                }
                .sheet(item: $coordinator.sheet) { item in
                    build(sheet: item)
                }
        }
        .environment(\.candidacyDetailCoordinator, coordinator)
    }

    @ViewBuilder
    func build(screen: CandidacyDetailPath) -> some View {
        switch screen {
        case .candidacyDetail(let candidacyId):
            CandidacyDetailScreen(candidacyId: candidacyId, didUpdateCandidacy: didUpdateCandidacy)
        }
    }

    @ViewBuilder
    func build(sheet: CandidacyDetailSheet) -> some View {
        switch sheet {
        case .mock:
            CandidacyDetailNavigation(
                candidacyId: candidacyId,
                isPresented: $coordinator.sheet.toBool(),
                didUpdateCandidacy: didUpdateCandidacy
            )
        }
    }

    @ViewBuilder
    func build(fullScreen: CandidacyDetailFullScreen) -> some View {
        switch fullScreen {
        case .mock:
            CandidacyDetailNavigation(
                candidacyId: candidacyId,
                isPresented: $coordinator.fullScreen.toBool(),
                didUpdateCandidacy: didUpdateCandidacy
            )
        }
    }

}
