import random

from app.modules.database_module import DatabaseModule
from app.modules.database_module.models.default import Company


class CompanyRepository:
    @staticmethod
    async def create_company(payload: dict) -> Company:

        # Generate security code unique from the company
        exist_code: bool = True
        while exist_code:
            security_code: str = str(random.randint(0, 99999))
            company_exist: Company = await DatabaseModule.get_entity_filtered(
                Company, filters={"security_code": security_code}
            )

            if not company_exist:
                exist_code = False
                payload["security_code"] = security_code

        company = await DatabaseModule.post_entity(Company, payload)
        return company

    @staticmethod
    async def get_company_id_by_security_code(security_code: str) -> int | None:
        company = await DatabaseModule.get_entity_filtered(
            Company, {"security_code": security_code}
        )
        if company:
            return company.id
        else:
            return None

    @staticmethod
    async def get_company_by_id(company_id: int) -> Company | None:
        company = await DatabaseModule.get_entity_filtered(Company, {"id": company_id})
        return company

    @staticmethod
    async def update_student(payload: dict, company_id: id) -> Company | None:
        return await DatabaseModule.put_entity(Company, payload, company_id)
