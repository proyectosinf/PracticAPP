import SwiftUI

public struct RadioButtonImage: View {
    let isSelected: Bool

    public init(isSelected: Bool) {
        self.isSelected = isSelected
    }

    public var body: some View {
        Image(systemName: isSelected ? "checkmark.circle.fill" : "circle")
            .foregroundColor(isSelected ? .dsPrimary : Color.gray)
    }
}

public struct RadioButton<Tag, Label>: View where Tag: Hashable, Label: View {
    let tag: Tag
    @Binding var selection: Tag
    @ViewBuilder let label: () -> Label
    var isSelected: Bool { selection == tag }

    public init(tag: Tag, selection: Binding<Tag>, label: @escaping () -> Label) {
        self.tag = tag
        _selection = selection
        self.label = label
    }

    public var body: some View {
        Button {
            selection = tag
        } label: {
            HStack(alignment: .top) {
                RadioButtonImage(isSelected: isSelected)
                    .padding(.top, 1)
                label()
                Spacer()
            }
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
    }
}

public extension RadioButton where Label == Text {
    init(_ text: LocalizedStringKey, tag: Tag, selection: Binding<Tag>) {
        self.tag = tag
        _selection = selection
        label = { Text(text) }
    }

    init(_ text: some StringProtocol, tag: Tag, selection: Binding<Tag>) {
        self.tag = tag
        _selection = selection
        label = { Text(text) }
    }
}

struct RadioButtonsPreview: View {
    let options: [Int] = [0, 1, 2]
    @State var selection: Int = 0
    @State private var dates: Set<DateComponents> = []

    var body: some View {
        Form {
            Section {
                ForEach(options, id: \.self) { tag in
                    RadioButton(tag: tag, selection: $selection) {
                        Text("Opción **\(tag)**")
                    }
                }
            } header: {
                Text("Header")
            }

            Section {
                ForEach(options, id: \.self) { tag in
                    RadioButton("Opción **\(tag)**", tag: tag, selection: $selection)
                }
            } header: {
                Text("Header")
            }
        }
    }
}

#if DEBUG
#Preview { RadioButtonsPreview() }
#endif
