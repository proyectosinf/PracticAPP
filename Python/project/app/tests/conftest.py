import asyncio
import json
import os

import pytest
import requests
from fastapi import FastAPI
from requests.models import Response
from starlette.testclient import TestClient
from tortoise.contrib.fastapi import register_tortoise

from app.app_config import AppSettings, get_application_settings
from app.app_main import create_app
from app.modules.database_module.scripts.init_db import generate_schema
from app.modules.database_module.scripts.init_fixtures import inset_degrees
from app.modules.database_module.scripts.tests_init_db import (
    delete_database,
    generate_available_offers_view,
    generate_view,
)
from app.modules.database_module.settings import module_settings
from app.tests.constants import Credentials


class TestAPP:

    def __init__(self, test_app, test_app_url):
        """
        Instance the test application
        """
        self.test_app = test_app
        self.test_app_url = test_app_url

        # Init empty variables
        self.tokens = {}

    def get_token_for_user(self, email: str, password: str) -> str:
        """
        Firebase login to get token for a specific user
        :param email: User email
        :param password: User password
        :return: Firebase token
        """
        firebase_api_key = os.getenv("FIREBASE_API_KEY")

        resp = requests.post(
            "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword",
            params={"key": firebase_api_key},
            json={
                "email": email,
                "password": password,
                "returnSecureToken": True,
            },
        )

        if resp.status_code != 200:
            raise Exception(f"Firebase login failed: {resp.text}")

        return resp.json()["idToken"]

    def get_token_for_role(self, role: str) -> str:
        """
        Firebase login to get token for a specific role
        :param role: Rol como string
        :return: Firebase token
        """
        if role in self.tokens:
            return self.tokens[role]

        if role not in Credentials.CREDENTIALS:
            raise ValueError(f"Role {role} no tiene credenciales configuradas")

        email = Credentials.CREDENTIALS[role]["email"]
        password = Credentials.CREDENTIALS[role]["password"]
        token = self.get_token_for_user(email, password)
        self.tokens[role] = token
        return token

    def do_request(
        self, http_method: str, endpoint: str, headers: dict = None, data: dict = None
    ) -> Response:
        """
        Make a request to the app and return response
        :param http_method: http method (get/post/put/delete)
        :param endpoint: endpoint to call
        :param headers: headers of the request
        :param data: data of the request
        :return: response from the server
        :rtype: Response
        """

        return self.test_app.request(
            http_method,
            f"{self.test_app_url}{endpoint}",
            headers=headers if headers else None,
            data=json.dumps(data) if data else None,
        )

    def do_request_with_role(
        self,
        role: str,
        http_method: str,
        endpoint: str,
        headers: dict = None,
        data: dict = None,
    ) -> Response:
        """
        Make a request to the app with specific role and return response
        :param role: role to login and make the request
        :param http_method: http method (get/post/put/delete)
        :param endpoint: endpoint to call
        :param headers: headers of the request
        :param data: data of the request
        :return: response from the server
        :rtype: Response
        """

        # Get token from role
        token = self.get_token_for_role(role)

        return self.do_request(
            http_method,
            endpoint,
            headers={**{"Authorization": f"Bearer {token}"}, **(headers or {})},
            data=data,
        )


def setup_script():
    """Script to run before all tests."""
    print("Running setup script before all tests...")
    delete_database()
    asyncio.run(generate_schema())
    asyncio.run(generate_available_offers_view())
    asyncio.run(generate_view())
    asyncio.run(inset_degrees())


def teardown_script():
    """Script to run after all tests."""
    print("Running teardown script after all tests...")


@pytest.fixture(scope="session", autouse=True)
def setup_and_teardown():
    # Setup: Run before any tests
    setup_script()

    # Yield to run the tests
    yield

    # Teardown: Run after all tests
    teardown_script()


@pytest.fixture(scope="session")
def test_app() -> TestAPP:
    # Init app
    test_app: FastAPI = create_app()

    # Override the test application settings
    test_app_config = AppSettings()
    test_app.dependency_overrides[get_application_settings] = test_app_config

    # Init Tortoise for database
    register_tortoise(
        test_app,
        db_url=module_settings.database_url,
        modules={"default": ["app.modules.database_module.models.default"]},
    )

    with TestClient(test_app) as test_client:
        yield TestAPP(test_client, f"/api/v{test_app_config.api_version}")


def pytest_collection_modifyitems(items):
    order = {
        "test_student.py": 0,
        "test_work_tutor.py": 1,
        "test_company.py": 2,
        "test_degree.py": 3,
        "test_offer.py": 4,
        "test_candidacy.py": 5,
    }

    def sort_key(item):
        filename = item.nodeid.split("::")[0].replace("\\", "/")
        shortname = filename.split("integration/")[-1]
        return order.get(shortname, 999)

    items.sort(key=sort_key)
