from app.schemas.base_schema import BaseException, BaseExceptionInfo


class CompanyServiceExceptionInfo(BaseExceptionInfo):
    COMPANY_FOUND_WITH_NAME = (3000, "Company exists with name", 409)
    COMPANY_FOUND_WITH_CIF = (3001, "Company exists with CIF", 409)
    COMPANY_NOT_FOUND = (3002, "Company not found", 404)
    COMPANY_CREATE_ERROR = (3003, "Error when creating company", 500)


class CompanyServiceException(BaseException):
    pass
