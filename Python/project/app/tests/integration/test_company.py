import pytest
from requests.models import Response

from app.schemas.company_schema import CompanyInputSchema, CompanyOutputSchema
from app.services.company_service.company_service_exception import (
    CompanyServiceExceptionInfo,
)
from app.services.work_tutor_service.work_tutor_service_exception import (
    WorkTutorServiceExceptionInfo,
)
from app.tests.constants import ApiServices, Company


def verify_fields(
    company_schema: CompanyInputSchema | CompanyOutputSchema, response: Response
):
    assert response.status_code == 200

    # Verify all fields
    for key, value in company_schema.model_dump(mode="json").items():
        assert response.json()[key] == value


@pytest.mark.usefixtures("test_app")
class TestCompany:

    def test_create_company(self, test_app):
        company_schema = CompanyInputSchema(**Company.__dict__)

        response = test_app.do_request_with_role(
            "WORK_TUTOR",
            "POST",
            ApiServices.APP_COMPANY,
            data=company_schema.model_dump(mode="json"),
        )
        verify_fields(company_schema, response)

    def test_create_company_work_tutor_contain_company(self, test_app) -> None:
        company_schema = CompanyInputSchema(**Company.__dict__)
        response = test_app.do_request_with_role(
            "WORK_TUTOR",
            "POST",
            ApiServices.APP_COMPANY,
            data=company_schema.model_dump(mode="json"),
        )

        assert (
            response.status_code
            == WorkTutorServiceExceptionInfo.WORK_TUTOR_CONTAIN_COMPANY.value[2]
        )

    def test_create_company_with_exits_name(self, test_app) -> None:
        company_schema = CompanyInputSchema(**Company.__dict__)
        responses = test_app.do_request_with_role(
            "WORK_TUTOR2",
            "POST",
            ApiServices.APP_COMPANY,
            data=company_schema.model_dump(mode="json"),
        )

        assert (
            responses.status_code
            == CompanyServiceExceptionInfo.COMPANY_FOUND_WITH_NAME.value[2]
        )

    def test_create_company_with_exits_cif(self, test_app) -> None:
        company_schema = CompanyInputSchema(**Company.__dict__)
        company_schema.name = "TestCompany"
        responses = test_app.do_request_with_role(
            "WORK_TUTOR2",
            "POST",
            ApiServices.APP_COMPANY,
            data=company_schema.model_dump(mode="json"),
        )

        assert (
            responses.status_code
            == CompanyServiceExceptionInfo.COMPANY_FOUND_WITH_CIF.value[2]
        )

    def test_get_current_company(self, test_app) -> None:
        response = test_app.do_request_with_role(
            "WORK_TUTOR",
            "GET",
            ApiServices.APP_COMPANY_CURRENT,
        )
        company_schema = CompanyInputSchema(**Company.__dict__)
        verify_fields(company_schema, response)

    def test_get_current_company_dont_exist(self, test_app) -> None:
        responses = test_app.do_request_with_role(
            "WORK_TUTOR2",
            "GET",
            ApiServices.APP_COMPANY_CURRENT,
        )

        assert (
            responses.status_code
            == CompanyServiceExceptionInfo.COMPANY_NOT_FOUND.value[2]
        )

    def test_get_company_by_security_code(self, test_app) -> None:

        response_get_company = test_app.do_request_with_role(
            "WORK_TUTOR",
            "GET",
            ApiServices.APP_COMPANY_CURRENT,
        )
        security_code = response_get_company.json()["security_code"]
        response = test_app.do_request_with_role(
            "WORK_TUTOR",
            "GET",
            f"{ApiServices.APP_COMPANY_SECURITY_CODE}{security_code}",
        )
        assert response.status_code == 200
        assert response.json() == response_get_company.json()["id"]

    def test_get_company_by_security_code_not_found(self, test_app) -> None:

        response_get_company = test_app.do_request_with_role(
            "WORK_TUTOR",
            "GET",
            ApiServices.APP_COMPANY_CURRENT,
        )
        security_code = int(response_get_company.json()["security_code"]) - 1
        response = test_app.do_request_with_role(
            "WORK_TUTOR",
            "GET",
            f"{ApiServices.APP_COMPANY_SECURITY_CODE}{security_code}",
        )
        assert (
            response.status_code
            == CompanyServiceExceptionInfo.COMPANY_NOT_FOUND.value[2]
        )
