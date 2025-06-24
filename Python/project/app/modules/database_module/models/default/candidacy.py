from tortoise import fields

from app.modules.database_module.models.candidacy_status import CandidacyStatus
from app.modules.database_module.models.database_model import DatabaseModel


class Candidacy(DatabaseModel):
    status: CandidacyStatus = fields.IntField()
    additional_notes = fields.TextField(null=True)
    postulation_date = fields.DateField(auto_now_add=True)
    presentation_card = fields.TextField(null=True)

    # Relations
    student = fields.ForeignKeyField(
        "default.Student", related_name="student_candidacies"
    )
    offer = fields.ForeignKeyField("default.Offer", related_name="offer_candidacies")
