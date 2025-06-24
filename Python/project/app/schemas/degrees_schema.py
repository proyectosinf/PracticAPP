from typing import Annotated

from fastapi import Query

from app.schemas.base_schema import BaseSchema


class DegreesInputSchema(BaseSchema):
    name: Annotated[str, Query(max_length=255)]


class DegreesOutputSchema(BaseSchema):
    id: int
    name: Annotated[str, Query(max_length=255)]
