from app.modules.database_module import DatabaseModule
from app.modules.database_module.models.default import WorkTutor


class WorkTutorRepository:
    @staticmethod
    async def create_user(payload: dict) -> WorkTutor:
        return await DatabaseModule.post_entity(WorkTutor, payload)

    @staticmethod
    async def get_work_tutor_by_uid(uid: str) -> WorkTutor:
        return await DatabaseModule.get_entity_filtered(WorkTutor, filters={"uid": uid})

    @staticmethod
    async def update_work_tutor(payload: dict, work_tutor_id: id) -> WorkTutor | None:
        return await DatabaseModule.put_entity(WorkTutor, payload, work_tutor_id)
