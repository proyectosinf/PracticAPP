from app.schemas.candidacy_schema import (
    CandidacyOutputListSchema,
    CandidacyPaginatedSchema,
)


class HelperService:
    @staticmethod
    async def build_paginated_response(
        candidacies: tuple[list, int]
    ) -> CandidacyPaginatedSchema:

        list_candidacies: list[CandidacyOutputListSchema] = []
        for candidacy in candidacies[0]:
            await candidacy.fetch_related("student", "offer")
            await candidacy.offer.fetch_related("company")

            candidacy_schema = CandidacyOutputListSchema(
                student_name=candidacy.student.name,
                student_surname=candidacy.student.surname,
                offer_title=candidacy.offer.title,
                company_name=candidacy.offer.company.name,
                **candidacy.__dict__,
            )
            list_candidacies.append(candidacy_schema)

        return CandidacyPaginatedSchema(items=list_candidacies, total=candidacies[1])
