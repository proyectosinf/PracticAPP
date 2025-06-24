from app.schemas.base_schema import BaseException, BaseExceptionInfo


class EventServiceExceptionInfo(BaseExceptionInfo):
    EVENT_CREATE_ERROR = (5000, "Error when creating event", 500)


class EventServiceException(BaseException):
    pass
