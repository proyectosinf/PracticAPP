import pytest
from requests import Response

from app.schemas.candidacy_schema import (
    CandidacyAcceptCancelSchema,
    CandidacyInputSchema,
)
from app.services.candidacity.candidacy_service_exception import (
    CandidacyServiceExceptionInfo,
)
from app.tests.constants import (
    ApiServices,
    Candidacy,
    Company,
    Offer,
    Student,
    UpdateCandidacy,
)


def verify_fields(candidacy_schema: CandidacyInputSchema, response: Response):
    assert response.status_code == 200

    # Verify all fields
    for key, value in candidacy_schema.model_dump(mode="json").items():
        assert response.json()[key] == value


@pytest.mark.usefixtures("test_app")
class TestCandidacy:
    def test_get_all_candidacy_paginated_none_candidacy(self, test_app):
        response = test_app.do_request_with_role(
            "STUDENT",
            "GET",
            ApiServices.APP_CANDIDACY_PAGINATED,
        )

        assert response.status_code == 200
        assert len(response.json()["items"]) == 0

    def test_create_candidacy(self, test_app):
        candidacy_schema = CandidacyInputSchema(**Candidacy.__dict__)
        response = test_app.do_request_with_role(
            "STUDENT",
            "POST",
            ApiServices.APP_CANDIDACY,
            data=candidacy_schema.model_dump(mode="json"),
        )

        assert response.status_code == 200

    def test_create_candidacy_no_available_slots(self, test_app):
        candidacy_schema = CandidacyInputSchema(**Candidacy.__dict__)
        response = test_app.do_request_with_role(
            "STUDENT",
            "POST",
            ApiServices.APP_CANDIDACY,
            data=candidacy_schema.model_dump(mode="json"),
        )

        assert (
            response.status_code
            == CandidacyServiceExceptionInfo.CANDIDACY_NO_AVAILABLE_SLOTS.value[2]
        )

    def test_create_candidacy_fails_if_student_already_registered(self, test_app):
        candidacy_schema = CandidacyInputSchema(**Candidacy.__dict__)
        response = test_app.do_request_with_role(
            "STUDENT",
            "POST",
            ApiServices.APP_CANDIDACY,
            data=candidacy_schema.model_dump(mode="json"),
        )

        assert (
            response.status_code
            == CandidacyServiceExceptionInfo.CANDIDACY_EXISTS_OFFER_STUDENT.value[2]
        )

    def test_get_all_candidacy_student_paginated(self, test_app):
        response = test_app.do_request_with_role(
            "STUDENT",
            "GET",
            ApiServices.APP_CANDIDACY_PAGINATED,
        )

        assert response.status_code == 200
        assert len(response.json()["items"]) >= 1
        assert response.json()["items"][0]["student_name"] == Student.name

    def test_get_all_candidacy_work_tutor_paginated(self, test_app):
        response = test_app.do_request_with_role(
            "WORK_TUTOR",
            "GET",
            ApiServices.APP_CANDIDACY_PAGINATED,
        )

        assert response.status_code == 200
        assert len(response.json()["items"]) >= 1
        assert response.json()["items"][0]["student_name"] == Student.name

    def test_get_all_candidacy_work_tutor_paginated_by_offer_id(self, test_app):
        response = test_app.do_request_with_role(
            "WORK_TUTOR",
            "GET",
            f"{ApiServices.APP_CANDIDACY_PAGINATED}/{1}",
        )

        assert response.status_code == 200
        assert len(response.json()["items"]) >= 1
        assert response.json()["items"][0]["student_name"] == Student.name

    def test_get_all_candidacy_work_tutor_paginated_by_offer_id_none_exist(
        self, test_app
    ):
        response = test_app.do_request_with_role(
            "WORK_TUTOR",
            "GET",
            f"{ApiServices.APP_CANDIDACY_PAGINATED}/{50}",
        )

        assert response.status_code == 200
        assert len(response.json()["items"]) == 0

    def test_update_candidacy(self, test_app):
        update_candidacy = CandidacyAcceptCancelSchema(**UpdateCandidacy.__dict__)
        response = test_app.do_request_with_role(
            "WORK_TUTOR",
            "PUT",
            endpoint=f"{ApiServices.APP_CANDIDACY_UPDATE}{1}",
            data=update_candidacy.model_dump(mode="json"),
        )

        assert response.status_code == 200
        assert response.json()["status"] == UpdateCandidacy.status
        assert response.json()["additional_notes"] == UpdateCandidacy.additional_notes

    def test_update_candidacy_none_exist(self, test_app):
        update_candidacy = CandidacyAcceptCancelSchema(**UpdateCandidacy.__dict__)
        response = test_app.do_request_with_role(
            "WORK_TUTOR",
            "PUT",
            endpoint=f"{ApiServices.APP_CANDIDACY_UPDATE}{20}",
            data=update_candidacy.model_dump(mode="json"),
        )

        assert response.status_code == 500

    def test_update_candidacy_with_role_student(self, test_app):
        update_candidacy = CandidacyAcceptCancelSchema(**UpdateCandidacy.__dict__)
        response = test_app.do_request_with_role(
            "STUDENT",
            "PUT",
            endpoint=f"{ApiServices.APP_CANDIDACY_UPDATE}{1}",
            data=update_candidacy.model_dump(mode="json"),
        )
        assert response.status_code == 401

    def test_get_candidacy_by_id(self, test_app):
        response = test_app.do_request_with_role(
            "WORK_TUTOR",
            "GET",
            f"{ApiServices.APP_CANDIDACY_BY_ID}{1}",
        )

        assert response.status_code == 200
        assert response.json()["offer_title"] == Offer.title
        assert response.json()["company_name"] == Company.name
        assert response.json()["student_photo"] == Student.photo

    def test_get_candidacy_by_id_none_exist(self, test_app):
        response = test_app.do_request_with_role(
            "WORK_TUTOR",
            "GET",
            f"{ApiServices.APP_CANDIDACY_BY_ID}{55}",
        )

        assert response.status_code == 404
