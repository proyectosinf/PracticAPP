import FoundationUtils
import AppCommon
import Domain

struct OfferTypeOption: PickerOption, Hashable {
    let type: OfferType

    var pickerOptionRepresentation: String {
        type.description
    }
}
