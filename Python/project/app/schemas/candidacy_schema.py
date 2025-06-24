from datetime import date
from typing import Optional

from pydantic import EmailStr, HttpUrl

from app.modules.database_module.models.candidacy_status import CandidacyStatus
from app.schemas.base_schema import BaseSchema


class CandidacyOutputSchema(BaseSchema):
    id: int
    status: CandidacyStatus
    additional_notes: Optional[str] = None
    postulation_date: date
    presentation_card: Optional[str] = None

    student_id: int
    offer_id: int
    inscribe: Optional[bool] = None


class CandidacyInputSchema(BaseSchema):
    presentation_card: Optional[str] = None
    offer_id: int


class CandidacyAcceptCancelSchema(BaseSchema):
    additional_notes: Optional[str] = None
    status: CandidacyStatus


class CandidacyOutputListSchema(BaseSchema):
    id: int
    status: CandidacyStatus
    postulation_date: date
    student_name: str
    student_surname: str
    offer_title: str
    company_name: str


class CandidacyPaginatedSchema(BaseSchema):
    items: list[CandidacyOutputListSchema]
    total: int


class CandidacyDetailsSchema(BaseSchema):
    # Candidacy
    id: int
    status: CandidacyStatus
    postulation_date: date
    additional_notes: Optional[str] = None
    presentation_card: Optional[str] = None
    # Offer
    offer_title: str
    contact_name: Optional[str] = None
    contact_email: Optional[str] = None
    contact_phone: Optional[str] = None
    # Company
    company_name: str
    company_photo: Optional[HttpUrl] = None
    # Student
    student_name: Optional[str] = None
    student_surname: Optional[str] = None
    student_email: EmailStr
    student_pdf: Optional[HttpUrl] = None
    student_photo: Optional[HttpUrl] = None
