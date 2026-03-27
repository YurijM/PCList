package com.mu.pclist.presentation.screen.office.list

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
import com.mu.pclist.presentation.util.DIR_DOCS
import com.mu.pclist.presentation.util.SUB_DIR_OFFICES
import com.mu.pclist.presentation.util.createExtFile
import com.mu.pclist.presentation.util.currentDateToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OfficeListViewModel @Inject constructor(
    private val officeRepository: OfficeRepository
) : ViewModel() {
    //var offices = officeRepository.officeList()
    var offices by mutableStateOf(emptyList<OfficeModel>())

    init {
        viewModelScope.launch {
            officeRepository.officeList().collect { list ->
                offices = list
            }
        }
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

            is OfficeListEvent.OnOfficeListDocCreate -> {
                var writeResult = ""
                val filename = "offices.txt"
                val date = currentDateToString()

                val file = createExtFile(filename, SUB_DIR_OFFICES)

                val fullPath = "${Environment.DIRECTORY_DOWNLOADS}/ $DIR_DOCS/ $SUB_DIR_OFFICES/ $filename"

                var fileOutputStream: FileOutputStream? = null
                try {
                    fileOutputStream = FileOutputStream(file, true)

                    fileOutputStream.write("Список отделов на $date\n".toByteArray())
                    fileOutputStream.write("№;Код;Сокращенно;Отдел\n".toByteArray())

                    offices.forEachIndexed { index, office ->
                        val data = "${index + 1};${office.code};${office.shortName};${office.office}\n"
                        fileOutputStream.write(data.toByteArray())
                    }
                    writeResult = "Создан файл $fullPath"
                } catch (e: Exception) {
                    //e.printStackTrace()
                    writeResult = "Ошибка ${e.message}"
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close()
                        } catch (e: IOException) {
                            //e.printStackTrace()
                            writeResult = "Ошибка ${e.message}"
                        }
                    }
                    if (writeResult.isNotBlank()) {
                        Toast.makeText(event.context, writeResult, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}