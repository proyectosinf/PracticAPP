from tortoise import fields

from app.modules.database_module.models.database_model import DatabaseModel
from app.modules.database_module.models.user_role import Role


class User(DatabaseModel):
    uid = fields.CharField(max_length=255, unique=True, null=True)
    email = fields.CharField(max_length=255, unique=True)
    name = fields.CharField(max_length=100)
    surname = fields.CharField(max_length=100)
    role: Role = fields.IntField()
    photo = fields.TextField(null=True)

    class Meta:
        abstract = True
