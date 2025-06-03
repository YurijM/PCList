package com.mu.pclist.presentation.util

import android.util.Log

fun toLog(message: String) {
    Log.d(DEBUG_TAG, message)
}

fun checkIsFieldEmpty(value: String?): String {
    return if ((value != null) && value.isBlank()) FIELD_CAN_NOT_BE_EMPTY
    else ""
}

