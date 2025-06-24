from tortoise import fields

from app.modules.database_module.models.database_model import DatabaseModel


class UserCombinedView(DatabaseModel):
    uid = fields.CharField(max_length=255, null=True)
    email = fields.CharField(max_length=255)
    name = fields.CharField(max_length=100)
    surname = fields.CharField(max_length=100)
    role = fields.IntField()
    photo = fields.TextField(null=True)
    dni = fields.CharField(max_length=9, null=True)
    social_security_number = fields.CharField(max_length=12, null=True)
    user_type = fields.CharField(max_length=20)

    class Meta:
        table = "user_combined_view"
        managed = False
