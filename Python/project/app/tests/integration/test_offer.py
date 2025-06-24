import datetime

import pytest
from requests import Response

from app.schemas.offer_schema import OfferInputSchema
from app.services.offer_service.offer_service_exception import OfferServiceExceptionInfo
from app.tests.constants import ApiServices, Offer


def verify_data(offer_schema: OfferInputSchema, response: Response) -> None:
    assert response.status_code == 200
    for key, value in offer_schema.model_dump().items():
        if key == "degree_id":
            continue
        if isinstance(value, (datetime.date, datetime.datetime)):
            assert response.json()[key] == value.isoformat()
        else:
            assert response.json()[key] == value


@pytest.mark.usefixtures("test_app")
class TestOffer:
    def test_paginated_offers_as_work_tutor_returns_not_found_when_no_offers(
        self, test_app
    ) -> None:
        response = test_app.do_request_with_role(
            http_method="GET",
            role="WORK_TUTOR",
            endpoint=f"{ApiServices.APP_OFFER_PAGINATED}",
        )

        assert response.status_code == 200
        assert len(response.json()["items"]) == 0

    def test_paginated_offers_as_student_returns_not_found_when_no_offers(
        self, test_app
    ) -> None:
        response = test_app.do_request_with_role(
            http_method="GET",
            role="STUDENT",
            endpoint=f"{ApiServices.APP_OFFER_PAGINATED}",
        )

        assert response.status_code == 200
        assert len(response.json()["items"]) == 0

    def test_create_offer(self, test_app) -> None:
        offer_schema = OfferInputSchema(**Offer.__dict__)
        response = test_app.do_request_with_role(
            http_method="POST",
            role="WORK_TUTOR",
            endpoint=ApiServices.APP_OFFER,
            data=offer_schema.model_dump(mode="json"),
        )

        verify_data(offer_schema, response)

    def test_create_offer_invalid_type(self, test_app) -> None:
        data = OfferInputSchema(**Offer.__dict__)
        data.type = "Offer"

        response = test_app.do_request_with_role(
            http_method="POST",
            role="WORK_TUTOR",
            endpoint=ApiServices.APP_OFFER,
            data=data.model_dump(mode="json"),
        )
        assert response.status_code == 422

    def test_create_offer_start_date_in_past(self, test_app) -> None:
        data = OfferInputSchema(**Offer.__dict__)
        data.start_date = datetime.date.today() - datetime.timedelta(days=1)

        response = test_app.do_request_with_role(
            http_method="POST",
            role="WORK_TUTOR",
            endpoint=ApiServices.APP_OFFER,
            data=data.model_dump(mode="json"),
        )

        assert response.status_code == 422

    def test_create_offer_end_date_before_start_date(self, test_app) -> None:
        data = OfferInputSchema(**Offer.__dict__)
        data.start_date = datetime.date.today() + datetime.timedelta(days=5)
        data.end_date = datetime.date.today() + datetime.timedelta(days=3)

        response = test_app.do_request_with_role(
            http_method="POST",
            role="WORK_TUTOR",
            endpoint=ApiServices.APP_OFFER,
            data=data.model_dump(mode="json"),
        )

        assert response.status_code == 422

    def test_create_offer_invalid_degree(self, test_app) -> None:
        data = OfferInputSchema(**Offer.__dict__)
        data.degree_id = 999999

        response = test_app.do_request_with_role(
            http_method="POST",
            role="WORK_TUTOR",
            endpoint=ApiServices.APP_OFFER,
            data=data.model_dump(mode="json"),
        )

        assert (
            response.status_code
            == OfferServiceExceptionInfo.OFFER_DEGREE_INCORRECT.value[2]
        )

    def test_create_offer_work_tutor_without_company(self, test_app) -> None:

        data = OfferInputSchema(**Offer.__dict__)

        response = test_app.do_request_with_role(
            http_method="POST",
            role="WORK_TUTOR2",
            endpoint=ApiServices.APP_OFFER,
            data=data.model_dump(mode="json"),
        )

        assert (
            response.status_code
            == OfferServiceExceptionInfo.OFFER_TUTOR_COMPANY_MISMATCH.value[2]
        )

    def test_get_all_offer_paginated_as_work_tutor(self, test_app) -> None:
        response = test_app.do_request_with_role(
            http_method="GET",
            role="WORK_TUTOR",
            endpoint=f"{ApiServices.APP_OFFER_PAGINATED}",
        )

        assert response.status_code == 200
        assert len(response.json()["items"]) >= 1

    def test_get_all_offer_paginated_as_student(self, test_app) -> None:
        response = test_app.do_request_with_role(
            http_method="GET",
            role="STUDENT",
            endpoint=f"{ApiServices.APP_OFFER_PAGINATED}",
        )

        assert response.status_code == 200
        assert len(response.json()["items"]) >= 1

    def test_get_offer_by_id_none_exist(self, test_app) -> None:
        response = test_app.do_request_with_role(
            http_method="GET",
            role="STUDENT",
            endpoint=f"{ApiServices.APP_OFFER}{20}",
        )
        assert response.status_code == 404

    def test_get_offer_by_id(self, test_app) -> None:
        response = test_app.do_request_with_role(
            http_method="GET",
            role="STUDENT",
            endpoint=f"{ApiServices.APP_OFFER}{1}",
        )
        assert response.status_code == 200
        assert response.json()["title"] == Offer.title
        assert response.json()["type"] == Offer.type
        assert response.json()["address"] == Offer.address
