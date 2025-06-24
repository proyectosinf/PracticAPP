from fastapi import APIRouter, Depends

from app.modules.database_module.models.event_type import EventType
from app.modules.database_module.models.user_role import Role
from app.schemas.event_schema import EventInputSchema
from app.schemas.offer_schema import (
    OfferInputSchema,
    OfferOutputSchema,
    OfferPaginatedSchema,
)
from app.services.auth_service.role_dependency import role_required
from app.services.event_service.event_service import EventService
from app.services.offer_service.offer_service import OfferService

router = APIRouter()


@router.post("/", response_model=OfferOutputSchema)
async def register_offer(
    payload: OfferInputSchema,
    decoded_token: dict = Depends(role_required(Role.WORK_TUTOR)),
) -> OfferOutputSchema:
    return await OfferService.create_offer(payload, decoded_token.get("uid"))


@router.get("/paginated", response_model=OfferPaginatedSchema)
async def list_paginated_offers(
    page: int = 0,
    limit: int = 25,
    order: str = None,
    decoded_token: dict = Depends(role_required(Role.WORK_TUTOR, Role.STUDENT)),
) -> OfferPaginatedSchema:
    return await OfferService.get_all_offer_paginated(
        uid_user=decoded_token.get("uid"), page=page, limit=limit, order=order
    )


@router.get("/{offer_id}", response_model=OfferOutputSchema)
async def get_offer_by_id(
    offer_id: int,
    decoded_token: dict = Depends(role_required(Role.STUDENT)),
) -> OfferOutputSchema:
    event_schema = EventInputSchema(
        user_uid=decoded_token.get("uid"),
        type_entity=EventType.OFFER,
        id_entity=offer_id,
    )
    await EventService.post_event(event_schema)
    return await OfferService.get_offer_by_id(offer_id, decoded_token.get("uid"))
