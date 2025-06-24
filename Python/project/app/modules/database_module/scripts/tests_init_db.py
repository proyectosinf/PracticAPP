import logging
import os
from pathlib import Path

from dotenv import load_dotenv
from tortoise import Tortoise

logger = logging.getLogger(__name__)
# load env
load_dotenv()


def delete_database() -> None:
    """Delete file from database."""
    logger.info("Clearing database tables...")
    FILE_DATABASE_PATH = (
        Path(__file__).resolve().parent.parent.parent.parent / "tests" / "practicapp.db"
    )

    if FILE_DATABASE_PATH.exists():
        FILE_DATABASE_PATH.unlink()
        logger.info("Database file deleted.")
    else:
        logger.error("File not found")


async def generate_schema() -> None:
    logger.info("Initializing Tortoise...")

    await Tortoise.init(
        db_url=os.getenv("DATABASE_URL"),
        modules={"default": ["app.modules.database_module.models.default.__main__"]},
    )

    logger.info("Generating database schemas via Tortoise...")
    await Tortoise.generate_schemas()

    await Tortoise.close_connections()


async def generate_view() -> None:
    await Tortoise.init(
        db_url=os.getenv("DATABASE_URL"),
        modules={"default": ["app.modules.database_module.models.default.__main__"]},
    )

    await Tortoise.get_connection("default").execute_script(
        """
        CREATE VIEW IF NOT EXISTS user_combined_view AS
        SELECT id,
               uid,
               email,
               name,
               surname,
               role,
               photo,
               dni,
               social_security_number,
               'student' AS user_type
        FROM student
        UNION
        SELECT id,
               uid,
               email,
               name,
               surname,
               role,
               photo,
               NULL         AS dni,
               NULL         AS social_security_number,
               'work_tutor' AS user_type
        FROM worktutor;
        """
    )
    await Tortoise.close_connections()


async def generate_available_offers_view() -> None:
    try:
        await Tortoise.init(
            db_url=os.getenv("DATABASE_URL"),
            modules={
                "default": ["app.modules.database_module.models.default.__main__"]
            },
        )
        await Tortoise.get_connection("default").execute_script(
            """
            CREATE VIEW IF NOT EXISTS available_offers_view AS
            SELECT
                o.*,
                COUNT(c.id) FILTER (WHERE c.status = 2) AS approved_count,
                o.vacancies_number - COUNT(c.id) FILTER (WHERE c.status = 2) AS
                available_spots
            FROM
                offer o
            LEFT JOIN
                candidacy c ON o.id = c.offer_id
            GROUP BY
                o.id
            HAVING
                COUNT(c.id) FILTER (WHERE c.status = 2) < o.vacancies_number;

            """
        )
        logger.info("available_offers_view created successfully.")
    except Exception as e:
        logger.error(f"Error creating view generate_available_offers_view: {e}")
    finally:
        await Tortoise.close_connections()
