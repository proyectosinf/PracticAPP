import FoundationUtils
import SwiftUI

public struct MenuPickerView<O: PickerOption>: View {
    let title: String
    let options: [O]
    @Binding var value: O?
    let error: String?

    public init(
        title: String,
        options: [O],
        value: Binding<O?>,
        error: String? = nil
    ) {
        self.title = title
        self.options = options
        _value = value
        self.error = error
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: .spacingXXXS) {
            Picker(title, selection: $value) {
                ForEach(options, id: \.self) { Text($0.pickerOptionRepresentation).tag($0 as O?) }
            }
            .font(.subheadline)
            .foregroundStyle((error?.isEmpty ?? true) ? Color.dsOnSurface : .dsError)
            if let error, !error.isEmpty {
                Text(error)
                    .font(.caption)
                    .foregroundStyle(Color.dsError)
            }
        }
    }
}

public struct MenuPickerInput<O: PickerOption>: View {
    let title: String
    @Binding var state: PickerInputState<O>

    public init(
        title: String,
        state: Binding<PickerInputState<O>>
    ) {
        self.title = title
        _state = state
    }

    public var body: some View {
        MenuPickerView(
            title: title,
            options: state.options,
            value: $state.value,
            error: state.error
        )
    }
}

#Preview {
    @Previewable @State var stateEmpty: String?
    @Previewable @State var stateFemale: String? = "Female"
    @Previewable @State var stateFemalePickerInput: PickerInputState = .init(
        value: "Female",
        options: ["Male", "Female"]
    )
    NavigationStack {
        Form {
            MenuPickerView<String>(
                title: "Gender",
                options: ["Male", "Female"],
                value: $stateEmpty,
                error: "Obligatorio"
            )
            MenuPickerView<String>(
                title: "Gender",
                options: ["Male", "Female"],
                value: $stateFemale
            )
            MenuPickerInput(
                title: "Gender",
                state: $stateFemalePickerInput
            )
        }
    }
}
