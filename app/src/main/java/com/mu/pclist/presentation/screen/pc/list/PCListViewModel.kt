package com.mu.pclist.presentation.screen.pc.list

import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mu.pclist.data.entity.PCEntity
import com.mu.pclist.domain.model.PCModel
import com.mu.pclist.domain.repository.PCRepository
import com.mu.pclist.presentation.navigation.Destinations.PCListDestination
import com.mu.pclist.presentation.util.BY_FAMILY
import com.mu.pclist.presentation.util.BY_INVENTORY_NUMBER
import com.mu.pclist.presentation.util.BY_OFFICES
import com.mu.pclist.presentation.util.BY_SERVICE_NUMBER
import com.mu.pclist.presentation.util.COMPUTERS
import com.mu.pclist.presentation.util.DIR_DOCS
import com.mu.pclist.presentation.util.FOUND_NOTHING
import com.mu.pclist.presentation.util.INTERNET
import com.mu.pclist.presentation.util.PC_LIST_IS_EMPTY
import com.mu.pclist.presentation.util.SUB_DIR_COMPUTERS
import com.mu.pclist.presentation.util.createExtFile
import com.mu.pclist.presentation.util.currentDateToString
import com.mu.pclist.presentation.util.setTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PCListViewModel @Inject constructor(
    private val pcRepository: PCRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var computers by mutableStateOf(emptyList<PCModel>())
    var foundComputers by mutableStateOf(emptyList<PCModel>())
    var sortedBy by mutableStateOf(BY_INVENTORY_NUMBER)
    var search by mutableStateOf("")
    var searchResult = PC_LIST_IS_EMPTY
    var title = ""
        private set
    var position by mutableIntStateOf(0)

    init {
        val args = savedStateHandle.toRoute<PCListDestination>()
        sortedBy = args.sortedBy

        viewModelScope.launch {
            pcRepository.pcList().collect { list ->
                computers = list.sortedList(sortedBy)

                foundComputers = computers
                title = setTitle(COMPUTERS, foundComputers.size, computers.size)

                val idx = computers.indexOf(computers.find { it.id == args.id })
                if (idx > 0) {
                    position = idx
                }
            }
        }
    }

    private fun List<PCModel>.sortedList(sortedBy: String) = when (sortedBy) {
        BY_FAMILY -> {
            this.sortedWith(
                compareBy<PCModel> { it.family }
                    .thenBy { it.name }
                    .thenBy { it.patronymic }
            )
        }

        BY_SERVICE_NUMBER -> {
            this.sortedBy { it.serviceNumber }
        }

        BY_OFFICES -> {
            this.sortedWith(
                compareBy<PCModel> { it.office }
                    .thenBy { it.inventoryNumber }
            )
        }

        else -> {
            this.sortedBy { it.inventoryNumber }
        }
    }

    fun onEvent(event: PCListEvent) {
        when (event) {
            is PCListEvent.OnPCListSortByChange -> {
                search = ""
                foundComputers = computers
                title = setTitle(COMPUTERS, foundComputers.size, computers.size)

                sortedBy = event.sortBy
                foundComputers = foundComputers.sortedList(sortedBy)
            }

            is PCListEvent.OnPCListSearchChange -> {
                search = event.search
                if (search.isBlank()) {
                    foundComputers = computers
                    searchResult = PC_LIST_IS_EMPTY
                } else {
                    searchResult = FOUND_NOTHING
                    foundComputers = when (sortedBy) {
                        BY_FAMILY -> {
                            computers.filter {
                                it.family.contains(search, ignoreCase = true)
                                        || it.name.contains(search, ignoreCase = true)
                                        || it.patronymic.contains(search, ignoreCase = true)
                            }
                        }

                        BY_OFFICES -> {
                            computers.filter { it.office.contains(search, ignoreCase = true) }
                        }

                        else -> {
                            computers.filter { it.inventoryNumber.contains(search, ignoreCase = true) }
                        }
                    }
                }
                title = setTitle(COMPUTERS, foundComputers.size, computers.size)
            }

            is PCListEvent.OnPCListDelete -> {
                search = ""
                viewModelScope.launch {
                    pcRepository.delete(
                        PCEntity(
                            id = event.pc.id,
                            inventoryNumber = event.pc.inventoryNumber,
                        )
                    )

                    title = setTitle(COMPUTERS, foundComputers.size, computers.size)
                }
            }

            is PCListEvent.OnPCListDocCreate -> {
                var writeResult = ""
                val filename = "computers.txt"
                val date = currentDateToString()
                var sort = "сортировка по "
                var sortedByInternet = sortedBy
                var header = ""
                var headerInternet = ""
                when (sortedBy) {
                    BY_INVENTORY_NUMBER -> {
                        sort = "по инвентарному номеру"
                        header = "№ п/п;Инв. №;ФИО;Таб. №;Отдел;Телефон"
                        headerInternet = "№ п/п;Инв. №;IP-адрес;Кабинет;Отдел;Телефон"
                    }

                    BY_FAMILY -> {
                        sort = "по фамилии"
                        sortedByInternet = BY_SERVICE_NUMBER
                        header = "№ п/п;Инв. №;ФИО;Таб. №;Отдел;Телефон"
                        headerInternet = "№ п/п;Инв. №;IP-адрес;Кабинет;Отдел;Телефон"
                    }

                    BY_OFFICES -> {
                        sort = "по отделам"
                        header = "№ п/п;Инв. №;Отдел;ФИО;Таб. №;Телефон"
                        headerInternet = "№ п/п;Инв. №;IP-адрес;Отдел;Кабинет;Телефон"
                    }
                }

                val file = createExtFile(filename, SUB_DIR_COMPUTERS)

                val fullPath = "${Environment.DIRECTORY_DOWNLOADS}/ $DIR_DOCS/ $SUB_DIR_COMPUTERS/ $filename"

                var fileOutputStream: FileOutputStream? = null
                try {
                    fileOutputStream = FileOutputStream(file, true)

                    fileOutputStream.write("Список компьютеров на $date\n".toByteArray())
                    fileOutputStream.write("($sort)\n".toByteArray())
                    fileOutputStream.write("$header\n".toByteArray())

                    computers.filter { !it.family.contains(INTERNET) }.sortedList(sortedBy).forEachIndexed { index, computer ->
                        val data = when (sortedBy) {
                            BY_INVENTORY_NUMBER -> "${index + 1};${computer.inventoryNumber};" +
                                    "${computer.family} ${computer.name} ${computer.patronymic};" +
                                    "${computer.serviceNumber};${computer.phone};${computer.office}\n"

                            BY_FAMILY -> "${index + 1};${computer.inventoryNumber};" +
                                    "${computer.family} ${computer.name} ${computer.patronymic};" +
                                    "${computer.serviceNumber};${computer.phone};${computer.office}\n"

                            BY_OFFICES -> "${index + 1};${computer.inventoryNumber};${computer.office};" +
                                    "${computer.family} ${computer.name} ${computer.patronymic};" +
                                    "${computer.serviceNumber};${computer.phone}\n"

                            else -> ""
                        }
                        fileOutputStream.write(data.toByteArray())
                    }
                    val computersInternet = computers.filter { it.family.contains(INTERNET) }.sortedList(sortedByInternet)
                    if (computersInternet.isNotEmpty()) {
                        fileOutputStream.write("Список компьютеров под Internet\n".toByteArray())
                        fileOutputStream.write("$headerInternet\n".toByteArray())

                        computersInternet.forEachIndexed { index, computer ->
                            val data = when (sortedBy) {
                                BY_OFFICES -> "${index + 1};${computer.inventoryNumber};${computer.serviceNumber};" +
                                        "${computer.office};${computer.name} ${computer.patronymic};${computer.phone}\n"

                                else -> "${index + 1};${computer.inventoryNumber};${computer.serviceNumber};" +
                                        "${computer.name} ${computer.patronymic};${computer.office};${computer.phone}\n"
                            }
                            fileOutputStream.write(data.toByteArray())
                        }
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