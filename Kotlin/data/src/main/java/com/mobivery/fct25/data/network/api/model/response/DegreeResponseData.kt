package com.mobivery.fct25.data.network.api.model.response

import com.mobivery.template.domain.model.degree.Degree
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DegreeResponseData(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
)

fun DegreeResponseData.toDomain(): Degree = Degree(
    id = id,
    name = name
)
