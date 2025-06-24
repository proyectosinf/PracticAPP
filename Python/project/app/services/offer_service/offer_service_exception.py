from app.schemas.base_schema import BaseException, BaseExceptionInfo


class OfferServiceExceptionInfo(BaseExceptionInfo):
    OFFER_FOUND_WITH_NAME = (6000, "Offer exists with name", 409)
    OFFER_FOUND_WITH_CIF = (6001, "Offer exists with CIF", 409)
    OFFER_NOT_FOUND = (6002, "Offer not found", 404)
    OFFER_TYPE_INCORRECT = (6003, "Incorrect offer type", 400)
    OFFER_DEGREE_INCORRECT = (6004, "Incorrect degree", 404)
    OFFER_FILE_ERROR = (6005, "Error loading or processing file", 500)
    OFFER_TUTOR_COMPANY_MISMATCH = (
        6006,
        "Work tutor does not belong to offer's company",
        400,
    )


class OfferServiceException(BaseException):
    pass
