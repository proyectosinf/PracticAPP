from datetime import timedelta

from tortoise.expressions import Q

from app.api.helpers.date_helper import utc_now
from app.modules.database_module import DatabaseModule
from app.modules.database_module.models.default import (
    AvailableOffer,
    Offer,
    WorkTutor,
)
from app.modules.database_module.models.event_type import EventType
from app.modules.database_module.models.user_role import Role
from app.repositories.offer_repository import OfferRepository
from app.schemas.candidacy_schema import CandidacyOutputSchema
from app.schemas.event_schema import EventFilterSchema
from app.schemas.offer_schema import (
    OfferInputSchema,
    OfferOutputSchema,
    OfferPaginatedSchema,
)
from app.services.candidacity.candidacy_service_exception import (
    CandidacyServiceException,
)
from app.services.degree_service.degree_service import DegreeService
from app.services.event_service.event_service import EventService
from app.services.offer_service.offer_service_exception import (
    OfferServiceException,
    OfferServiceExceptionInfo,
)
from app.services.user_service.user_service import UserService

DEFAULT_ORDER = "start_date"


class OfferService:

    @staticmethod
    async def create_offer(
        payload: OfferInputSchema, uid_work_tutor: str
    ) -> OfferOutputSchema:

        # Search for the degree
        await DegreeService.get_degree_by_id(payload.degree_id)

        # check work tutor contain company id
        work_tutor = await UserService.get_user_by_uid(uid_work_tutor)

        if not work_tutor.company_id:
            raise OfferServiceException(
                OfferServiceExceptionInfo.OFFER_TUTOR_COMPANY_MISMATCH
            )

        # create offer
        offer: Offer = await OfferRepository.create_offer(
            work_tutor.company_id, payload.model_dump()
        )

        if not offer:
            raise OfferServiceException(OfferServiceExceptionInfo.OFFER_NOT_FOUND)
        await offer.fetch_related("company", "degree")

        return OfferOutputSchema(
            company=offer.company.name,
            degree=offer.degree.name,
            company_photo=offer.company.logo,
            **offer.__dict__
        )

    @staticmethod
    async def get_all_offer_paginated(
        uid_user,
        page: int = 0,
        limit: int = 25,
        order: str = None,
    ) -> OfferPaginatedSchema:
        from app.services.candidacity.candidacy_service import CandidacyService

        if not order:
            order = DEFAULT_ORDER

        # Get user by UID
        user = await UserService.get_user_by_uid(uid_user)

        # Check role from user
        if user.role == Role.WORK_TUTOR:

            work_tutor: WorkTutor = await DatabaseModule.get_entity_filtered(
                WorkTutor, {"uid": user.uid}
            )

            offers = await OfferRepository.get_all_offer_paginated(
                Offer,
                page=page,
                limit=limit,
                filters={"company_id": work_tutor.company_id},
                order=order,
            )

        else:
            # Current date + 15 days
            filter_date = utc_now() + timedelta(days=15)
            offers = await OfferRepository.get_all_offer_paginated(
                AvailableOffer,
                page=page,
                limit=limit,
                q=Q(start_date__gt=filter_date),
                order=order,
            )

        # Verify candidate status before setting 'inscribe' state for the student
        if user.role == Role.STUDENT:

            list_offer: list[OfferOutputSchema] = []
            for offer in offers[0]:
                try:
                    candidacy = (
                        await CandidacyService.get_candidacy_by_user_id_and_offer_id(
                            user.id, offer.id
                        )
                    )
                except CandidacyServiceException:
                    candidacy = None

                await offer.fetch_related("company", "degree")
                event_schema = EventFilterSchema(
                    id_entity=offer.id, type_entity=EventType.OFFER
                )
                inscriptions_candidacy = (
                    await CandidacyService.get_all_count_candidacy_by_offer_id(offer.id)
                )
                offer.vacancies_number = offer.available_spots
                offer_output: OfferOutputSchema = OfferOutputSchema(
                    views=await EventService.get_view_filtered(event_schema),
                    inscriptions_candidacy=inscriptions_candidacy,
                    company=offer.company.name,
                    degree=offer.degree.name,
                    company_photo=offer.company.logo,
                    **offer.__dict__
                )

                if candidacy:
                    offer_output.inscribe = True

                if offer_output.vacancies_number > 0:
                    list_offer.append(offer_output)

            return OfferPaginatedSchema(items=list_offer, total=offers[1])

        else:
            list_offer: list[OfferOutputSchema] = []
            for offer in offers[0]:
                await offer.fetch_related("company", "degree")
                event_schema = EventFilterSchema(
                    id_entity=offer.id, type_entity=EventType.OFFER
                )
                #  verify total vacancy
                vacancy: int = (
                    await CandidacyService.count_approved_candidacies_by_offer(offer.id)
                )
                offer.vacancies_number = (
                    (offer.vacancies_number or 0) - vacancy
                    if (offer.vacancies_number or 0) - vacancy >= 0
                    else 0
                )
                inscriptions_candidacy = (
                    await CandidacyService.get_all_count_candidacy_by_offer_id(offer.id)
                )
                offer_output: OfferOutputSchema = OfferOutputSchema(
                    views=await EventService.get_view_filtered(event_schema),
                    inscriptions_candidacy=inscriptions_candidacy,
                    company=offer.company.name,
                    degree=offer.degree.name,
                    company_photo=offer.company.logo,
                    **offer.__dict__
                )

                list_offer.append(offer_output)
            return OfferPaginatedSchema(items=list_offer, total=offers[1])

    @staticmethod
    async def get_offer_by_id(offer_id: int, user_uid: str = None) -> OfferOutputSchema:
        from app.services.candidacity.candidacy_service import CandidacyService

        user = await UserService.get_user_by_uid(user_uid)

        offer = await OfferRepository.get_offer_by_id(offer_id)

        if not offer:
            raise OfferServiceException(OfferServiceExceptionInfo.OFFER_NOT_FOUND)
        await offer.fetch_related("company", "degree")
        event_schema = EventFilterSchema(
            id_entity=offer.id, type_entity=EventType.OFFER
        )
        candidacy: CandidacyOutputSchema | None = None
        try:
            candidacy = await CandidacyService.get_candidacy_by_user_id_and_offer_id(
                user.id, offer.id
            )
        except CandidacyServiceException:
            pass
        #  verify total vacancy
        vacancy: int = await CandidacyService.count_approved_candidacies_by_offer(
            offer.id
        )
        offer.vacancies_number = (
            (offer.vacancies_number or 0) - vacancy
            if (offer.vacancies_number or 0) - vacancy >= 0
            else 0
        )
        return OfferOutputSchema(
            views=await EventService.get_view_filtered(event_schema),
            inscribe=True if candidacy else None,
            company=offer.company.name,
            degree=offer.degree.name,
            company_photo=offer.company.logo,
            **offer.__dict__
        )
