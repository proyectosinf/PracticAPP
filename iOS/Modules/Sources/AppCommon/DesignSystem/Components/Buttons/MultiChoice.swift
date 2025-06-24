import SwiftUI

public struct CheckButtonImage: View {
    let isSelected: Bool

    public init(isSelected: Bool) {
        self.isSelected = isSelected
    }

    public var body: some View {
        Image(systemName: isSelected ? "checkmark.square.fill" : "square")
            .foregroundColor(isSelected ? .dsPrimary : Color.gray)
    }
}

public struct MultiChoiceButton<Tag, Label>: View where Tag: Hashable, Label: View {
    let tag: Tag
    @Binding var selection: Set<Tag>
    @ViewBuilder let label: () -> Label
    var isSelected: Bool { selection.contains(tag) }

    public init(tag: Tag, selection: Binding<Set<Tag>>, label: @escaping () -> Label) {
        self.tag = tag
        _selection = selection
        self.label = label
    }

    public var body: some View {
        Button {
            if selection.contains(tag) {
                selection.remove(tag)
            } else {
                selection.insert(tag)
            }
        } label: {
            HStack(alignment: .top) {
                CheckButtonImage(isSelected: isSelected)
                    .padding(.top, 1)
                label()
                Spacer()
            }
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
    }
}

public extension MultiChoiceButton where Label == Text {
    init(_ text: LocalizedStringKey, tag: Tag, selection: Binding<Set<Tag>>) {
        self.tag = tag
        _selection = selection
        label = { Text(text) }
    }

    init(_ text: some StringProtocol, tag: Tag, selection: Binding<Set<Tag>>) {
        self.tag = tag
        _selection = selection
        label = { Text(text) }
    }
}

#if DEBUG
// swiftlint:disable line_length
struct MultiChoiceButtonsPreview: View {
    let options: [Int] = [0, 1, 2]
    @State var selection: Set<Int> = []

    var body: some View {
        Form {
            Section {
                ForEach(options, id: \.self) { tag in
                    MultiChoiceButton("Opción **\(tag)**", tag: tag, selection: $selection)
                }
            } header: {
                Text("Header")
            }

            Section {
                ForEach(options, id: \.self) { tag in
                    MultiChoiceButton(tag: tag, selection: $selection) {
                        Text(
                            "Opción **\(tag)** con un texto bastante largo que no cabe en una linea, ni en dos, si no en tres."
                        )
                    }
                }
            } header: {
                Text("Header")
            }
        }
    }
}

#Preview { MultiChoiceButtonsPreview() }
// swiftlint:enable line_length
#endif
