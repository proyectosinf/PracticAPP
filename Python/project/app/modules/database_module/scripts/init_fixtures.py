import json
import logging
import os

from dotenv import load_dotenv
from tortoise import Tortoise, run_async

from app.modules.database_module import DatabaseModule
from app.modules.database_module.models.default import Degree
from app.schemas.degrees_schema import DegreesInputSchema

# logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

load_dotenv()


async def generate_view_users() -> None:
    try:
        await Tortoise.init(
            db_url=os.getenv("DATABASE_URL"),
            modules={
                "default": ["app.modules.database_module.models.default.__main__"]
            },
        )
        await Tortoise.get_connection("default").execute_script(
            """
            CREATE OR REPLACE VIEW user_combined_view AS
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
        logger.info("View created successfully.")
    except Exception as e:
        logger.error(f"Error creating view generate_view_users: {e}")
    finally:
        await Tortoise.close_connections()


async def inset_degrees() -> None:
    PATH_GRADES: str = os.path.join(os.path.dirname(__file__), "Grados_FP.JSON")
    try:
        with open(PATH_GRADES, "r", encoding="utf-8") as file:
            list_offers: dict = json.load(file)
            for key, families in list_offers.items():
                for family_name, degrees in families.items():
                    for degree in degrees:
                        degree_input: DegreesInputSchema = DegreesInputSchema(
                            name=degree
                        )
                        await DatabaseModule.post_entity(
                            Degree, degree_input.model_dump()
                        )
        logger.info("Degrees inserted successfully.")
    except Exception as e:
        logger.error(f"Error inserting degrees: {e}")


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
            CREATE OR REPLACE VIEW available_offers_view AS
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


if __name__ == "__main__":
    run_async(generate_view_users())
    run_async(generate_available_offers_view())
    run_async(inset_degrees())
