from tortoise import fields

from app.modules.database_module.models.database_model import DatabaseModel
from app.modules.database_module.models.event_type import EventType


class Event(DatabaseModel):
    user_uid = fields.TextField()
    current_at = fields.DatetimeField(auto_now_add=True)
    type_entity = fields.IntEnumField(enum_type=EventType)
    id_entity = fields.IntField()
