from app.schemas.base_schema import BaseException, BaseExceptionInfo


class DegreeServiceExceptionInfo(BaseExceptionInfo):
    DEGREES_NOT_FOUND = (4000, "Degree not found", 404)


class DegreeServiceException(BaseException):
    pass
