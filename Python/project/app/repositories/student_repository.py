from app.modules.database_module import DatabaseModule
from app.modules.database_module.models.default import Student


class StudentRepository:
    @staticmethod
    async def create_user(payload: dict) -> Student:
        return await DatabaseModule.post_entity(Student, payload)

    @staticmethod
    async def get_user_by_uid(uid: str) -> Student:
        return await DatabaseModule.get_entity_filtered(Student, filters={"uid": uid})

    @staticmethod
    async def update_student(payload: dict, student_id: id) -> Student | None:
        return await DatabaseModule.put_entity(Student, payload, student_id)
