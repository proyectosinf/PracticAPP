package com.mobivery.fct25.notifications

const val ID = "id"
const val SUBJECT = "subject"
const val TITLE = "title"
const val MESSAGE = "message"

// TODO: add all notification types necessary
enum class NotificationType(val code: String) {
    NOTIFICATION_TYPE_1("notificationType1"),
    NOTIFICATION_TYPE_2("notificationType2")
}

fun getNotificationType(code: String?): NotificationType = when (code) {
    NotificationType.NOTIFICATION_TYPE_1.code -> NotificationType.NOTIFICATION_TYPE_1
    NotificationType.NOTIFICATION_TYPE_2.code -> NotificationType.NOTIFICATION_TYPE_2
    else -> NotificationType.NOTIFICATION_TYPE_1
}