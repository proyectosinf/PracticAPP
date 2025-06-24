from tortoise.expressions import Q

from app.api.helpers.date_helper import utc_now
from app.modules.database_module.models.candidacy_status import CandidacyStatus
from app.modules.database_module.models.user_role import Role
from app.repositories.candidacy_repository import CandidacyRepository
from app.schemas.candidacy_schema import (
    CandidacyAcceptCancelSchema,
    CandidacyDetailsSchema,
    CandidacyInputSchema,
    CandidacyOutputSchema,
    CandidacyPaginatedSchema,
)
from app.schemas.student_schema import StudentOutputSchema
from app.services.candidacity.candidacy_service_exception import (
    CandidacyServiceException,
    CandidacyServiceExceptionInfo,
)
from app.services.helpers.build_paginated import HelperService
from app.services.offer_service.offer_service import OfferService
from app.services.user_service.user_service import UserService

DEFAULT_ORDER = "-postulation_date"


class CandidacyService:
    @staticmethod
    async def create_candidacy(
        payload: CandidacyInputSchema, user_uid
    ) -> CandidacyOutputSchema:

        student: StudentOutputSchema = await UserService.get_user_by_uid(user_uid)

        offer = await OfferService.get_offer_by_id(payload.offer_id, user_uid)
        # Verify that the student does not have an application with the same offer ID
        candidacy = None
        try:
            candidacy = await CandidacyService.get_candidacy_by_user_id_and_offer_id(
                student.id, payload.offer_id
            )
        except CandidacyServiceException:
            pass

        if candidacy:
            raise CandidacyServiceException(
                CandidacyServiceExceptionInfo.CANDIDACY_EXISTS_OFFER_STUDENT
            )

        if offer.vacancies_number is not None and offer.vacancies_number <= 0:
            raise CandidacyServiceException(
                CandidacyServiceExceptionInfo.CANDIDACY_NO_AVAILABLE_SLOTS
            )

        data = payload.model_dump()
        data["postulation_date"] = utc_now().date()
        data["student_id"] = student.id
        data["status"] = CandidacyStatus.PENDING
        candidacy = await CandidacyRepository.create_candidacy(data)

        return CandidacyOutputSchema(inscribe=True, **candidacy.__dict__)

    @staticmethod
    async def get_candidacy_by_user_id_and_offer_id(
        user_id: int, offer_id: int
    ) -> CandidacyOutputSchema:
        candidacy = await CandidacyRepository.get_candidacy_by_user_id_and_offer_id(
            user_id, offer_id
        )

        if not candidacy:
            raise CandidacyServiceException(
                CandidacyServiceExceptionInfo.CANDIDACY_NOT_FOUND
            )

        return CandidacyOutputSchema(**candidacy.__dict__)

    @staticmethod
    async def get_all_candidacies_paginated(
        user_uid: str,
        page: int = 0,
        limit: int = 25,
        order: str = None,
    ) -> CandidacyPaginatedSchema:
        if not order:
            order = DEFAULT_ORDER

        user = await UserService.get_user_by_uid(user_uid)

        if user.role == Role.STUDENT:
            candidacies = await CandidacyRepository.get_all_candidacy_paginated(
                page=page,
                limit=limit,
                filters={"student_id": user.id},
                order=order,
            )
        else:
            candidacies = await CandidacyRepository.get_all_candidacy_paginated(
                page=page,
                limit=limit,
                q=Q(offer__company_id=user.company_id),
                order=order,
            )

        return await HelperService.build_paginated_response(candidacies)

    @staticmethod
    async def get_all_candidacies_paginated_by_offer_id(
        offer_id: int,
        user_uid: str,
        page: int = 0,
        limit: int = 25,
        order: str = None,
    ) -> CandidacyPaginatedSchema:
        if not order:
            order = DEFAULT_ORDER

        user = await UserService.get_user_by_uid(user_uid)

        candidacies = await CandidacyRepository.get_all_candidacy_paginated(
            page=page,
            limit=limit,
            q=Q(offer__company_id=user.company_id),
            filters={"offer_id": offer_id},
            order=order,
        )

        return await HelperService.build_paginated_response(candidacies)

    @staticmethod
    async def update_candidacy(
        identifier: int, candidacy: CandidacyAcceptCancelSchema
    ) -> CandidacyOutputSchema:

        candidacy = await CandidacyRepository.update_candidacy(
            identifier=identifier, payload=candidacy.model_dump()
        )
        if not candidacy:
            raise CandidacyServiceException(
                CandidacyServiceExceptionInfo.CANDIDACY_UPDATE_ERROR
            )

        return CandidacyOutputSchema(inscribe=True, **candidacy.__dict__)

    @staticmethod
    async def get_candidacy_by_id(identifier: int) -> CandidacyDetailsSchema:
        candidacy = await CandidacyRepository.get_candidacy_by_id(identifier)

        if not candidacy:
            raise CandidacyServiceException(
                CandidacyServiceExceptionInfo.CANDIDACY_NOT_FOUND
            )

        await candidacy.fetch_related("student", "offer")

        await candidacy.offer.fetch_related("company")
        return CandidacyDetailsSchema(
            offer_title=candidacy.offer.title,
            company_name=candidacy.offer.company.name,
            company_photo=candidacy.offer.company.logo,
            contact_name=candidacy.offer.contact_name,
            contact_email=candidacy.offer.contact_email,
            contact_phone=candidacy.offer.contact_phone,
            student_name=candidacy.student.name,
            student_surname=candidacy.student.surname,
            student_pdf=candidacy.student.pdf_cv,
            student_photo=candidacy.student.photo,
            student_email=candidacy.student.email,
            **candidacy.__dict__
        )

    @staticmethod
    async def count_approved_candidacies_by_offer(
        offer_id: int,
    ) -> int:
        return await CandidacyRepository.get_approved_candidacy_count_by_offer(offer_id)

    @staticmethod
    async def get_all_count_candidacy_by_offer_id(offer_id: int) -> int:
        return await CandidacyRepository.get_all_candidacy_by_offer_id(offer_id)
