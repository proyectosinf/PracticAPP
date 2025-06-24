from app.repositories.degree_repository import DegreeRepository
from app.schemas.degrees_schema import DegreesOutputSchema
from app.services.degree_service.degree_service_exception import (
    DegreeServiceException,
    DegreeServiceExceptionInfo,
)


class DegreeService:
    @staticmethod
    async def get_all_degrees() -> list[DegreesOutputSchema]:

        list_degree = await DegreeRepository.get_all()

        return [DegreesOutputSchema(**_.__dict__) for _ in list_degree]

    @staticmethod
    async def get_degree_by_id(degree_id) -> DegreesOutputSchema:

        degree = await DegreeRepository.get_by_id(degree_id)
        if not degree:
            raise DegreeServiceException(DegreeServiceExceptionInfo.DEGREES_NOT_FOUND)

        return DegreesOutputSchema(**degree.__dict__)
