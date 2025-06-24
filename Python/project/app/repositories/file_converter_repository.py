from io import BytesIO

from fastapi import UploadFile
from PIL import Image


class FileConverterRepository:
    @staticmethod
    def convert_to_png(file: UploadFile) -> BytesIO:
        image = Image.open(file.file).convert("RGBA")
        buffer = BytesIO()
        image.save(buffer, format="PNG")
        buffer.seek(0)
        return buffer
