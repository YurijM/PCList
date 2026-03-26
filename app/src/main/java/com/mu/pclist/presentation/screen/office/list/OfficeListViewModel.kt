package com.mu.pclist.presentation.screen.office.list

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.pclist.data.entity.OfficeEntity
import com.mu.pclist.domain.model.OfficeModel
import com.mu.pclist.domain.repository.OfficeRepository
import com.mu.pclist.presentation.util.ROOT_DIR_OFFICES
import com.mu.pclist.presentation.util.SUB_DIR_OFFICES
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
    //var offices = officeRepository.officeList()
    var offices by mutableStateOf(emptyList<OfficeModel>())
    var writeResult by mutableStateOf("")

    init {
        viewModelScope.launch {
            officeRepository.officeList().collect { list ->
                offices = list
            }
        }
    }

    fun createExtFile(context: Context) {
        writeResult = ""

        val filename = "offices.txt"

        var path: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        path = File(path, ROOT_DIR_OFFICES)
        if (!path.exists()) path.mkdir()
        path = File(path, SUB_DIR_OFFICES)
        if (!path.exists()) path.mkdir()

        val fullPath = "${Environment.DIRECTORY_DOWNLOADS}/ $ROOT_DIR_OFFICES/ $SUB_DIR_OFFICES/ $filename"

        val file = File(path, filename)
        file.writeText("")

        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file, true)
            val title = "№;Код;Сокращенно;Отдел\n"
            fileOutputStream.write(title.toByteArray())

            offices.forEachIndexed { index, office ->
                val data = "${index + 1};${office.code};${office.shortName};${office.office}\n"
                fileOutputStream.write(data.toByteArray())
            }
            writeResult = "Создан файл $fullPath"
        } catch (e: Exception) {
            //e.printStackTrace()
            writeResult = "Ошибка записи в файл $filename"
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    //e.printStackTrace()
                    writeResult = "Ошибка закрытия файла $filename"
                }
            }
            if (writeResult.isNotBlank()) {
                Toast.makeText(context, writeResult, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun createFile(context: Context) {
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