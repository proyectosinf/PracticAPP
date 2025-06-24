from app.schemas.base_schema import BaseException, BaseExceptionInfo


class StudentServiceExceptionInfo(BaseExceptionInfo):
    STUDENT_NOT_FOUND = (7000, "Student not found", 404)
    STUDENT_EXISTS_EMAIL = (7001, "Student exists with given email", 409)
    STUDENT_EXISTS_SSN = (7002, "Student exists with given SSN", 409)
    STUDENT_EXISTS_DNI = (7003, "Student exists with given DNI", 409)
    STUDENT_EXISTS_UID = (7004, "Student exists with given UID", 409)
    STUDENT_CREATE_ERROR = (7005, "Error when creating student", 500)
    STUDENT_UPDATE_ERROR = (7006, "Error when updating student", 500)
    STUDENT_DELETE_ERROR = (7007, "Error when deleting student", 500)
    INVALID_STUDENT_SCHEMA = (7008, "Invalid data for student output", 400)


class StudentServiceException(BaseException):
    pass
