from firebase_admin import auth
from firebase_admin.auth import UserNotFoundError

from app.modules.database_module import DatabaseModule
from app.modules.database_module.models.default import UserCombinedView
from app.repositories.student_repository import StudentRepository
from app.schemas.student_schema import StudentInputSchema, StudentOutputSchema
from app.services.student_service.student_service_exception import (
    StudentServiceException,
    StudentServiceExceptionInfo,
)


class StudentService:
    @staticmethod
    async def create_student(payload: StudentInputSchema) -> StudentOutputSchema:

        # Verify exist uid en firebase
        try:
            auth.get_user(payload.uid)
            auth.get_user_by_email(payload.email)
        except UserNotFoundError:
            raise StudentServiceException(StudentServiceExceptionInfo.STUDENT_NOT_FOUND)

        # Verify unique
        uniqueness_checks = [
            ("email", StudentServiceExceptionInfo.STUDENT_EXISTS_EMAIL),
            ("dni", StudentServiceExceptionInfo.STUDENT_EXISTS_DNI),
            ("social_security_number", StudentServiceExceptionInfo.STUDENT_EXISTS_SSN),
            ("uid", StudentServiceExceptionInfo.STUDENT_EXISTS_UID),
        ]

        for field, error_info in uniqueness_checks:
            if payload.model_dump().get(field):
                users = await DatabaseModule.get_entity_filtered(
                    UserCombinedView, {field: payload.model_dump()[field]}
                )
                if users:
                    raise StudentServiceException(error_info)

        student = await StudentRepository.create_user(payload.model_dump())

        return StudentOutputSchema(**student.__dict__)

    @staticmethod
    async def update_student(payload: dict, student_id: int) -> StudentOutputSchema:
        student = await StudentRepository.update_student(payload, student_id)
        if not student:
            StudentServiceException(StudentServiceExceptionInfo.STUDENT_UPDATE_ERROR)

        return StudentOutputSchema(**student.__dict__)
