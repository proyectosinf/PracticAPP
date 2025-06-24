from app.modules.database_module import DatabaseModule
from app.modules.database_module.models.default import Event


class EventRepository:
    @staticmethod
    async def post_event(payload: dict) -> Event:
        return await DatabaseModule.post_entity(Event, payload)

    @staticmethod
    async def get_view_filtered(payload: dict) -> int:
        return len(await DatabaseModule.get_all_entity_filtered(Event, payload))
