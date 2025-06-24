import FoundationUtils
import SwiftUI

public struct DatePickerView: View {
    let title: String
    @Binding var value: Date
    let error: String?
    let displayedComponents: DatePicker<EmptyView>.Components
    let closedRange: ClosedRange<Date>?
    let partialRangeFrom: PartialRangeFrom<Date>?
    let partialRangeThrough: PartialRangeThrough<Date>?

    private init(
        title: String,
        value: Binding<Date>,
        error: String? = nil,
        components: DatePicker<EmptyView>.Components,
        closedRange: ClosedRange<Date>? = nil,
        partialRangeFrom: PartialRangeFrom<Date>? = nil,
        partialRangeThrough: PartialRangeThrough<Date>? = nil
    ) {
        self.title = title
        _value = value
        self.error = error
        displayedComponents = components
        self.closedRange = closedRange
        self.partialRangeFrom = partialRangeFrom
        self.partialRangeThrough = partialRangeThrough
    }

    init(
        title: String,
        value: Binding<Date>,
        error: String?,
        displayedComponents: DatePicker<EmptyView>.Components = [.hourAndMinute, .date]
    ) {
        self.init(title: title, value: value, error: error, components: displayedComponents)
    }

    init(
        title: String,
        value: Binding<Date>,
        error: String?,
        in closedRange: ClosedRange<Date>,
        displayedComponents: DatePicker<EmptyView>.Components = [.hourAndMinute, .date]
    ) {
        self.init(title: title, value: value, error: error, components: displayedComponents, closedRange: closedRange)
    }

    init(
        title: String,
        value: Binding<Date>,
        error: String?,
        in partialRangeFrom: PartialRangeFrom<Date>,
        displayedComponents: DatePicker<EmptyView>.Components = [.hourAndMinute, .date]
    ) {
        self.init(
            title: title,
            value: value,
            error: error,
            components: displayedComponents,
            partialRangeFrom: partialRangeFrom
        )
    }

    init(
        title: String,
        value: Binding<Date>,
        error: String?,
        in partialRangeThrough: PartialRangeThrough<Date>,
        displayedComponents: DatePicker<EmptyView>.Components = [.hourAndMinute, .date]
    ) {
        self.init(
            title: title,
            value: value,
            error: error,
            components: displayedComponents,
            partialRangeThrough: partialRangeThrough
        )
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: .spacingXXXS) {
            if let closedRange {
                DatePicker(
                    title,
                    selection: $value,
                    in: closedRange,
                    displayedComponents: displayedComponents
                )
                .font(.subheadline)
                .foregroundStyle((error?.isEmpty ?? true) ? Color.dsOnSurface : .dsError)
            } else if let partialRangeFrom {
                DatePicker(
                    title,
                    selection: $value,
                    in: partialRangeFrom,
                    displayedComponents: displayedComponents
                )
                .font(.subheadline)
                .foregroundStyle((error?.isEmpty ?? true) ? Color.dsOnSurface : .dsError)
            } else if let partialRangeThrough {
                DatePicker(
                    title,
                    selection: $value,
                    in: partialRangeThrough,
                    displayedComponents: displayedComponents
                )
                .font(.subheadline)
                .foregroundStyle((error?.isEmpty ?? true) ? Color.dsOnSurface : .dsError)
            } else {
                DatePicker(
                    title,
                    selection: $value,
                    displayedComponents: displayedComponents
                )
                .font(.subheadline)
                .foregroundStyle((error?.isEmpty ?? true) ? Color.dsOnSurface : .dsError)
            }

            if let error, !error.isEmpty {
                Text(error)
                    .font(.caption)
                    .foregroundStyle(Color.dsError)
            }
        }
    }
}

public struct DatePickerInput: View {
    let title: String
    @Binding var state: InputState<Date>
    let displayedComponents: DatePicker<EmptyView>.Components
    let closedRange: ClosedRange<Date>?
    let partialRangeFrom: PartialRangeFrom<Date>?
    let partialRangeThrough: PartialRangeThrough<Date>?

    private init(
        title: String,
        state: Binding<InputState<Date>>,
        components: DatePicker<EmptyView>.Components,
        closedRange: ClosedRange<Date>? = nil,
        partialRangeFrom: PartialRangeFrom<Date>? = nil,
        partialRangeThrough: PartialRangeThrough<Date>? = nil
    ) {
        self.title = title
        _state = state
        displayedComponents = components
        self.closedRange = closedRange
        self.partialRangeFrom = partialRangeFrom
        self.partialRangeThrough = partialRangeThrough
    }

    public init(
        title: String,
        state: Binding<InputState<Date>>,
        displayedComponents: DatePicker<EmptyView>.Components = [.date, .hourAndMinute]
    ) {
        self.init(title: title, state: state, components: displayedComponents)
    }

    public init(
        title: String,
        state: Binding<InputState<Date>>,
        displayedComponents: DatePicker<EmptyView>.Components = [.date, .hourAndMinute],
        in closedRange: ClosedRange<Date>
    ) {
        self.init(title: title, state: state, components: displayedComponents, closedRange: closedRange)
    }

    public init(
        title: String,
        state: Binding<InputState<Date>>,
        displayedComponents: DatePicker<EmptyView>.Components = [.date, .hourAndMinute],
        in partialRangeFrom: PartialRangeFrom<Date>
    ) {
        self.init(
            title: title,
            state: state,
            components: displayedComponents,
            partialRangeFrom: partialRangeFrom
        )
    }

    public init(
        title: String,
        state: Binding<InputState<Date>>,
        displayedComponents: DatePicker<EmptyView>.Components = [.date, .hourAndMinute],
        in partialRangeThrough: PartialRangeThrough<Date>
    ) {
        self.init(
            title: title,
            state: state,
            components: displayedComponents,
            partialRangeThrough: partialRangeThrough
        )
    }

    public var body: some View {
        if let closedRange {
            DatePickerView(
                title: title,
                value: $state.value.defaultValue(.now),
                error: state.error,
                in: closedRange,
                displayedComponents: displayedComponents
            )
        } else if let partialRangeFrom {
            DatePickerView(
                title: title,
                value: $state.value.defaultValue(.now),
                error: state.error,
                in: partialRangeFrom,
                displayedComponents: displayedComponents
            )
        } else if let partialRangeThrough {
            DatePickerView(
                title: title,
                value: $state.value.defaultValue(.now),
                error: state.error,
                in: partialRangeThrough,
                displayedComponents: displayedComponents
            )
        } else {
            DatePickerView(
                title: title,
                value: $state.value.defaultValue(.now),
                error: state.error,
                displayedComponents: displayedComponents
            )
        }
    }
}

struct DatePickerPreview: View {
    @State var birthdate: InputState<Date> = .init(
        error: "Debes ser mayor de edad",
        validator: .closure(
            error: "Debes ser mayor de edad",
            validation: { date in
                guard let date else { return false }
                return date.year + 18 <= Date.now.year
            }
        )
    )
    @State var expiration: InputState<Date> = .init()
    @State var hour: InputState<Date> = .init()

    var body: some View {
        Group {
            DatePickerInput(
                title: "Fecha y hora",
                state: $expiration
            )

            DatePickerInput(
                title: "Fecha de nacimiento",
                state: $birthdate,
                displayedComponents: .date
            )

            DatePickerInput(
                title: "Hora",
                state: $hour,
                displayedComponents: .hourAndMinute
            )

            DatePickerInput(title: "Hasta hoy", state: $hour, displayedComponents: .date, in: ...Date())

            DatePickerInput(title: "Desde hoy", state: $hour, displayedComponents: .date, in: Date()...)

            DatePickerInput(title: "Hoy", state: $hour, displayedComponents: .date, in: Date.now ... Date.now)
        }
    }
}

#Preview { NavigationStack { Form { DatePickerPreview() } } }
