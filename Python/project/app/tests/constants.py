from datetime import datetime, timedelta

from app.modules.database_module.models.candidacy_status import CandidacyStatus
from app.modules.database_module.models.offer_type import OfferType


class ApiServices:
    APP_STUDENT = "/students/"
    APP_WORK_TUTOR = "/work_tutors/"
    APP_COMPANY = "/companies/"
    APP_COMPANY_CURRENT = "/companies/get_current_user_company"
    APP_COMPANY_SECURITY_CODE = "/companies/by-security-code/"
    APP_DEGREES = "/degrees/"
    APP_OFFER = "/offers/"
    APP_OFFER_PAGINATED = "/offers/paginated/"
    APP_CANDIDACY = "/candidacies/"
    APP_CANDIDACY_PAGINATED = "/candidacies/paginated"
    APP_CANDIDACY_BY_ID = "/candidacies/by-id/"
    APP_CANDIDACY_UPDATE = "/candidacies/update-state/"


class Credentials:
    CREDENTIALS = {
        "STUDENT": {"email": "tests@gmail.com", "password": "Usuario0?"},
        "STUDENT2": {"email": "antonio@gmail.com", "password": "Usuario0?"},
        "WORK_TUTOR": {"email": "test.worktutor@gmail.com", "password": "Usuario0?"},
        "WORK_TUTOR2": {
            "email": "antonio.worktutor@gmail.com",
            "password": "Usuario0?",
        },
        "WORK_TUTOR3": {
            "email": "david.worktutor@gmail.com",
            "password": "Usuario0?",
        },
    }


class UserError:
    uid = "JiNkdRFp65Y8d8D5454vQNbKfIc2"
    email = "david@gmail.com"
    none_email = "noexisteestecorreo@gmail.com"
    social_security_number = "159300055573"
    dni = "14141499P"


class Student:
    """
    Test student values
    """

    id = 1
    uid = "fRgTbQrFjXSRfRic6beacHeU4mf2"
    name = "Tests"
    surname = "Tests Prueba"
    role = 1
    email = "tests@gmail.com"
    dni = "14141414P"
    social_security_number = "159300034573"
    pdf_cv = "https://example.com/"
    contact_name = "Tests"
    contact_email = "tests@gmail.com"
    contact_phone = "600000000"
    photo = "https://example.com/"


class StudentNoneOptionals:
    """
    Test student values none optionals
    """

    id = 2
    uid = "uZhIbrjhmGdlqQTTMLWEvfVaAAa2"
    name = "Antonio"
    surname = "Gomez Camarena"
    role = 1
    email = "antonio@gmail.com"


class WorkTutor:
    """
    Test work tutor with optional values
    """

    uid = "91s41QwXz6Z11gm3SG78bty3sEd2"
    name = "Work"
    surname = "Tutor"
    email = "test.worktutor@gmail.com"
    role = 2
    photo = "https://example.com/photo.jpg"
    company_id = None


class WorkTutorOptional:
    """
    Test work tutor values
    """

    uid = "sTIjTe9C1UN3RWn0eRegE53MN8j2"
    name = "antonio"
    surname = "Work Tutor"
    email = "antonio.worktutor@gmail.com"
    role = 2


class WorkTutor3:
    uid = "ggLZX4UEIQN2A4fVkrzegHz1n243"
    name = "David"
    surname = "Castro"
    email = "david.worktutor@gmail.com"
    role = 2
    photo = "https://example.com/photo.jpg"
    company_id = 3


class Company:
    name = "Antonio"
    sector = "Informática"
    web = "https://antonio.com"
    cif = "A11111111"
    logo = None


class Degree:
    id = 50
    name = "Técnico Superior en Desarrollo de Aplicaciones Multiplataforma"


class Offer:
    title = "Oferta de antonio"
    description = "Descripción de la oferta"
    vacancies_number = 10
    start_date = (datetime.today() + timedelta(days=16)).date()
    end_date = (datetime.today() + timedelta(days=63)).date()

    type = OfferType.DISTANCE

    address = "Calle de prueba, 23"
    postal_code = "41530"

    contact_name = WorkTutor.name
    contact_email = WorkTutor.email
    contact_phone = "666666666"
    degree_id = Degree.id


class Offer2:
    title = "Oferta de antonio 2"
    description = "Descripción de la oferta 2"
    vacancies_number = 1
    start_date = (datetime.today() + timedelta(days=18)).date()
    end_date = (datetime.today() + timedelta(days=70)).date()

    type = OfferType.DISTANCE

    address = "Calle de prueba, 29"
    postal_code = "41530"

    contact_name = WorkTutor.name
    contact_email = WorkTutor.email
    contact_phone = "666666666"

    degree = "sadfsd"


class Candidacy:
    presentation_card = None
    offer_id = 1


class UpdateCandidacy:
    status = CandidacyStatus.APPROVED
    additional_notes = "Nos ha gustado tu perfil, esperamos verte pronto por aquí"
