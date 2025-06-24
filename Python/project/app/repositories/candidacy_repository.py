from tortoise.expressions import Q

from app.modules.database_module import DatabaseModule
from app.modules.database_module.models.candidacy_status import CandidacyStatus
from app.modules.database_module.models.default import Candidacy


class CandidacyRepository:

    @staticmethod
    async def create_candidacy(
        payload: dict,
    ) -> Candidacy | None:
        return await DatabaseModule.post_entity(Candidacy, payload)

    @staticmethod
    async def get_candidacy_by_user_id_and_offer_id(
        user_id: int, offer_id: int
    ) -> Candidacy | None:
        return await DatabaseModule.get_entity_filtered(
            Candidacy, {"offer_id": offer_id, "student_id": user_id}
        )

    @staticmethod
    async def get_all_candidacy_paginated(
        page: int = 0,
        limit: int = 25,
        q: Q = None,
        filters: dict = None,
        order: str = None,
    ) -> tuple[list[Candidacy], int] | None:
        return await DatabaseModule.get_all_entity_filtered_paginated(
            Candidacy, page, limit, q, filters, order
        )

    @staticmethod
    async def update_candidacy(identifier: int, payload: dict) -> Candidacy | None:
        return await DatabaseModule.put_entity(Candidacy, payload, identifier)

    @staticmethod
    async def get_candidacy_by_id(identifier: int) -> Candidacy | None:
        return await DatabaseModule.get_entity(Candidacy, identifier=identifier)

    @staticmethod
    async def get_approved_candidacy_count_by_offer(offer_id: int) -> int:
        return await Candidacy.filter(
            offer_id=offer_id, status=CandidacyStatus.APPROVED
        ).count()

    @staticmethod
    async def get_all_candidacy_by_offer_id(offer_id: int) -> int:
        return await DatabaseModule.get_all_count_entity_filtered(
            Candidacy, {"offer_id": offer_id}
        )
