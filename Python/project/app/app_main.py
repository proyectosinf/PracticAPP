import logging
from contextlib import asynccontextmanager

import uvicorn
from fastapi import APIRouter, FastAPI, Request
from fastapi.encoders import jsonable_encoder
from fastapi.middleware.cors import CORSMiddleware
from starlette.responses import JSONResponse
from tortoise import Tortoise

from app import SingletonMeta
from app.api.app import (
    candidacy_router,
    company_router,
    degree_router,
    firebase_storage_router,
    offer_router,
    student_router,
    user_router,
    work_tutor_router,
)
from app.app_config import app_settings
from app.modules.database_module.settings import module_settings
from app.schemas.base_schema import BaseException
from app.services.auth_service.firebase_auth import FirebaseAuthService

logger = logging.getLogger(__name__)


def application_service_handler(_: Request, exception: Exception) -> JSONResponse:
    if isinstance(exception, BaseException):
        return JSONResponse(
            status_code=exception.status_code,
            content=jsonable_encoder(
                obj=exception.detail,
                exclude_none=True,
            ),
        )

    return JSONResponse(
        status_code=500,
        content=jsonable_encoder(
            obj=exception,
            exclude_none=True,
        ),
    )


@asynccontextmanager
async def lifespan(_app: FastAPI):
    # Init Firebase
    SingletonMeta.get_instance(FirebaseAuthService)
    # Init Tortoise
    await Tortoise.init(
        db_url=module_settings.database_url,
        modules={"default": ["app.modules.database_module.models.default"]},
    )

    yield
    await Tortoise.close_connections()


def create_app() -> FastAPI:
    application = FastAPI(
        title="PracticAPP",
        description="PracticAPP",
        version=app_settings.api_version,
        lifespan=lifespan,
    )

    # Middlewares
    # Add CORS origins
    application.add_middleware(
        CORSMiddleware,
        allow_origins=["*"],
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )

    # Handlers
    application.add_exception_handler(BaseException, application_service_handler)

    # Routes
    api_version_router = APIRouter()

    api_version_router.include_router(
        student_router.router, prefix="/students", tags=["students"]
    )
    api_version_router.include_router(
        work_tutor_router.router, prefix="/work_tutors", tags=["work_tutors"]
    )
    api_version_router.include_router(
        user_router.router, prefix="/users", tags=["users"]
    )
    api_version_router.include_router(
        company_router.router, prefix="/companies", tags=["companies"]
    )
    api_version_router.include_router(
        offer_router.router, prefix="/offers", tags=["offers"]
    )
    api_version_router.include_router(
        degree_router.router, prefix="/degrees", tags=["degrees"]
    )
    api_version_router.include_router(
        candidacy_router.router, prefix="/candidacies", tags=["candidacies"]
    )
    api_version_router.include_router(
        firebase_storage_router.router, prefix="/firebase", tags=["firebase"]
    )
    application.include_router(
        api_version_router, prefix=f"/api/v{app_settings.api_version}"
    )
    return application


app = create_app()

if __name__ == "__main__":
    uvicorn.run("app_main:app", host="0.0.0.0", port=8000)
