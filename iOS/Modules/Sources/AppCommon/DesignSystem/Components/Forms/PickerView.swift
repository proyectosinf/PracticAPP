import FoundationUtils
import SwiftUI

public struct PickerView<O: PickerOption>: View {
    let title: String
    let options: [O]
    @Binding var value: O?
    let error: String?
    let placeholder: String
    let canClear: Bool
    let canSearch: Bool
    let listStyle: any ListStyle

    public init(
        title: String,
        options: [O],
        value: Binding<O?>,
        error: String? = nil,
        placeholder: String,
        canClear: Bool = false,
        canSearch: Bool = false,
        listStyle: any ListStyle = .insetGrouped
    ) {
        self.title = title
        self.options = options
        _value = value
        self.error = error
        self.placeholder = placeholder
        self.canClear = canClear
        self.canSearch = canSearch
        self.listStyle = listStyle
    }

    public var body: some View {
        NavigationLink(
            destination: {
                OptionsList(title: title, options: options, selection: $value, canSearch: canSearch, style: listStyle)
            },
            label: {
                HStack {
                    VStack(alignment: .leading, spacing: .spacingXXXS) {
                        Text(title)
                            .font(.subheadline)
                            .foregroundStyle((error?.isEmpty ?? true) ? Color.dsOnSurface : .dsError)
                        HStack(spacing: .spacingXXS) {
                            Text(value?.pickerOptionRepresentation ?? placeholder)
                                .foregroundStyle(value == nil ? Color.dsOnSurfaceSecondary : Color.dsOnSurface)
                            Spacer()
                        }
                        if let error, !error.isEmpty {
                            Text(error)
                                .font(.caption)
                                .foregroundStyle(Color.dsError)
                        }
                    }

                    if canClear, value != nil { clearButton }
                }
            }
        )
    }

    var clearButton: some View {
        Button(
            action: { value = nil },
            label: { Image(systemName: "xmark.circle.fill") }
        )
        .buttonStyle(BorderlessButtonStyle())
        .foregroundStyle(Color.dsOnBackgroundSecondary)
        .font(.subheadline)
    }
}

public struct PickerInput<O: PickerOption>: View {
    let title: String
    @Binding var state: PickerInputState<O>
    let placeholder: String
    let canClear: Bool
    let canSearch: Bool
    let listStyle: any ListStyle

    public init(
        title: String,
        state: Binding<PickerInputState<O>>,
        placeholder: String,
        canClear: Bool = false,
        canSearch: Bool = false,
        listStyle: any ListStyle = .insetGrouped
    ) {
        self.title = title
        _state = state
        self.placeholder = placeholder
        self.canClear = canClear
        self.canSearch = canSearch
        self.listStyle = listStyle
    }

    public var body: some View {
        PickerView(
            title: title,
            options: state.options,
            value: $state.value,
            error: state.error,
            placeholder: placeholder,
            canClear: canClear,
            canSearch: canSearch,
            listStyle: listStyle
        )
    }
}

struct OptionsList<O: PickerOption>: View {
    @Environment(\.dismiss) var dismiss
    let title: String
    let options: [O]
    @Binding var selection: O?
    let canSearch: Bool
    let style: any ListStyle
    @State var searchText: String = ""
    var filteredOptions: [O] {
        if searchText.isEmpty {
            options
        } else {
            options.filter { $0.pickerOptionRepresentation.localizedStandardContains(searchText) }
        }
    }

    var body: some View {
        List(filteredOptions, id: \.self, selection: $selection) { option in
            HStack {
                Text(option.pickerOptionRepresentation)
                Spacer()
                Image(systemName: "checkmark")
                    .isHidden(selection != option)
                    .fontWeight(.medium)
                    .foregroundStyle(FormColors.button)
            }
            .contentShape(Rectangle())
            .onTapGesture {
                selection = option
                dismiss()
            }
            .listRowBackground(Color.dsSurface)
        }
        .modifier { AnyView($0.listStyle(style)) }
        .if(canSearch) {
            $0.searchable(text: $searchText, placement: .navigationBarDrawer(displayMode: .always))
        }
        .navigationTitle(title)
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct PickerViewPreview: View {
    let options: [String] = ["Sevilla", "Barcelona", "Madrid", "Zahara de los atunes"]
    @State var selection: String?
    @State var pickerState: PickerInputState<String> = .init(
        value: "Sevilla",
        options: ["Sevilla", "Barcelona", "Madrid", "Zahara de los atunes"],
        error: "Mensaje de error de prueba"
    )

    var body: some View {
        Group {
            PickerView(
                title: "Ciudad",
                options: options,
                value: $selection,
                placeholder: "Selecciona",
                canSearch: true
            )

            PickerInput(
                title: "Origen",
                state: $pickerState,
                placeholder: "Selecciona",
                canClear: true
            )

            PickerView(
                title: "Destino",
                options: options,
                value: $selection,
                placeholder: "Selecciona",
                canClear: true,
                listStyle: .plain
            )
        }
    }
}

#Preview {
    NavigationStack {
        Form {
            PickerViewPreview()
        }
        .navigationTitle("Form")
    }
}
