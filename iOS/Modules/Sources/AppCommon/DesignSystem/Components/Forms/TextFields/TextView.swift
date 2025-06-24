import FoundationUtils
import SwiftUI
@_spi(Advanced) import SwiftUIIntrospect

public struct TextView: View {
    let title: String
    let placeholder: String
    let error: String
    let text: Binding<String>
    let disabled: Bool
    let maxLength: Int?
    @State var textEditorHeight: CGFloat = 20
    let minHeight: CGFloat
    let maxHeight: CGFloat

    public init(
        title: String,
        text: Binding<String>,
        placeholder: String = "",
        error: String = "",
        maxLength: Int? = nil,
        disabled: Bool = false,
        minHeight: CGFloat = 30,
        maxHeight: CGFloat = 200
    ) {
        self.title = title
        self.placeholder = placeholder
        self.error = error
        self.text = text
        self.maxLength = maxLength
        self.disabled = disabled
        self.minHeight = minHeight
        self.maxHeight = maxHeight
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: 1) {
            Text(title)
                .font(.subheadline)
                .foregroundStyle(error.isEmpty ? Color.dsOnSurface : .dsError)
                .padding(.top, 4)

            HStack(alignment: .bottom, spacing: 0) {
                ZStack(alignment: .topLeading) {
                    Text(text.wrappedValue)
                        .foregroundColor(.clear)
                        .multilineTextAlignment(.leading)
                        .background(
                            GeometryReader {
                                Color.clear.opacity(0.2)
                                    .preference(key: ViewHeightKey.self, value: $0.frame(in: .local).size.height)
                            }
                        )
                        .frame(minHeight: minHeight, maxHeight: maxHeight)
                    Text(placeholder.isEmpty ? title : placeholder)
                        .foregroundStyle(Color.dsOnSurfaceSecondary)
                        .opacity(text.wrappedValue.isEmpty ? 1 : 0)

                    TextEditor(text: text)
                        .scrollContentBackground(.hidden)
                        .background(.clear)
                        .multilineTextAlignment(.leading)
                        .introspect(.textEditor, on: .iOS(.v16...)) {
                            $0.textContainerInset = .zero
                            $0.textContainer.lineFragmentPadding = 0
                        }
                        .frame(height: min(maxHeight, max(minHeight, textEditorHeight)))
                }
                .onPreferenceChange(ViewHeightKey.self) { [$textEditorHeight] height in
                    $textEditorHeight.wrappedValue = height
                }

                if let maxLength, !text.wrappedValue.isEmpty {
                    Text("\(maxLength - text.wrappedValue.count)")
                        .font(.subheadline)
                        .foregroundStyle(Color.dsOnSurfaceSecondary)
                }
            }
        }
    }
}

struct ViewHeightKey: PreferenceKey {
    static var defaultValue: CGFloat { 0 }
    static func reduce(value: inout Value, nextValue: () -> Value) {
        value += nextValue()
    }
}

public struct TextViewInput: View {
    let title: String
    @Binding var state: InputState<String>
    let placeholder: String?
    let disabled: Bool
    let maxLength: Int?
    let minHeight: CGFloat
    let maxHeight: CGFloat

    public init(
        title: String,
        state: Binding<InputState<String>>,
        placeholder: String? = nil,
        maxLength: Int? = nil,
        disabled: Bool = false,
        minHeight: CGFloat = 30,
        maxHeight: CGFloat = 200
    ) {
        self.title = title
        _state = state
        self.placeholder = placeholder
        self.maxLength = maxLength
        self.disabled = disabled
        self.minHeight = minHeight
        self.maxHeight = maxHeight
    }

    public var body: some View {
        TextView(
            title: title,
            text: $state.value.defaultValue(""),
            placeholder: placeholder ?? "",
            error: state.error ?? "",
            maxLength: maxLength,
            disabled: disabled,
            minHeight: minHeight,
            maxHeight: maxHeight
        )
    }
}

#if DEBUG
// swiftlint:disable line_length
@available(iOS 17.0, *)
#Preview {
    @Previewable @State var stateEmpty = ""
    @Previewable @State var stateLongText =
        "Este texto de 4 lineas debería verse completo hasta la palabra FINAL. Así que vamos a seguir incluyendo texto hasta que llegemos al mismo FINAL."
    Form {
        TextView(title: "Title", text: $stateEmpty, placeholder: "Placeholder", maxLength: 20)

        TextView(
            title: "En lugar",
            text: $stateLongText,
            placeholder: "Placeholder",
            maxLength: 200
        )

        TextView(
            title: "Min 100 max 200",
            text: $stateEmpty,
            placeholder: "Placeholder",
            maxLength: 200,
            minHeight: 100,
            maxHeight: 200
        )
    }
}
// swiftlint:enable line_length
#endif
