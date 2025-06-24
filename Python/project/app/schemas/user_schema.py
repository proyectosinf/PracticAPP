from typing import Annotated, Optional

from fastapi.params import Query
from pydantic import EmailStr, HttpUrl

from app.modules.database_module.models.user_role import Role
from app.schemas.base_schema import BaseSchema


class UserOutputSchema(BaseSchema):
    id: int
    uid: str
    name: Annotated[str, Query(max_length=100)]
    surname: Annotated[str, Query(max_length=100)]
    email: EmailStr
    role: Role
    photo: Optional[HttpUrl] = None


class UserInputSchema(BaseSchema):
    uid: str
    name: Annotated[str, Query(max_length=100)]
    surname: Annotated[str, Query(max_length=100)]
    email: EmailStr
    role: Role
    photo: Optional[HttpUrl] = None
