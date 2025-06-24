from tortoise import fields

from app.modules.database_module.models.database_model import DatabaseModel

# TODO: Avoid inheritance from Offer due to ForeignKeyFields with related_name,
# which cause reverse relation duplication errors like:
# tortoise.exceptions.ConfigurationError: backward relation "offers"
# duplicates in model Company


class AvailableOffer(DatabaseModel):
    title = fields.CharField(max_length=255)
    description = fields.TextField()
    vacancies_number = fields.IntField()
    start_date = fields.DateField()
    end_date = fields.DateField(null=True)
    type = fields.IntField()

    address = fields.CharField(max_length=100)
    postal_code = fields.CharField(max_length=10)

    contact_name = fields.CharField(max_length=100)
    contact_email = fields.CharField(max_length=255)
    contact_phone = fields.CharField(max_length=20, null=True)

    approved_count = fields.IntField()
    available_spots = fields.IntField()

    company = fields.ForeignKeyField(
        "default.Company", related_name="available_offers", null=True
    )
    degree = fields.ForeignKeyField(
        "default.Degree", related_name="available_offers", null=True
    )

    class Meta:
        table = "available_offers_view"
        managed = False
