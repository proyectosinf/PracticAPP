package com.mobivery.template.domain.model.offer

enum class OfferType(val value: Int) {
    REGULAR(1),
    ADULTS(2);

    companion object {
        fun from(value: Int): OfferType = when (value) {
            1 -> REGULAR
            2 -> ADULTS
            else -> throw IllegalArgumentException("Unknown offer type value: $value")
        }
    }
}