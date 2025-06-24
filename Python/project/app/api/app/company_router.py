from fastapi import APIRouter, Depends

from app.modules.database_module.models.user_role import Role
from app.schemas.company_schema import CompanyInputSchema, CompanyOutputSchema
from app.services.auth_service.role_dependency import role_required
from app.services.company_service.company_service import CompanyService

router = APIRouter()


@router.post("/", response_model=CompanyOutputSchema)
async def register_company(
    payload: CompanyInputSchema,
    decoded_token: dict = Depends(role_required(Role.WORK_TUTOR)),
) -> CompanyOutputSchema:
    return await CompanyService.create_company(payload, decoded_token.get("uid"))


@router.get("/by-security-code/{code}", response_model=int)
async def resolve_company_id(
    code: str, decode_token: dict = Depends(role_required(Role.WORK_TUTOR))
) -> int:
    return await CompanyService.get_company_id_by_security_code(code)


@router.get("/get_current_user_company", response_model=CompanyOutputSchema)
async def get_current_user_company(
    decode_token: dict = Depends(role_required(Role.WORK_TUTOR)),
) -> CompanyOutputSchema:
    return await CompanyService.get_current_user_company(decode_token.get("uid"))
