from fastapi import APIRouter, Depends

from app.schemas.work_tutor_schema import WorkTutorInputSchema, WorkTutorOutputSchema
from app.services.auth_service.firebase_auth import firebase_auth_service
from app.services.work_tutor_service.work_tutor_service import WorkTutorService

router = APIRouter()


@router.post("/", response_model=WorkTutorOutputSchema)
async def register_work_tutor(
    payload: WorkTutorInputSchema,
    decoded_token: dict = Depends(firebase_auth_service.verify_token),
) -> WorkTutorOutputSchema:
    return await WorkTutorService.create_student(payload)
