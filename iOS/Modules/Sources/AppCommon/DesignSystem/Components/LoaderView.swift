import SwiftUI

public struct LoaderView: View {
    let message: String
    let circular: Bool

    @Binding private var loading: Bool

    @State private var counter = 0.0
    @State private var timer: Timer?

    public init(
        loading: Binding<Bool>,
        message: String = Strings.general.common_loading_text,
        circular: Bool = true
    ) {
        _loading = .init(projectedValue: loading)
        self.message = message
        self.circular = circular
    }

    public var body: some View {
        if loading {
            if circular {
                HStack(spacing: 8) {
                    ProgressView(message)
                        .progressViewStyle(.horizontal)
                }
                .padding(.spacingM)
                .background(Color.dsSurface)
                .cornerRadius(15)
                .shadow(color: Color.black.opacity(0.2), radius: 5, x: 0, y: 5)
            } else {
                ProgressView(value: counter, total: 100.0)
                    .tint(.dsPrimary)
                    .onChange(of: loading) {
                        if loading {
                            runCounter(
                                counter: $counter,
                                start: 0.0,
                                end: 100.0,
                                speed: 0.05
                            )
                        } else {
                            timer?.invalidate()
                            timer = nil
                        }
                    }
                    .onAppear {
                        if loading {
                            runCounter(
                                counter: $counter,
                                start: 0.0,
                                end: 100.0,
                                speed: 0.05
                            )
                        }
                    }
            }
        }
    }

    private func runCounter(
        counter: Binding<Double>,
        start: Double,
        end: Double,
        speed: Double
    ) {
        counter.wrappedValue = start

        timer = Timer.scheduledTimer(withTimeInterval: speed, repeats: true) { _ in
            counter.wrappedValue += 1.0
            if counter.wrappedValue == end {
                counter.wrappedValue = 0.0
            }
        }
    }
}

struct HorizontalProgressViewStyle: ProgressViewStyle {
    func makeBody(configuration: Configuration) -> some View {
        HStack(spacing: 8) {
            ProgressView()
                .progressViewStyle(.circular)
            configuration.label
        }.foregroundColor(.secondary)
    }
}

extension ProgressViewStyle where Self == HorizontalProgressViewStyle {
    static var horizontal: HorizontalProgressViewStyle { .init() }
}

#if DEBUG
#Preview {
    VStack {
        LoaderView(loading: .constant(true), circular: false)
            .frame(alignment: .top)

        ScrollView {
            LazyVStack {
                ForEach(1 ..< 11) { number in
                    Text("Number \(number)")
                        .frame(maxWidth: .infinity, minHeight: 50)
                }
            }
        }
    }
    .frame(maxWidth: .infinity, maxHeight: .infinity)
    .background(Color.dsBackground)
}

#Preview {
    ZStack {
        ScrollView {
            LazyVStack {
                ForEach(1 ..< 11) { number in
                    Text("Número \(number)")
                        .frame(maxWidth: .infinity, minHeight: 50)
                }
            }
        }

        LoaderView(loading: .constant(true), message: "Loading...")
    }
    .frame(maxWidth: .infinity, maxHeight: .infinity)
    .background(Color.dsBackground)
}
#endif
