import FoundationUtils
import SwiftUI

public struct SignUpSampleView: View {
    public init() {}
    @State var name: InputState<String> = .init()
    @State var surname: InputState<String> = .init()
    @State var birthDate: InputState<Date> = .init(value: .now)
    @State var username: InputState<String> = .init()
    @State var gender: PickerInputState<String> = .init(
        value: "Female",
        options: ["Male", "Female"]
    )
    @State var nationality: PickerInputState<String> = .init(options: ["Española"])
    @State var description: InputState<String> = .init()
    @State var email: InputState<String> = .init()
    @State var password: InputState<String> = .init()
    @State var repeatPassword: InputState<String> = .init()
    @State var termsIsOn: Bool = false

    public var body: some View {
        Form {
            Section {
                TextFieldInput(title: "Name", state: $name, placeholder: "Required")
                TextFieldInput(title: "Surname", state: $surname, placeholder: "Required")
                DatePickerInput(
                    title: "Birthdate",
                    state: $birthDate,
                    displayedComponents: [.date]
                )
                TextFieldInput(title: "Nickname", state: $surname, placeholder: "Required")
                MenuPickerInput(title: "Gender", state: $gender)
                PickerInput(title: "Nationality", state: $nationality, placeholder: "Required", canClear: true)
            } header: {
                VStack(alignment: .center, spacing: .spacingS) {
                    Image("practicapp")
                        .resizable()
                        .frame(width: 90, height: 90, alignment: .center)
                        .aspectRatio(contentMode: .fit)
                        .foregroundStyle(Color.white)
                        .padding(.spacingXS)
                        .clipShape(Circle())
                    Text("About you")
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
            }

            Section {
                TextViewInput(
                    title: "Something about you",
                    state: $description,
                    placeholder: "Optional",
                    minHeight: 60
                )
            }

            Section {
                TextFieldInput(title: "Email", state: $email, placeholder: "Required")
                TextFieldInput(title: "Password", state: $password, placeholder: "Required")
                VStack(alignment: .leading, spacing: .spacingS) {
                    Text("Must have 8 characters longer, uppercase included and unless 1 special character as ¿?¡!*#")
                        .font(.caption)
                        .foregroundStyle(Color.dsOnSurfaceSecondary)
                    TextFieldInput(title: "Repeat password", state: $repeatPassword, placeholder: "Required")
                }
            } header: {
                Text("Data for your sign in")
            }

            VStack(spacing: .spacingL) {
                AgreementView(
                    text: "Acepto los [términos y condiciones](https://google.es) de esta App",
                    isAccepted: $termsIsOn,
                    openURLAction: { _ in }
                )
                .foregroundStyle(Color.dsOnSurface)
                .padding(.horizontal)

                Button("Submit") {}
                    .buttonStyle(.primary)
            }
            .listRowInsets(.horizontal(0))
            .listRowBackground(Color.clear)
        }
        .navigationTitle("Sign up", displayMode: .inline)
    }
}

#if DEBUG
#Preview {
    NavigationStack { SignUpSampleView() }
}
#endif
