import pytest

from app.services.degree_service.degree_service_exception import (
    DegreeServiceExceptionInfo,
)
from app.tests.constants import ApiServices, Degree


@pytest.mark.usefixtures("test_app")
class TestDegree:

    def test_get_all_degrees(self, test_app) -> None:
        response = test_app.do_request_with_role(
            http_method="get", role="WORK_TUTOR", endpoint=ApiServices.APP_DEGREES
        )

        assert response.status_code == 200

    def test_get_degree_by_id(self, test_app) -> None:
        response = test_app.do_request_with_role(
            http_method="get",
            role="WORK_TUTOR",
            endpoint=f"{ApiServices.APP_DEGREES}{Degree.id}",
        )
        assert response.status_code == 200
        assert response.json()["id"] == Degree.id
        assert response.json()["name"] == Degree.name

    def test_get_degree_by_id_not_found(self, test_app) -> None:
        response = test_app.do_request_with_role(
            http_method="get",
            role="WORK_TUTOR",
            endpoint=f"{ApiServices.APP_DEGREES}{999}",
        )

        assert (
            response.status_code
            == DegreeServiceExceptionInfo.DEGREES_NOT_FOUND.value[2]
        )
