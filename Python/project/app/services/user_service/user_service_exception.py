from app.schemas.base_schema import BaseException, BaseExceptionInfo


class UserServiceExceptionInfo(BaseExceptionInfo):
    USER_NOT_FOUND = (8000, "User not found", 404)
    USER_EXISTS_EMAIL = (8001, "User exists with given email", 409)
    USER_EXISTS_UID = (8002, "User exists with given UID", 409)
    USER_CREATE_ERROR = (8003, "Error when creating user", 500)
    USER_UPDATE_ERROR = (8004, "Error when updating user", 500)
    USER_DELETE_ERROR = (8005, "Error when deleting user", 500)


class UserServiceException(BaseException):
    pass
