from typing import Type

from tortoise.expressions import Q

from app.modules.database_module.models.database_model import DatabaseModel


class GenericDAO:
    @classmethod
    async def post_entity(
        cls, model: Type[DatabaseModel], data: dict
    ) -> DatabaseModel | None:
        """
        Create an object passed by parameter and return the identifier
        :param model: entity model to create
        :param data: dict with information to create
        :return: Object created
        :rtype: DatabaseModel | None
        """
        return await model.create(**data)

    @classmethod
    async def get_entity(
        cls, model: Type[DatabaseModel], identifier: int, filters: dict = None
    ) -> DatabaseModel | None:
        """
        Get an object and return it
        :param model: entity model to find
        :param identifier: entity model identifier
        :param filters: filters to find the entity
        :return: Result found if exists
        :rtype: DatabaseModel | None
        """
        filters = filters if filters else {}
        return await model.filter(id=identifier).filter(**filters).first()

    @classmethod
    async def get_entity_filtered(
        cls, model: Type[DatabaseModel], filters: dict
    ) -> DatabaseModel | None:
        """
        Query an object with filter and return it
        :param model: entity model to find
        :param filters: filters to find the entity
        :return: Result found if exists
        :rtype: DatabaseModel | None
        """
        return await model.filter(**filters).first()

    @classmethod
    async def get_all_entity_filtered(
        cls, model: Type[DatabaseModel], filters: dict = None
    ) -> list[DatabaseModel] | None:
        filters = filters if filters else {}
        return await model.filter(**filters).all()

    @classmethod
    async def get_all_count_entity_filtered(
        cls, model: Type[DatabaseModel], filters: dict = None
    ) -> int:
        filters = filters if filters else {}
        return await model.filter(**filters).count()

    @classmethod
    async def get_all_entity_filtered_paginated(
        cls,
        model: Type[DatabaseModel],
        page: int = 0,
        limit: int = 25,
        q: Q = None,
        filters: dict = None,
        order: str = None,
    ) -> tuple[list[DatabaseModel], int]:
        filters = filters if filters else {}
        data = model.filter(q, **filters) if q else model.filter(**filters)
        if order:
            data = data.order_by(order)

        return await data.offset(page * limit).limit(limit), await data.all().count()

    @classmethod
    async def put_entity(
        cls,
        model: Type[DatabaseModel],
        data: dict,
        identifier: int,
    ) -> DatabaseModel | None:
        entity = await model.filter(id=identifier).first()
        if not entity:
            return None

        entity = await model.filter(id=identifier).update(**data)

        if entity:
            update_entity = await model.filter(id=identifier).first()
            return update_entity
        return None
