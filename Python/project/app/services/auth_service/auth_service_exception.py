from app.schemas.base_schema import BaseException, BaseExceptionInfo


class AuthServiceExceptionInfo(BaseExceptionInfo):
    INVALID_TOKEN = (1000, "Invalid token provided", 401)
    UNAUTHORIZED = (1001, "You don't have permission", 401)
    CUSTOM_TOKEN_ERROR = (1002, "Error occurred with auth service", 500)
    USER_NOT_FOUND = (1003, "User not found", 404)


class AuthServiceException(BaseException):
    """
    Class that define an auth service exception
    """

    pass
