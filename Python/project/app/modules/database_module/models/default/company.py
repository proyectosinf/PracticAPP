from tortoise import fields

from app.modules.database_module.models.database_model import DatabaseModel
from app.modules.database_module.models.default.offer import Offer
from app.modules.database_module.models.default.work_tutor import WorkTutor


class Company(DatabaseModel):
    name = fields.CharField(max_length=100, unique=True)
    sector = fields.TextField()
    web = fields.TextField(null=True)
    cif = fields.CharField(max_length=9, unique=True)
    logo = fields.TextField(null=True)
    security_code = fields.CharField(max_length=5, unique=True)

    # Relations
    work_tutors: fields.ReverseRelation["WorkTutor"]
    offers: fields.ReverseRelation["Offer"]
