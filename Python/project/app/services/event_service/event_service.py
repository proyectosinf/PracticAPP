from app.repositories.event_repository import EventRepository
from app.schemas.event_schema import (
    EventFilterSchema,
    EventInputSchema,
    EventOutputSchema,
)


class EventService:
    @staticmethod
    async def post_event(event: EventInputSchema) -> EventOutputSchema:
        event = await EventRepository.post_event(event.model_dump())

        return EventOutputSchema(**event.__dict__)

    @staticmethod
    async def get_view_filtered(payload: EventFilterSchema) -> int:
        return await EventRepository.get_view_filtered(payload.model_dump())
