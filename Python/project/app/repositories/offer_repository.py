from typing import Type

from tortoise.expressions import Q

from app.modules.database_module import DatabaseModule
from app.modules.database_module.models.database_model import DatabaseModel
from app.modules.database_module.models.default import AvailableOffer, Offer


class OfferRepository:

    @staticmethod
    async def create_offer(company_id: int, payload: dict) -> Offer | None:
        payload["views"] = 0
        payload["company_id"] = company_id
        return await DatabaseModule.post_entity(Offer, payload)

    @staticmethod
    async def get_all_offer_paginated(
        model: Type[DatabaseModel],
        page: int = 0,
        limit: int = 25,
        q: Q = None,
        filters: dict = None,
        order: str = None,
    ) -> tuple[list[Offer | AvailableOffer], int] | None:
        return await DatabaseModule.get_all_entity_filtered_paginated(
            model, page, limit, q, filters, order
        )

    @staticmethod
    async def get_offer_by_id(offer_id: int) -> Offer | None:
        return await DatabaseModule.get_entity(Offer, identifier=offer_id)
