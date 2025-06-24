from typing import Optional

from pydantic import BaseModel, EmailStr, Field, HttpUrl

from app.schemas.user_schema import UserInputSchema, UserOutputSchema


class StudentBaseSchema(BaseModel):
    dni: Optional[str] = Field(default=None, pattern=r"^\d{8}[A-Za-z]$")
    social_security_number: Optional[str] = Field(default=None, pattern=r"^\d{12}$")
    pdf_cv: Optional[HttpUrl] = None
    contact_name: Optional[str] = Field(default=None, max_length=100)
    contact_email: Optional[EmailStr] = None
    contact_phone: Optional[str] = Field(default=None, pattern=r"^\d{9}$")


class StudentInputSchema(UserInputSchema, StudentBaseSchema):
    pass


class StudentOutputSchema(UserOutputSchema, StudentBaseSchema):
    pass
