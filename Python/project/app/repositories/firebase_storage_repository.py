from io import BytesIO

from fastapi import UploadFile
from firebase_admin import storage


class FirebaseStorageRepository:
    @staticmethod
    def upload_image_to_firebase(file: BytesIO) -> str:

        bucket = storage.bucket()
        blob = bucket.blob(f"profile/{file.name}")

        blob.upload_from_file(file, content_type="image/png")
        blob.make_public()

        return blob.public_url

    @staticmethod
    def delete_image_to_firebase(file_url: str) -> None:
        from urllib.parse import unquote, urlparse

        if not file_url or not file_url.strip():
            return

        # Check if it's a valid Firebase URL
        if "storage.googleapis.com" not in file_url:
            return

        parsed_url = urlparse(file_url)

        # For URLs like: https://storage.googleapis.com/bucket-name/path/file.png
        path_parts = parsed_url.path.split("/")

        # Remove empty elements and build the path
        path_parts = [part for part in path_parts if part]

        if len(path_parts) < 2:
            return

        # The blob path is everything after the bucket name
        # Example: ['bucket-name', 'profile', 'file.png'] -> 'profile/file.png'
        blob_path = "/".join(path_parts[1:])  # Skip the bucket name
        blob_path = unquote(blob_path)  # Decode special characters

        bucket = storage.bucket()
        blob = bucket.blob(blob_path)

        try:
            blob.delete()
        except Exception as e:
            print(f"Error deleting file {blob_path}: {str(e)}")

    @staticmethod
    def upload_pdf_to_firebase(file: UploadFile, user_uid: str) -> str:
        bucket = storage.bucket()
        blob = bucket.blob(f"pdf/{user_uid}.pdf")

        blob.upload_from_file(file.file, content_type=file.content_type)
        blob.make_public()

        return blob.public_url
