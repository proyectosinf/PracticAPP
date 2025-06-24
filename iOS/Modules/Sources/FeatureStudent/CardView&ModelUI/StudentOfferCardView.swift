import SwiftUI
import AppCommon
import Kingfisher

struct CompanyLogoView: View {
    let url: String?
    private let size: CGFloat = 60

    var body: some View {
            ZStack {
                Circle()
                    .fill(Color.gray.opacity(0.1))
                    .frame(width: size, height: size)
                if let validUrl = url?.trimmingCharacters(in: .whitespacesAndNewlines), !validUrl.isEmpty {
                    KFImage(URL(string: validUrl))
                        .resizable()
                        .scaledToFill()
                        .frame(width: size, height: size)
                        .clipShape(Circle())
                } else {
                    Image("practicapp")
                        .resizable()
                        .scaledToFill()
                        .frame(width: size, height: size)
                        .clipShape(Circle())
                }
            }
            .overlay(Circle().stroke(Color.gray.opacity(0.3), lineWidth: 1))
        }
    }

struct StudentOfferCardView: View {
    let offer: StudentOfferUIModel
    let degreeName: String
    let companyName: String
    let companyLogoUrl: String?

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack(spacing: 12) {
                CompanyLogoView(url: companyLogoUrl)
                Text(offer.title)
                    .font(.title2)
                    .fontWeight(.medium)
                    .lineLimit(1)
                    .truncationMode(.tail)
            }
            Text(companyName)
                .font(.headline)
                .lineLimit(2)

            (Text(Strings.general.offer_list_date_offer_text).bold()
             + Text("\(FormattedDate.format(offer.startDate)) - \(FormattedDate.format(offer.endDate))"))
            .font(.subheadline)
            (Text(Strings.general.offer_list_formation_text).bold() + Text(degreeName))
                .font(.subheadline)
            (Text(Strings.general.offer_list_modality_text).bold() + Text("\(offer.type)"))
                .font(.subheadline)
            (Text(Strings.general.offer_list_vacancies_text).bold() + Text("\(offer.vacancies)"))
                .font(.subheadline)

            if offer.isSubscribed {
                Label(Strings.general.offer_list_inscribe_text, systemImage: "checkmark.seal.fill")
                    .foregroundColor(.green)
                    .font(.subheadline)
            }
        }
        .padding()
        .frame(maxWidth: .infinity, minHeight: 160, alignment: .topLeading)
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(Color(.systemBackground))
                .overlay(
                    RoundedRectangle(cornerRadius: 12)
                        .stroke(Color.gray.opacity(0.3), lineWidth: 1)
                )
                .shadow(color: Color.black.opacity(0.05), radius: 4, x: 0, y: 2)
        )
    }
}
