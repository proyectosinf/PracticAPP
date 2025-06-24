from app.modules.database_module.models.default import UserCombinedView
from app.modules.database_module.models.user_role import Role
from app.repositories.student_repository import StudentRepository
from app.repositories.work_tutor_repository import WorkTutorRepository
from app.schemas.student_schema import StudentOutputSchema
from app.schemas.work_tutor_schema import WorkTutorOutputSchema


class UserService:
    @staticmethod
    async def get_user_by_uid(
        user_uid: str,
    ) -> StudentOutputSchema | WorkTutorOutputSchema:
        user_aux = await UserCombinedView.filter(**{"uid": user_uid}).first()

        if user_aux.role == Role.STUDENT:
            student = await StudentRepository.get_user_by_uid(user_uid)
            return StudentOutputSchema(**student.__dict__)
        else:
            work_tutor = await WorkTutorRepository.get_work_tutor_by_uid(user_uid)
            return WorkTutorOutputSchema(**work_tutor.__dict__)
