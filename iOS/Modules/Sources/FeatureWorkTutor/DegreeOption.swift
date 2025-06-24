import Domain
import AppCommon
import FoundationUtils

struct DegreeOption: PickerOption, Hashable {
    let degree: Degree

    var pickerOptionRepresentation: String {
        degree.name
    }
}
