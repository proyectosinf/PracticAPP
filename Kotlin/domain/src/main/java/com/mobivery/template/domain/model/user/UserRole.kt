package com.mobivery.template.domain.model.user

enum class UserRole(val value: Int) {
    STUDENT(1),
    TUTOR(2);

    companion object {
        fun from(value: Int): UserRole = when (value) {
            1 -> STUDENT
            2 -> TUTOR
            else -> throw IllegalArgumentException("Unknown role value: $value")
        }
    }
}