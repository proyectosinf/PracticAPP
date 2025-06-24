import SwiftUI

public struct DesignSystemComponentsNavigation: View {
    public init() {}

    public var body: some View {
        NavigationStack {
            List {
                Section {
                    NavigationLink(Strings.general.components_sample_menu_textfields_button) {
                        TextFieldInput.Preview()
                            .navigationBar(color: .teal, scheme: .dark)
                    }
                    NavigationLink(Strings.general.components_sample_menu_pickers_button) {
                        PickersView()
                            .navigationBar(color: .indigo, scheme: .dark)
                    }
                    NavigationLink(Strings.general.components_sample_menu_buttons_button) {
                        ButtonsView()
                            .navigationBar(color: .mint)
                    }
                    NavigationLink(Strings.general.components_sample_menu_sign_up_example_button) {
                        SignUpSampleView()
                            .navigationBar(color: .cyan, scheme: .dark)
                    }
                } header: {
                    Text("Forms")
                }
            }
            .navigationTitle(Strings.general.components_sample_title_text)
            .navigationBar(color: .cyan, scheme: .dark)
        }
    }
}

#if DEBUG
#Preview("Navigation") { DesignSystemComponentsNavigation() }
#endif
