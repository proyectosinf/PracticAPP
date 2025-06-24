from fastapi import UploadFile

from app.api.helpers.date_helper import utc_now
from app.modules.database_module.models.model_type_photo import ModelTypePhoto
from app.repositories.firebase_storage_repository import FirebaseStorageRepository
from app.services.company_service.company_service import CompanyService
from app.services.file_converter_service.file_converter_service import (
    FileConverterService,
)
from app.services.firebase_storage_service.firebase_storage_service_exception import (
    FirebaseStorageServiceException,
    FirebaseStorageServiceExceptionInfo,
)
from app.services.student_service.student_service import StudentService
from app.services.user_service.user_service import UserService
from app.services.work_tutor_service.work_tutor_service import WorkTutorService


class FirebaseStorageService:
    @staticmethod
    async def upload_image(
        model: ModelTypePhoto, file: UploadFile, user_id: str
    ) -> str:
        if not file.content_type.startswith("image/"):
            raise FirebaseStorageServiceException(
                FirebaseStorageServiceExceptionInfo.INVALID_IMAGE_FILE
            )

        if model not in ModelTypePhoto:
            raise FirebaseStorageServiceException(
                FirebaseStorageServiceExceptionInfo.INVALID_CLASS_TYPE
            )
        url_photo: str = ""
        buffer = FileConverterService.convert_to_png(file)
        if model == ModelTypePhoto.STUDENT:
            student = await UserService.get_user_by_uid(user_id)
            buffer.name = f"{user_id}{utc_now()}.png"
            previous_url = student.photo
            url_photo = FirebaseStorageRepository.upload_image_to_firebase(buffer)
            await StudentService.update_student({"photo": url_photo}, student.id)
            if previous_url:
                FirebaseStorageRepository.delete_image_to_firebase(str(previous_url))

        elif model == ModelTypePhoto.WORK_TUTOR:
            buffer.name = f"{user_id}{utc_now()}.png"
            url_photo = FirebaseStorageRepository.upload_image_to_firebase(buffer)
            work_tutor = await UserService.get_user_by_uid(user_id)
            previous_url = work_tutor.photo
            await WorkTutorService.update_work_tutor(
                {"photo": url_photo}, work_tutor.id
            )
            if previous_url:
                FirebaseStorageRepository.delete_image_to_firebase(str(previous_url))
        elif model == ModelTypePhoto.COMPANY:
            company = await CompanyService.get_current_user_company(user_id)
            buffer.name = f"{user_id}company{company.cif}{utc_now()}.png"
            previous_url = company.logo
            url_photo = FirebaseStorageRepository.upload_image_to_firebase(buffer)
            await CompanyService.update_company({"logo": url_photo}, company.id)
            if previous_url:
                FirebaseStorageRepository.delete_image_to_firebase(str(previous_url))
        return url_photo

    @staticmethod
    async def upload_pdf(file: UploadFile, user_id: str) -> str:
        if file.content_type != "application/pdf":
            raise FirebaseStorageServiceException(
                FirebaseStorageServiceExceptionInfo.INVALID_PDF_FILE
            )

        student = await UserService.get_user_by_uid(user_id)

        url_photo = FirebaseStorageRepository.upload_pdf_to_firebase(file, user_id)
        await StudentService.update_student({"pdf_cv": url_photo}, student.id)

        return url_photo
