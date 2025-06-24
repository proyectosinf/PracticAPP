from firebase_admin import auth
from firebase_admin.auth import UserNotFoundError

from app.modules.database_module import DatabaseModule
from app.modules.database_module.models.default import Company, UserCombinedView
from app.repositories.work_tutor_repository import WorkTutorRepository
from app.schemas.work_tutor_schema import WorkTutorInputSchema, WorkTutorOutputSchema
from app.services.work_tutor_service.work_tutor_service_exception import (
    WorkTutorServiceException,
    WorkTutorServiceExceptionInfo,
)


class WorkTutorService:
    @staticmethod
    async def create_student(payload: WorkTutorInputSchema) -> WorkTutorOutputSchema:
        # Verify exist uid en firebase
        try:
            auth.get_user(payload.uid)
            auth.get_user_by_email(payload.email)
        except UserNotFoundError:
            raise WorkTutorServiceException(
                WorkTutorServiceExceptionInfo.WORK_TUTOR_NOT_FOUND
            )

        uniqueness_checks = [
            ("email", WorkTutorServiceExceptionInfo.WORK_TUTOR_EXISTS_EMAIL),
            ("uid", WorkTutorServiceExceptionInfo.WORK_TUTOR_EXISTS_UID),
        ]
        for field, error_info in uniqueness_checks:
            users = await DatabaseModule.get_entity_filtered(
                UserCombinedView, {field: payload.model_dump()[field]}
            )
            if users:
                raise WorkTutorServiceException(error_info)

        # Check company id exist

        if payload.company_id:
            company = await DatabaseModule.get_entity_filtered(
                Company, {"id": payload.company_id}
            )
            if not company:
                raise WorkTutorServiceException(
                    WorkTutorServiceExceptionInfo.WORK_TUTOR_COMPANY_NOT_FOUND
                )

        work_tutor = await WorkTutorRepository.create_user(payload.model_dump())

        return WorkTutorOutputSchema(**work_tutor.__dict__)

    @staticmethod
    async def update_work_tutor(
        payload: dict, work_tutor_id: int
    ) -> WorkTutorOutputSchema:
        work_tutor = await WorkTutorRepository.update_work_tutor(payload, work_tutor_id)

        return WorkTutorOutputSchema(**work_tutor.__dict__)
