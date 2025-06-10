package com.mu.pclist.presentation.util

import android.content.Context
import android.util.Log
import android.util.TypedValue

fun toLog(message: String) {
    Log.d(DEBUG_TAG, message)
}

fun checkIsFieldEmpty(value: String?): String {
    return if ((value != null) && value.isBlank()) FIELD_CAN_NOT_BE_EMPTY
    else ""
}

fun Int.toDp(context: Context): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()

fun setTitle(title: String, foundSize: Int, size: Int): String =
    title +
            if (size > 0) {
                " (" + if (foundSize > 0 && foundSize < size) {
                    "$foundSize из "
                } else {
                    ""
                } + "$size)"
            } else ""
