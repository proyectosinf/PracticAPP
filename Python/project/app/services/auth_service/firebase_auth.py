import json
import os

import firebase_admin
from fastapi import Depends
from fastapi.security import HTTPAuthorizationCredentials, HTTPBearer
from firebase_admin import auth, credentials

from app.services.auth_service.auth_service_exception import (
    AuthServiceException,
    AuthServiceExceptionInfo,
)

security = HTTPBearer()


class FirebaseAuthService:
    def __init__(self):
        if not firebase_admin._apps:
            cred = credentials.Certificate(
                json.loads(os.getenv("SERVICE_ACCOUNT_CREDENTIALS"))
            )
            firebase_admin.initialize_app(
                cred,
                {
                    "storageBucket": os.getenv("FIREBASE_STORAGE_BUCKET"),
                },
            )

    @staticmethod
    async def verify_token(
        credentials: HTTPAuthorizationCredentials = Depends(security),
    ):
        try:
            token = credentials.credentials
            decoded_token = auth.verify_id_token(token)
            return decoded_token
        except Exception:
            raise AuthServiceException(AuthServiceExceptionInfo.INVALID_TOKEN)


firebase_auth_service = FirebaseAuthService()
