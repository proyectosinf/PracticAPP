from fastapi import APIRouter, File, UploadFile
from fastapi.params import Depends

from app.modules.database_module.models.model_type_photo import ModelTypePhoto
from app.modules.database_module.models.user_role import Role
from app.services.auth_service.role_dependency import role_required
from app.services.firebase_storage_service.firebase_storage_service import (
    FirebaseStorageService,
)

router = APIRouter()


@router.post("/upload-image", response_model=str)
async def upload_image_endpoint(
    model: ModelTypePhoto,
    file: UploadFile = File(...),
    decode_token: dict = Depends(role_required(Role.WORK_TUTOR, Role.STUDENT)),
):
    return await FirebaseStorageService.upload_image(
        model, file, decode_token.get("uid")
    )


@router.post("/upload-pdf", response_model=str)
async def upload_pdf_endpoint(
    file: UploadFile = File(...),
    decode_token: dict = Depends(role_required(Role.STUDENT)),
):
    return await FirebaseStorageService.upload_pdf(file, decode_token.get("uid"))
