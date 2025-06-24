import java.time.LocalDate

data class Offer(
    val id: Int,
    val title: String,
    val description: String? = null,
    val vacancies: Int,
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
    val views: Int,
    val type: Int,
    val address: String,
    val postalCode: String,
    val contactName: String,
    val contactEmail: String,
    val contactPhone: String? = null,
    val company: String,
    val degreeId: String,
    val degreeName: String,
    val companyPhoto: String,
    val inscribe: Boolean?,
    val presentationCard: String? = null,
    val inscriptionsCandidacy: Int,
)
