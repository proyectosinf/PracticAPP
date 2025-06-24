from datetime import date
from typing import Annotated, Optional

from fastapi import Query
from pydantic import AfterValidator, EmailStr, HttpUrl, model_validator

from app.modules.database_module.models.offer_type import OfferType
from app.schemas.base_schema import BaseSchema

ContactPhone = Annotated[str, Query(max_length=20)]


def check_date_today(start_day: date) -> date:
    if (start_day - date.today()).days < 1:
        raise ValueError("The start date must be greater than today's date.")
    return start_day


class OfferOutputSchema(BaseSchema):
    id: int
    title: Annotated[str, Query(max_length=255)]
    description: str
    vacancies_number: Annotated[int, Query(ge=0)]
    start_date: date
    end_date: date
    views: Optional[int] = 0
    type: OfferType

    address: str
    postal_code: Annotated[str, Query(max_length=10)]

    contact_name: Annotated[str, Query(max_length=100)]
    contact_email: Annotated[EmailStr, Query(max_length=255)]
    contact_phone: Optional[ContactPhone] = None

    company: str
    degree: str
    company_photo: Optional[HttpUrl] = None

    inscribe: Optional[bool] = None
    inscriptions_candidacy: Optional[int] = 0


class OfferInputSchema(BaseSchema):
    title: Annotated[str, Query(max_length=255)]
    description: str
    vacancies_number: Annotated[int, Query(gt=0)]
    start_date: Annotated[date, AfterValidator(check_date_today)]
    end_date: date

    type: OfferType

    address: str
    postal_code: Annotated[str, Query(max_length=10)]

    contact_name: Annotated[str, Query(max_length=100)]
    contact_email: Annotated[EmailStr, Query(max_length=255)]
    contact_phone: Optional[ContactPhone] = None

    degree_id: int

    @model_validator(mode="after")
    def validate_dates(self) -> "OfferInputSchema":
        if self.end_date <= self.start_date:
            raise ValueError("The end date must be after the start date.")
        return self


class OfferPaginatedSchema(BaseSchema):
    items: list[OfferOutputSchema]
    total: int
