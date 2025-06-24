import Foundation

public struct Candidacy: Codable, Identifiable, Equatable, Hashable, Sendable {
    public let id: Int
    public let status: Int
    public let postulationDate: String
    public let additionalNotes: String?
    public let presentationCard: String
    public let offerTitle: String
    public let companyName: String
    public let companyPhoto: String
    public let contactName: String
    public let contactEmail: String
    public let contactPhone: String?
    public let studentName: String
    public let studentSurname: String
    public let studentEmail: String
    public let studentPdf: String?
    public let studentPhoto: String?
    public init(
        id: Int,
        status: Int,
        postulationDate: String,
        additionalNotes: String,
        presentationCard: String,
        offerTitle: String,
        companyName: String,
        companyPhoto: String,
        contactName: String,
        contactEmail: String,
        contactPhone: String,
        studentName: String,
        studentSurname: String,
        studentEmail: String,
        studentPdf: String,
        studentPhoto: String
    ) {
        self.id = id
        self.status = status
        self.postulationDate = postulationDate
        self.additionalNotes = additionalNotes
        self.presentationCard = presentationCard
        self.offerTitle = offerTitle
        self.companyName = companyName
        self.companyPhoto = companyPhoto
        self.contactName = contactName
        self.contactEmail = contactEmail
        self.contactPhone = contactPhone
        self.studentName = studentName
        self.studentSurname = studentSurname
        self.studentEmail = studentEmail
        self.studentPdf = studentPdf
        self.studentPhoto = studentPhoto
    }

}
