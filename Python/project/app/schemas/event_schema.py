from datetime import datetime

from app.modules.database_module.models.event_type import EventType
from app.schemas.base_schema import BaseSchema


class EventOutputSchema(BaseSchema):
    id: int
    user_uid: str
    current_at: datetime
    type_entity: EventType
    id_entity: int


class EventInputSchema(BaseSchema):
    user_uid: str
    type_entity: EventType
    id_entity: int


class EventFilterSchema(BaseSchema):
    type_entity: EventType
    id_entity: int
