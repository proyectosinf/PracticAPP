from tortoise import fields

from .user import User


class WorkTutor(User):
    # Relations
    company = fields.ForeignKeyField(
        "default.Company", related_name="work_tutors", null=True
    )
