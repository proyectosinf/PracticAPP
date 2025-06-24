import SwiftUI

struct ButtonsView: View {
    var body: some View {
        List {
            Section {
                PrimaryButtonsPreview()
            } header: {
                Text(Strings.general.components_sample_buttons_primary_text)
            }
            .listRowSeparator(.hidden)

            Section {
                SecondaryButtonsPreview()
            } header: {
                Text(Strings.general.components_sample_buttons_secondary_text)
            }
            .listRowSeparator(.hidden)

            Section {
                TertiaryButtonsPreview()
            } header: {
                Text(Strings.general.components_sample_buttons_tertiary_text)
            }
            .listRowSeparator(.hidden)
        }
        .listStyle(.plain)
        .navigationTitle(Strings.general.components_sample_menu_buttons_button)
    }
}

#Preview { ButtonsView() }
