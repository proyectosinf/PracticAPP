import pytest

from app.schemas.student_schema import StudentInputSchema
from app.services.student_service.student_service_exception import (
    StudentServiceExceptionInfo,
)
from app.tests.constants import ApiServices, Student, StudentNoneOptionals, UserError


def assert_student_creation_error(
    student_schema: StudentInputSchema, code: StudentServiceExceptionInfo, test_app
):
    """
    General function that creates a student error
    """
    response = test_app.do_request_with_role(
        http_method="post",
        role="STUDENT",
        endpoint=ApiServices.APP_STUDENT,
        data={**student_schema.model_dump(mode="json")},
    )
    assert response.json()["code"] == code.value[0]
    assert response.json()["error"] == code.value[1]


@pytest.mark.usefixtures("test_app")
class TestStudent:

    def test_create_student(self, test_app) -> None:
        """
        Test to create a student
        """
        student = StudentInputSchema(**Student.__dict__)

        response = test_app.do_request_with_role(
            http_method="post",
            role="STUDENT",
            endpoint=ApiServices.APP_STUDENT,
            data={**student.model_dump(mode="json")},
        )

        assert response.status_code == 200
        assert response.json()["dni"] == Student.dni
        assert (
            response.json()["social_security_number"] == Student.social_security_number
        )
        assert response.json()["pdf_cv"] == str(Student.pdf_cv)
        assert response.json()["contact_name"] == Student.contact_name
        assert response.json()["contact_email"] == Student.contact_email
        assert response.json()["contact_phone"] == Student.contact_phone
        assert response.json()["uid"] == Student.uid
        assert response.json()["name"] == Student.name
        assert response.json()["surname"] == Student.surname
        assert response.json()["email"] == Student.email
        assert response.json()["role"] == Student.role
        assert response.json()["photo"] == str(Student.photo)

    def test_create_student_with_only_required_fields(self, test_app) -> None:
        """
        Test to create a student none optional values
        """
        student = StudentInputSchema(**StudentNoneOptionals.__dict__)

        response = test_app.do_request_with_role(
            http_method="post",
            role="STUDENT",
            endpoint=ApiServices.APP_STUDENT,
            data={**student.model_dump(mode="json")},
        )

        assert response.status_code == 200

        assert response.json()["uid"] == student.uid
        assert response.json()["name"] == student.name
        assert response.json()["surname"] == student.surname
        assert response.json()["email"] == student.email
        assert response.json()["role"] == student.role

    def test_create_student_with_nonexistent_uid(self, test_app) -> None:
        """
        Test to create a student with uid doest exists
        """
        student_schema = StudentInputSchema(**Student.__dict__)
        student_schema.uid = UserError.uid

        assert_student_creation_error(
            student_schema, StudentServiceExceptionInfo.STUDENT_NOT_FOUND, test_app
        )

    def test_create_student_with_existing_email(self, test_app) -> None:
        """
        Test to create a student with email doest exists
        """
        student_schema = StudentInputSchema(**Student.__dict__)
        student_schema.email = UserError.none_email

        assert_student_creation_error(
            student_schema, StudentServiceExceptionInfo.STUDENT_NOT_FOUND, test_app
        )

    def test_create_student_with_email_exist(self, test_app) -> None:
        """
        Test to create a student with email exists
        """
        student_schema = StudentInputSchema(**Student.__dict__)

        assert_student_creation_error(
            student_schema, StudentServiceExceptionInfo.STUDENT_EXISTS_EMAIL, test_app
        )

    def test_create_student_with_existing_dni(self, test_app) -> None:
        """
        Test to create a student with dni exists
        """
        student_schema = StudentInputSchema(**Student.__dict__)
        student_schema.email = UserError.email
        student_schema.social_security_number = UserError.social_security_number

        assert_student_creation_error(
            student_schema, StudentServiceExceptionInfo.STUDENT_EXISTS_DNI, test_app
        )

    def test_create_student_with_existing_social_security_number(
        self, test_app
    ) -> None:
        """
        Test to create a student with ssn exists
        """
        student_schema = StudentInputSchema(**Student.__dict__)
        student_schema.email = UserError.email
        student_schema.dni = UserError.dni

        assert_student_creation_error(
            student_schema, StudentServiceExceptionInfo.STUDENT_EXISTS_SSN, test_app
        )

    def test_create_student_with_existing_uid(self, test_app) -> None:
        """
        Test to create a student with uid exists
        """
        student_schema = StudentInputSchema(**Student.__dict__)
        student_schema.email = UserError.email
        student_schema.dni = UserError.dni
        student_schema.social_security_number = UserError.social_security_number

        assert_student_creation_error(
            student_schema, StudentServiceExceptionInfo.STUDENT_EXISTS_UID, test_app
        )

    def test_create_student_with_long_contact_name(self, test_app):
        """
        Test to create a student with a contact name that is too long
        """
        student_data = StudentNoneOptionals.__dict__.copy()
        student_data["contact_name"] = "a" * 101

        with pytest.raises(ValueError):
            StudentInputSchema(**student_data)

    def test_create_student_with_invalid_dni(self, test_app):
        """
        Test to create a student with an invalid DNI
        """
        student_data = StudentNoneOptionals.__dict__.copy()
        student_data["dni"] = "12345678"

        with pytest.raises(ValueError):
            StudentInputSchema(**student_data)

    def test_create_student_with_invalid_social_security_number(self, test_app):
        """
        Test to create a student with an invalid social security number
        """
        student_data = StudentNoneOptionals.__dict__.copy()
        student_data["social_security_number"] = "12345678901"

        with pytest.raises(ValueError):
            StudentInputSchema(**student_data)

    def test_create_student_with_invalid_contact_phone(self, test_app):
        """
        Test to create a student with an invalid contact phone
        """
        student_data = StudentNoneOptionals.__dict__.copy()
        student_data["contact_phone"] = "12345678"

        with pytest.raises(ValueError):
            StudentInputSchema(**student_data)
