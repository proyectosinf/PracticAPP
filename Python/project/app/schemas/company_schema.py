from typing import Annotated, Optional

from fastapi import Query
from pydantic import HttpUrl

from app.schemas.base_schema import BaseSchema


class CompanyOutputSchema(BaseSchema):
    id: int
    name: Annotated[str, Query(max_length=100)]
    sector: str
    web: Optional[HttpUrl] = None
    cif: str
    logo: Optional[HttpUrl] = None
    security_code: str


class CompanyInputSchema(BaseSchema):
    name: Annotated[str, Query(max_length=100)]
    sector: str
    web: Optional[HttpUrl] = None
    cif: Annotated[str, Query(pattern=r"^[A-Z]\d{8}$|^\d{8}[A-Z]$")]
    logo: Optional[HttpUrl] = None
