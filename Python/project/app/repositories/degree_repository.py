from app.modules.database_module import DatabaseModule
from app.modules.database_module.models.default import Degree
from app.schemas.degrees_schema import DegreesOutputSchema


class DegreeRepository:
    @staticmethod
    async def get_all() -> list[DegreesOutputSchema] | None:
        return await DatabaseModule.get_all_entity_filtered(Degree)

    @staticmethod
    async def get_by_id(degree_id: int) -> DegreesOutputSchema | None:
        return await DatabaseModule.get_entity(Degree, identifier=degree_id)
