package com.mu.pclist.presentation.screen.office.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.pclist.data.entity.OfficeEntity
import com.mu.pclist.domain.repository.OfficeRepository
import com.mu.pclist.presentation.util.toLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OfficeListViewModel @Inject constructor(
    private val officeRepository: OfficeRepository
) : ViewModel() {
    var offices = officeRepository.officeList()

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