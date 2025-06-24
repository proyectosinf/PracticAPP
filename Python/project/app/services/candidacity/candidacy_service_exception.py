from app.schemas.base_schema import BaseException, BaseExceptionInfo


class CandidacyServiceExceptionInfo(BaseExceptionInfo):
    CANDIDACY_CREATE_ERROR = (2000, "Error when creating candidacy", 500)
    CANDIDACY_NOT_FOUND = (2001, "Candidacy not found", 404)
    CANDIDACY_UPDATE_ERROR = (2002, "Error when updating candidacy", 500)
    CANDIDACY_EXISTS_OFFER_STUDENT = (
        2003,
        "Candidacy exists with given offer and student",
        409,
    )
    CANDIDACY_NO_AVAILABLE_SLOTS = (2004, "No available slots for this offer", 409)


class CandidacyServiceException(BaseException):
    pass
