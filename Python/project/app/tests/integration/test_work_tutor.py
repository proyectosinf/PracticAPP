import pytest

from app.schemas.work_tutor_schema import WorkTutorInputSchema
from app.services.work_tutor_service.work_tutor_service_exception import (
    WorkTutorServiceExceptionInfo,
)
from app.tests.constants import (
    ApiServices,
    UserError,
    WorkTutor,
    WorkTutor3,
    WorkTutorOptional,
)


def assert_work_tutor_creation_error(
    work_tutor_schema: WorkTutorInputSchema,
    code: WorkTutorServiceExceptionInfo,
    test_app,
) -> None:
    """
    General function that creates a work tutor error
    """
    response = test_app.do_request_with_role(
        http_method="post",
        role="WORK_TUTOR",
        endpoint=ApiServices.APP_WORK_TUTOR,
        data=work_tutor_schema.model_dump(mode="json"),
    )
    assert response.status_code == code.value[2]


@pytest.mark.usefixtures("test_app")
class TestWorkTutor:

    def test_create_work_tutor_with_optionals_fields(self, test_app):
        """
        Test to create a work tutor with optionals fields
        """
        work_tutor_schema = WorkTutorInputSchema(**WorkTutor.__dict__)
        response = test_app.do_request_with_role(
            http_method="post",
            role="WORK_TUTOR",
            endpoint=ApiServices.APP_WORK_TUTOR,
            data=work_tutor_schema.model_dump(mode="json"),
        )

        assert response.status_code == 200
        assert response.json()["uid"] == WorkTutor.uid
        assert response.json()["name"] == WorkTutor.name
        assert response.json()["email"] == WorkTutor.email
        assert response.json()["photo"] == WorkTutor.photo
        assert response.json()["company_id"] == WorkTutor.company_id

    def test_create_work_tutor(self, test_app):
        """
        Test to create a work tutor
        """
        work_tutor_schema = WorkTutorInputSchema(**WorkTutorOptional.__dict__)
        response = test_app.do_request_with_role(
            http_method="post",
            role="WORK_TUTOR",
            endpoint=ApiServices.APP_WORK_TUTOR,
            data=work_tutor_schema.model_dump(mode="json"),
        )

        assert response.status_code == 200
        assert response.json()["uid"] == WorkTutorOptional.uid
        assert response.json()["name"] == WorkTutorOptional.name
        assert response.json()["email"] == WorkTutorOptional.email
        assert response.json()["role"] == WorkTutorOptional.role

    def test_create_work_tutor_with_nonexistent_uid(self, test_app) -> None:
        """
        Test to create a work tutor with uid doest exists
        """
        work_tutor_schema = WorkTutorInputSchema(**WorkTutor.__dict__)
        work_tutor_schema.uid = UserError.uid

        assert_work_tutor_creation_error(
            work_tutor_schema,
            WorkTutorServiceExceptionInfo.WORK_TUTOR_NOT_FOUND,
            test_app,
        )

    def test_create_work_tutor_with_nonexistent_email(self, test_app) -> None:
        """
        Test to create a student with email doest exists
        """
        work_tutor_schema = WorkTutorInputSchema(**WorkTutor.__dict__)
        work_tutor_schema.email = UserError.none_email

        assert_work_tutor_creation_error(
            work_tutor_schema,
            WorkTutorServiceExceptionInfo.WORK_TUTOR_NOT_FOUND,
            test_app,
        )

    def test_create_student_with_existing_email(self, test_app) -> None:
        """
        Test to create a student with email exists
        """
        work_tutor_schema = WorkTutorInputSchema(**WorkTutor.__dict__)
        work_tutor_schema.uid = WorkTutor.uid

        assert_work_tutor_creation_error(
            work_tutor_schema,
            WorkTutorServiceExceptionInfo.WORK_TUTOR_EXISTS_EMAIL,
            test_app,
        )

    def test_create_student_with_existing_uid(self, test_app) -> None:
        """
        Test to create a student with email exists
        """
        work_tutor_schema = WorkTutorInputSchema(**WorkTutor.__dict__)
        work_tutor_schema.email = WorkTutor.email

        assert_work_tutor_creation_error(
            work_tutor_schema,
            WorkTutorServiceExceptionInfo.WORK_TUTOR_EXISTS_UID,
            test_app,
        )

    def test_create_work_tutor_with_long_name(self, test_app):
        """
        Test to create a work tutor with a name that is too long
        """
        work_tutor_data = WorkTutorOptional.__dict__.copy()
        work_tutor_data["name"] = "a" * 101

        with pytest.raises(ValueError):
            WorkTutorInputSchema(**work_tutor_data)

    def test_create_work_tutor_with_long_surname(self, test_app):
        """
        Test to create a work tutor with a surname that is too long
        """
        work_tutor_data = WorkTutorOptional.__dict__.copy()
        work_tutor_data["surname"] = "a" * 101

        with pytest.raises(ValueError):
            WorkTutorInputSchema(**work_tutor_data)

    def test_create_work_tutor_with_nonexistent_company(self, test_app) -> None:
        """
        Test to create a student with email doest exists
        """
        work_tutor_schema = WorkTutorInputSchema(**WorkTutor3.__dict__)

        assert_work_tutor_creation_error(
            work_tutor_schema,
            WorkTutorServiceExceptionInfo.WORK_TUTOR_COMPANY_NOT_FOUND,
            test_app,
        )
