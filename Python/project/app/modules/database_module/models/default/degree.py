from tortoise import fields

from app.modules.database_module.models.database_model import DatabaseModel


class Degree(DatabaseModel):
    name: str = fields.CharField(max_length=255, unique=True)
