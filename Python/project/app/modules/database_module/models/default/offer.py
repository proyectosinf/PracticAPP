from tortoise import fields

from app.modules.database_module.models.database_model import DatabaseModel
from app.modules.database_module.models.offer_type import OfferType


class Offer(DatabaseModel):
    title = fields.CharField(max_length=255)
    description = fields.TextField()
    vacancies_number = fields.IntField()
    start_date = fields.DateField()
    end_date = fields.DateField(null=True)
    type: OfferType = fields.IntField()

    address = fields.CharField(max_length=100)
    postal_code = fields.CharField(max_length=10)

    # Contact
    contact_name = fields.CharField(max_length=100)
    contact_email = fields.CharField(max_length=255)
    contact_phone = fields.CharField(max_length=20, null=True)

    # Relations
    company = fields.ForeignKeyField("default.Company", related_name="offers")
    degree = fields.ForeignKeyField("default.Degree", related_name="degree")
