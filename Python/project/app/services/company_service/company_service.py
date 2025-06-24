from app.modules.database_module import DatabaseModule
from app.modules.database_module.models.default import Company
from app.repositories.company_repository import CompanyRepository
from app.schemas.company_schema import CompanyInputSchema, CompanyOutputSchema
from app.services.company_service.company_service_exception import (
    CompanyServiceException,
    CompanyServiceExceptionInfo,
)
from app.services.user_service.user_service import UserService
from app.services.work_tutor_service.work_tutor_service import WorkTutorService
from app.services.work_tutor_service.work_tutor_service_exception import (
    WorkTutorServiceException,
    WorkTutorServiceExceptionInfo,
)


class CompanyService:
    @staticmethod
    async def create_company(
        payload: CompanyInputSchema, uid: str
    ) -> CompanyOutputSchema:
        work_tutor_schema = await UserService.get_user_by_uid(uid)

        if work_tutor_schema.company_id:
            raise WorkTutorServiceException(
                WorkTutorServiceExceptionInfo.WORK_TUTOR_CONTAIN_COMPANY
            )
        check_fields = [
            ("name", CompanyServiceExceptionInfo.COMPANY_FOUND_WITH_NAME),
            ("cif", CompanyServiceExceptionInfo.COMPANY_FOUND_WITH_CIF),
        ]

        for field, error in check_fields:
            company_exist: Company = await DatabaseModule.get_entity_filtered(
                Company, filters={field: payload.model_dump()[field]}
            )

            if company_exist:
                raise CompanyServiceException(error)

        company = await CompanyRepository.create_company(payload=payload.model_dump())

        await WorkTutorService.update_work_tutor(
            payload={"company_id": company.id}, work_tutor_id=work_tutor_schema.id
        )

        return CompanyOutputSchema(**company.__dict__)

    @staticmethod
    async def get_company_id_by_security_code(security_code: str) -> int:
        company_id = await CompanyRepository.get_company_id_by_security_code(
            security_code
        )
        if not company_id:
            raise CompanyServiceException(CompanyServiceExceptionInfo.COMPANY_NOT_FOUND)
        return company_id

    @staticmethod
    async def get_current_user_company(work_tutor_uid) -> CompanyOutputSchema:
        work_tutor = await UserService.get_user_by_uid(work_tutor_uid)

        if not work_tutor.company_id:
            raise CompanyServiceException(CompanyServiceExceptionInfo.COMPANY_NOT_FOUND)

        company = await CompanyRepository.get_company_by_id(work_tutor.company_id)

        return CompanyOutputSchema(**company.__dict__)

    @staticmethod
    async def update_company(payload: dict, company_id: int) -> CompanyOutputSchema:
        company = await CompanyRepository.update_student(payload, company_id)
        if not company:
            CompanyServiceException(CompanyServiceExceptionInfo.COMPANY_UPDATE_ERROR)

        return CompanyOutputSchema(**company.__dict__)
