package com.mu.pclist.presentation.screen.office.list

import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.pclist.data.entity.OfficeEntity
import com.mu.pclist.domain.repository.OfficeRepository
import com.mu.pclist.presentation.util.toLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OfficeListViewModel @Inject constructor(
    private val officeRepository: OfficeRepository
) : ViewModel() {
    var offices = officeRepository.officeList()

    fun createExtFile(context: Context) {
        val content = "Здесь будет список ПК"
        val filename = "pclist.txt"
        val dir = "pclist"

        val folder: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        toLog("folder: $folder")

        val newPath = File(folder, dir)
        toLog("newPath: $newPath")
        if (!newPath.exists()) newPath.mkdir()

        // Storing the data in file with name as qwerty.txt
        val file = File(newPath, filename)
        writeTextData(file, content)
    }

    private fun writeTextData(file: File, data: String) {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(data.toByteArray())
            toLog("write success")
        } catch (e: Exception) {
            //e.printStackTrace()
            toLog("write exception: ${e.message}")
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                    toLog("close success")
                } catch (e: IOException) {
                    //e.printStackTrace()
                    toLog("close exception: ${e.message}")
                }
            }
        }
    }

    fun createFile(context: Context) {
        val filename = "qwerty.txt"
        val dir = "test"
        val content = "Test qwerty"

        val path = context.filesDir
        toLog("path: $path")

        //External Storage
        /*val path = context.getExternalFilesDir(null)
        <uses-permission android-name="android.permission.WRITE_EXTERNAL_STORAGE" />*/

        val newPath = File(path, dir)
        toLog("newDir: $newPath")

        if (!newPath.exists()) newPath.mkdir()

        var file = File(newPath, filename)
        toLog("file: ${file.path}")

        //context.openFileOutput("Qwerty.txt", Context.MODE_PRIVATE).use {
        file.outputStream().use {
            it.write(content.toByteArray())
            toLog("write")
        }

        file = File(newPath, filename)
        //val file = File(path, "Qwerty.txt")
        //val content = file.readText()
        toLog("read: ${file.readText()}")
    }

    fun onEvent(event: OfficeListEvent) {
        when (event) {
            is OfficeListEvent.OnOfficeListDelete -> {
                viewModelScope.launch {
                    officeRepository.deleteOffice(
                        OfficeEntity(
                            id = event.office.id,
                            code = event.office.code,
                            shortName = event.office.shortName,
                            office = event.office.office,
                            userId = event.office.userId.toInt()
                        )
                    )
                }
            }
        }
    }
}