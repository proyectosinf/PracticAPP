from typing import Optional

from app.schemas.user_schema import UserInputSchema, UserOutputSchema


class WorkTutorOutputSchema(UserOutputSchema):
    company_id: Optional[int] = None


class WorkTutorInputSchema(UserInputSchema):
    company_id: Optional[int] = None
