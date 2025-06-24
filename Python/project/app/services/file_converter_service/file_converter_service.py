from io import BytesIO

from fastapi import UploadFile

from app.repositories.file_converter_repository import FileConverterRepository


class FileConverterService:
    @staticmethod
    def convert_to_png(file: UploadFile) -> BytesIO:
        return FileConverterRepository.convert_to_png(file)
