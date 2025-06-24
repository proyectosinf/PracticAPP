import SwiftUI

public struct LoadMoreCellView: View {
    let onAppear: () -> Void

    public init(_ onAppear: @escaping () -> Void) {
        self.onAppear = onAppear
    }

    public var body: some View {
        HStack {
            Spacer()
            ProgressView()
            Spacer()
        }
        .listRowSeparator(.hidden)
        .listRowInsets(.axis(h: .spacingS, v: .spacingS))
        .listRowBackground(Color.clear)
        .onAppear(perform: onAppear)
    }
}

#Preview {
    List {
        LoadMoreCellView {}
    }
}
