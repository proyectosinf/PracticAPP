from app.schemas.base_schema import BaseException, BaseExceptionInfo


class WorkTutorServiceExceptionInfo(BaseExceptionInfo):
    WORK_TUTOR_NOT_FOUND = (9000, "Work tutor not found", 404)
    WORK_TUTOR_COMPANY_NOT_FOUND = (9001, "Work tutor's company not found", 404)
    WORK_TUTOR_EXISTS_EMAIL = (9002, "Work tutor exists with given email", 409)
    WORK_TUTOR_CONTAIN_COMPANY = (9003, "Work tutor already linked to company", 409)
    WORK_TUTOR_EXISTS_UID = (9004, "Work tutor exists with given UID", 409)
    WORK_TUTOR_CREATE_ERROR = (9005, "Error when creating work tutor", 500)
    WORK_TUTOR_UPDATE_ERROR = (9006, "Error when updating work tutor", 500)
    WORK_TUTOR_DELETE_ERROR = (9007, "Error when deleting work tutor", 500)
    INVALID_WORK_TUTOR_SCHEMA = (9008, "Invalid data for work tutor output", 400)


class WorkTutorServiceException(BaseException):
    pass
