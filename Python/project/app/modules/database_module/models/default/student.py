from tortoise import fields

from .user import User


class Student(User):
    dni = fields.CharField(max_length=9, unique=True, null=True)
    social_security_number = fields.CharField(max_length=12, unique=True, null=True)
    pdf_cv = fields.TextField(null=True)
    contact_name = fields.CharField(max_length=100, null=True)
    contact_email = fields.CharField(max_length=255, null=True)
    contact_phone = fields.CharField(max_length=20, null=True)
