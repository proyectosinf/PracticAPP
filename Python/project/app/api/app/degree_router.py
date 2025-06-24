from fastapi import APIRouter, Depends

from app.modules.database_module.models.user_role import Role
from app.schemas.degrees_schema import DegreesOutputSchema
from app.services.auth_service.role_dependency import role_required
from app.services.degree_service.degree_service import DegreeService

router = APIRouter()


@router.get("/{degree_id}", response_model=DegreesOutputSchema)
async def get_by_id(
    degree_id: int,
    decode_token: dict = Depends(role_required(Role.STUDENT, Role.WORK_TUTOR)),
) -> DegreesOutputSchema:
    return await DegreeService.get_degree_by_id(degree_id)


@router.get("/", response_model=list[DegreesOutputSchema])
async def get_all(
    decode_token: dict = Depends(role_required(Role.STUDENT, Role.WORK_TUTOR))
) -> list[DegreesOutputSchema]:
    return await DegreeService.get_all_degrees()
