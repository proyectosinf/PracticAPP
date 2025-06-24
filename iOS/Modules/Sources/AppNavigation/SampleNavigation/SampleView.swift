import AppCommon
import SwiftUI

struct SampleView: View {
    let title: String

    @Environment(\.sampleCoordinator) var coordinator

    var body: some View {
        VStack(spacing: 16) {
            Button("Push to sample 1") {
                coordinator?.push(.path1)
            }

            Button("Push to sample 2") {
                coordinator?.push(.path2)
            }

            Button("Push to sample 3") {
                coordinator?.push(.path3)
            }

            Button("Pop to root") {
                coordinator?.popToRoot()
            }

            Button("Sheet") {
                coordinator?.sheet = .sheet
            }

            Button("Full Screen") {
                coordinator?.fullScreen = .fullScreen
            }
        }
        .navigationTitle(title)
        .toolbar {
            ToolbarItem(placement: .topBarTrailing) {
                if coordinator?.isPresented != nil {
                    Button("Cancel") {
                        coordinator?.dismiss()
                    }
                }
            }
        }
    }
}
