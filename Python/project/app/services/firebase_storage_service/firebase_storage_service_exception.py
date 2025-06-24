from app.schemas.base_schema import BaseException, BaseExceptionInfo


class FirebaseStorageServiceExceptionInfo(BaseExceptionInfo):
    INVALID_IMAGE_FILE = (3001, "The uploaded file is not a valid image", 400)
    INVALID_PDF_FILE = (3002, "The uploaded file is not a valid PDF document", 400)
    INVALID_CLASS_TYPE = (3003, "Class not found", 404)


class FirebaseStorageServiceException(BaseException):
    pass
