import SwiftUI
import Domain

public struct CandidacyCardView: View {
    let item: CandidacyListItem

    public init(item: CandidacyListItem) {
        self.item = item
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            HStack(spacing: 6) {
                Image(systemName: "person.fill")
                    .foregroundColor(.gray)
                Text("\(item.studentName) \(item.studentSurname)")
                    .fontWeight(.bold)
                    .font(.title3)
                Spacer()
                Text(statusText)
                    .font(.caption)
                    .padding(.horizontal, 8)
                    .padding(.vertical, 4)
                    .background(statusColor.opacity(0.2))
                    .foregroundColor(statusColor)
                    .clipShape(Capsule())
            }

            HStack(spacing: 6) {
                Image(systemName: "calendar")
                    .foregroundColor(.gray)
                Text(Strings.general.candidacy_postulation_text)
                    .bold()
                Spacer()
                Text(FormattedDate.format(item.postulationDate))
            }
        }
        .padding()
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.white)
        .cornerRadius(12)
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(Color.gray.opacity(0.2), lineWidth: 2)
        )
        .shadow(color: .black.opacity(0.05), radius: 4, x: 0, y: 2)
    }

    private var statusText: String {
        switch item.status {
        case 1: return "Pendiente"
        case 2: return "Aceptada"
        case 3: return "Rechazada"
        default: return "Desconocido"
        }
    }

    private var statusColor: Color {
        switch item.status {
        case 1: return .orange
        case 2: return .green
        case 3: return .red
        default: return .gray
        }
    }
}
