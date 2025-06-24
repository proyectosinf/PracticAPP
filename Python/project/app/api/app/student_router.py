from fastapi import APIRouter, Depends

from app.schemas.student_schema import StudentInputSchema, StudentOutputSchema
from app.services.auth_service.firebase_auth import firebase_auth_service
from app.services.student_service.student_service import StudentService

router = APIRouter()


@router.post("/", response_model=StudentOutputSchema)
async def register_student(
    payload: StudentInputSchema,
    decoded_token: dict = Depends(firebase_auth_service.verify_token),
) -> StudentOutputSchema:
    return await StudentService.create_student(payload)
