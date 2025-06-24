import FoundationUtils
import SwiftUI

struct PickersView: View {
    @State var dateIsOn: Bool = true
    @State var date: Date = .now
    @State var nullableDate: Date?
    @State var gender: String = "Ninguno"
    private let genders: [String] = ["Hombre", "Mujer"]
    @State var employment: String = ""
    @State var searchEmployment: String?
    private let employments: [String] = ["Arquitecto", "Ingeniero", "Policía", "Bombero", "Jardinero"]
    @State var city: String = "Madrid"
    @State var cities: [String] = ["Sevilla", "Madrid", "Barcelona"]
    @State var multiSelection: Set<String> = ["Sevilla", "Barcelona"]
    @State var cityState: PickerInputState<String> = .init(
        value: nil,
        options: ["Sevilla", "Madrid", "Barcelona", "Zahara de los atunes"],
        validator: .closure(error: "Si no eliges Sevilla te estás equivocando", validation: { $0 == "Sevilla" })
    )

    var body: some View {
        Form {
            Section {
                DatePicker(Strings.general.components_sample_picker_birth_date_text, selection: $date)
                    .font(.subheadline)

                DatePicker(
                    Strings.general.components_sample_picker_day_text,
                    selection: $date,
                    displayedComponents: .date
                )
                .font(.subheadline)

                DatePicker(
                    Strings.general.components_sample_picker_time_text,
                    selection: $date,
                    displayedComponents: .hourAndMinute
                )
                .font(.subheadline)

                VStack {
                    Toggle(Strings.general.components_sample_picker_hidden_with_check_text, isOn: $dateIsOn)
                        .font(.subheadline)
                    DatePicker(selection: $date, displayedComponents: .date) { HStack { Spacer() } }
                        .isHidden(!dateIsOn, remove: false)
                        .frame(height: dateIsOn ? nil : 0)
                }
            } header: {
                Text(Strings.general.components_sample_picker_header_text)
            }

            Section {
                Picker(selection: $gender) {
                    Section {
                        Text("Ninguno").tag("0")
                    }
                    Section {
                        ForEach(genders, id: \.self) { Text($0) }
                    } header: {
                        Text(Strings.general.components_sample_picker_gender_options_header_text)
                    }
                } label: {
                    VStack {
                        Text(Strings.general.components_sample_picker_gender_text)
                            .font(.subheadline)
                    }
                }
                PickerView(
                    title: Strings.general.components_sample_picker_list_with_search_text,
                    options: employments,
                    value: $searchEmployment,
                    placeholder: Strings.general.components_sample_picker_placeholder_text,
                    canSearch: true
                )
                PickerInput(
                    title: Strings.general.components_sample_picker_list_text,
                    state: $cityState,
                    placeholder: Strings.general.components_sample_picker_placeholder_text,
                    canClear: true
                )
            }

            Section {
                ForEach(cities, id: \.self) { tag in
                    MultiChoiceButton(tag, tag: tag, selection: $multiSelection)
                }
            } header: {
                Text(Strings.general.components_sample_picker_multiple_choice_text)
            }

            Section {
                ForEach(cities, id: \.self) { tag in
                    RadioButton(tag, tag: tag, selection: $city)
                }
            } header: {
                Text(Strings.general.components_sample_picker_single_choice_text)
            }

            Picker(Strings.general.components_sample_picker_inline_text, selection: $city) {
                ForEach(cities, id: \.self) { Text($0) }
            }
            .pickerStyle(.inline)

            Section {
                Picker(Strings.general.components_sample_picker_segmented_text, selection: $gender) {
                    ForEach(genders, id: \.self) { Text($0) }
                }
                .pickerStyle(.segmented)
            } header: {
                Text(Strings.general.components_sample_picker_segmented_text)
            }

            Section {
                Picker(Strings.general.components_sample_picker_wheel_text, selection: $city) {
                    ForEach(cities, id: \.self) { Text($0) }
                }
                .pickerStyle(.wheel)
            } header: {
                Text(Strings.general.components_sample_picker_wheel_text)
            }
        }
        .tint(.dsPrimary)
        .navigationTitle(Strings.general.components_sample_picker_title_text)
        .onAppear { cityState.validate() }
    }
}

#Preview { NavigationStack { PickersView() } }
