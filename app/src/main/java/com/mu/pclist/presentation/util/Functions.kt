package com.mu.pclist.presentation.util

import android.content.Context
import android.os.Environment
import android.util.Log
import android.util.TypedValue
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                " (" + if (foundSize in 1..<size) {
                    "$foundSize из "
                } else {
                    ""
                } + "$size)"
            } else ""

fun currentDateToString(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    return try {
        sdf.format(Date())
    } catch (e: Exception) {
        toLog("Ошибка asDate ${e.message}")
        ""
    }
}

fun createExtFile(filename: String, dir: String = ""): File {
    var path: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    path = File(path, DIR_DOCS)
    if (!path.exists()) path.mkdir()

    if (dir.isNotBlank()) {
        path = File(path, dir)
        if (!path.exists()) path.mkdir()
    }

    val file = File(path, filename)
    file.writeText("")

    return file
}

/*fun createFile(context: Context) {
    val filename = "qwerty.txt"
    val dir = "test"
    var content = "Test qwerty"

    val path = context.filesDir

    val newPath = File(path, dir)
    if (!newPath.exists()) newPath.mkdir()

    var file = File(newPath, filename)

    //context.openFileOutput("Qwerty.txt", Context.MODE_PRIVATE).use {
    file.outputStream().use {
        it.write(content.toByteArray())
    }

    file = File(newPath, filename)
    content = file.readText()
}*/
