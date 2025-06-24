from fastapi import APIRouter
from fastapi.params import Depends

from app.schemas.student_schema import StudentOutputSchema
from app.schemas.work_tutor_schema import WorkTutorOutputSchema
from app.services.auth_service.firebase_auth import firebase_auth_service
from app.services.user_service.user_service import UserService

router = APIRouter()


@router.get(
    "/get_current_user", response_model=WorkTutorOutputSchema | StudentOutputSchema
)
async def get_current_user(
    decode_token: dict = Depends(firebase_auth_service.verify_token),
) -> StudentOutputSchema | WorkTutorOutputSchema:
    return await UserService.get_user_by_uid(decode_token.get("uid"))
