from fastapi import Depends

from app.modules.database_module import DatabaseModule
from app.modules.database_module.models.default import UserCombinedView
from app.modules.database_module.models.user_role import Role
from app.services.auth_service.auth_service_exception import (
    AuthServiceException,
    AuthServiceExceptionInfo,
)
from app.services.auth_service.firebase_auth import firebase_auth_service


def role_required(*expected_roles: Role):
    async def _verify_role(
        decode_token: dict = Depends(firebase_auth_service.verify_token),
    ):
        user: UserCombinedView = await DatabaseModule.get_entity_filtered(
            UserCombinedView, {"uid": decode_token.get("uid")}
        )

        if not user:
            raise AuthServiceException(AuthServiceExceptionInfo.USER_NOT_FOUND)

        user_role = user.role

        if user_role not in expected_roles:
            raise AuthServiceException(AuthServiceExceptionInfo.UNAUTHORIZED)
        return decode_token

    return _verify_role
