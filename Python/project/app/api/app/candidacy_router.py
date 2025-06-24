from fastapi import APIRouter, Depends

from app.modules.database_module.models.user_role import Role
from app.schemas.candidacy_schema import (
    CandidacyAcceptCancelSchema,
    CandidacyDetailsSchema,
    CandidacyInputSchema,
    CandidacyOutputSchema,
    CandidacyPaginatedSchema,
)
from app.services.auth_service.role_dependency import role_required
from app.services.candidacity.candidacy_service import CandidacyService

router = APIRouter()


@router.post("/", response_model=CandidacyOutputSchema)
async def create_candidacy(
    payload: CandidacyInputSchema,
    decoded_token: dict = Depends(role_required(Role.STUDENT)),
) -> CandidacyOutputSchema:
    return await CandidacyService.create_candidacy(payload, decoded_token.get("uid"))


@router.get("/paginated", response_model=CandidacyPaginatedSchema)
async def list_paginated_candidacy(
    page: int = 0,
    limit: int = 25,
    order: str = None,
    decoded_token: dict = Depends(role_required(Role.WORK_TUTOR, Role.STUDENT)),
) -> CandidacyPaginatedSchema:
    return await CandidacyService.get_all_candidacies_paginated(
        user_uid=decoded_token.get("uid"), page=page, limit=limit, order=order
    )


@router.get("/paginated/{offer_id}", response_model=CandidacyPaginatedSchema)
async def list_paginated_candidacy_by_offer_id(
    offer_id: int,
    page: int = 0,
    limit: int = 25,
    order: str = None,
    decoded_token: dict = Depends(role_required(Role.WORK_TUTOR)),
) -> CandidacyPaginatedSchema:
    return await CandidacyService.get_all_candidacies_paginated_by_offer_id(
        offer_id, user_uid=decoded_token.get("uid"), page=page, limit=limit, order=order
    )


@router.put("/update-state/{candidacy_id}", response_model=CandidacyOutputSchema)
async def update_candidacy(
    candidacy_id: int,
    payload: CandidacyAcceptCancelSchema,
    decoded_token: dict = Depends(role_required(Role.WORK_TUTOR)),
) -> CandidacyOutputSchema:
    return await CandidacyService.update_candidacy(candidacy_id, payload)


@router.get("/by-id/{candidacy_id}", response_model=CandidacyDetailsSchema)
async def get_candidacy_by_id(
    candidacy_id: int,
    decode_token: dict = Depends(role_required(Role.WORK_TUTOR, Role.STUDENT)),
) -> CandidacyDetailsSchema:
    return await CandidacyService.get_candidacy_by_id(candidacy_id)
