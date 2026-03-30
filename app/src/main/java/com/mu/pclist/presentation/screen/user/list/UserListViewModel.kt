package com.mu.pclist.presentation.screen.user.list

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
import com.mu.pclist.data.entity.UserEntity
import com.mu.pclist.domain.model.PCModel
import com.mu.pclist.domain.model.UserModel
import com.mu.pclist.domain.repository.PCRepository
import com.mu.pclist.domain.repository.UserRepository
import com.mu.pclist.presentation.navigation.Destinations.UserListDestination
import com.mu.pclist.presentation.util.BY_FAMILY
import com.mu.pclist.presentation.util.BY_INVENTORY_NUMBER
import com.mu.pclist.presentation.util.BY_OFFICES
import com.mu.pclist.presentation.util.BY_SERVICE_NUMBER
import com.mu.pclist.presentation.util.DIR_DOCS
import com.mu.pclist.presentation.util.FOUND_NOTHING
import com.mu.pclist.presentation.util.INTERNET
import com.mu.pclist.presentation.util.SUB_DIR_USERS
import com.mu.pclist.presentation.util.USERS
import com.mu.pclist.presentation.util.USER_LIST_IS_EMPTY
import com.mu.pclist.presentation.util.createExtFile
import com.mu.pclist.presentation.util.currentDateToString
import com.mu.pclist.presentation.util.setTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import kotlin.collections.find

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val pcRepository: PCRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var users by mutableStateOf(emptyList<UserModel>())
    var foundUsers by mutableStateOf(emptyList<UserModel>())
    var sortedBy by mutableStateOf(BY_FAMILY)
    var search by mutableStateOf("")
    var searchResult = USER_LIST_IS_EMPTY
    var computers by mutableStateOf(emptyList<PCModel>())
    var title = ""
        private set
    var position by mutableIntStateOf(0)
    var withoutInternet by mutableStateOf(true)
    private var currentId = 0L

    init {
        val args = savedStateHandle.toRoute<UserListDestination>()
        currentId = args.id
        sortedBy = args.sortedBy
        search = args.search
        withoutInternet = args.withoutInternet

        viewModelScope.launch {
            pcRepository.pcList().collect { pcList ->
                computers = pcList.sortedBy { it.userId }

                userRepository.userList().collect { list ->
                    users = list.sortedList(sortedBy)
                    users = setUserPCList(users.toMutableList())

                    foundUsers = searchResult(sortedBy, search)

                    if (withoutInternet)
                        foundUsers = foundUsers.filter { it.family != INTERNET }

                    title = setTitle(USERS, foundUsers.size, users.size)

                    //val idx = users.indexOf(users.find { it.id == args.id })
                    val idx = foundUsers.indexOf(users.find { it.id == args.id })
                    if (idx > 0) {
                        position = idx
                    }
                }
            }
        }
    }

    private fun searchResult(sortedBy: String, search: String): List<UserModel> {
        return when (sortedBy) {
            BY_SERVICE_NUMBER -> {
                users.filter { it.serviceNumber.contains(search, ignoreCase = true) }
            }

            BY_OFFICES -> {
                users.filter { it.office.contains(search, ignoreCase = true) }
            }

            else -> {
                users.filter {
                    it.family.contains(search, ignoreCase = true)
                            || it.name.contains(search, ignoreCase = true)
                            || it.patronymic.contains(search, ignoreCase = true)
                }
            }
        }
    }

    private fun List<UserModel>.sortedList(sortedBy: String): List<UserModel> {
        val list = when (sortedBy) {
            BY_SERVICE_NUMBER -> this.sortedBy { it.serviceNumber }

            BY_INVENTORY_NUMBER -> this.sortedBy { it.pcList }

            BY_OFFICES -> this.sortedWith(
                compareBy<UserModel> { it.office }
                    .thenBy { it.family }
                    .thenBy { it.name }
                    .thenBy { it.patronymic }
            )

            else -> this.sortedWith(
                compareBy<UserModel> { it.family }
                    .thenBy { it.name }
                    .thenBy { it.patronymic }
            )
        }

        val idx = list.indexOf(list.find { it.id == currentId })
        if (idx > 0) {
            position = idx
        }

        return list
    }

    private fun setUserPCList(users: MutableList<UserModel>): List<UserModel> {
        users.forEachIndexed { idx, user ->
            val pcList = computers.filter { it.userId == user.id }
            if (pcList.isNotEmpty()) {
                var list = ""
                pcList.forEach { pc ->
                    list += (if (list.isNotEmpty()) ", " else "") + pc.inventoryNumber
                }
                users[idx] = user.copy(pcList = list)
            }
        }
        return users
    }

    fun onEvent(event: UserListEvent) {
        when (event) {
            is UserListEvent.OnUserListSortByChange -> {
                search = ""
                foundUsers = users
                if (withoutInternet)
                    foundUsers = foundUsers.filter { it.family != INTERNET }
                title = setTitle(USERS, foundUsers.size, users.size)

                sortedBy = event.sortBy
                foundUsers = foundUsers.sortedList(sortedBy)
            }

            is UserListEvent.OnUserListSearchChange -> {
                search = event.search
                if (search.isBlank()) {
                    foundUsers = setUserPCList(users.toMutableList())
                    if (withoutInternet)
                        foundUsers = foundUsers.filter { it.family != INTERNET }
                    searchResult = USER_LIST_IS_EMPTY
                } else {
                    searchResult = FOUND_NOTHING
                    foundUsers = searchResult(sortedBy, search)
                    foundUsers = setUserPCList(foundUsers.toMutableList())
                }
                title = setTitle(USERS, foundUsers.size, users.size)
            }

            is UserListEvent.OnUserListDelete -> {
                //search = ""
                val userEntity = UserEntity(
                    id = event.user.id,
                    serviceNumber = event.user.serviceNumber,
                    family = event.user.family,
                    name = event.user.name,
                    patronymic = event.user.patronymic,
                    phone = event.user.phone,
                    officeId = event.user.officeId.toInt()
                )
                viewModelScope.launch {
                    userRepository.deleteUser(userEntity)
                    title = setTitle(USERS, foundUsers.size, users.size)
                }
            }

            is UserListEvent.OnUserListDocCreate -> {
                var writeResult = ""
                val filename = "users.txt"
                val date = currentDateToString()
                var sort = "сортировка по "
                var sortedByInternet = sortedBy
                var header = ""
                var headerInternet = ""
                when (sortedBy) {
                    BY_FAMILY -> {
                        sort = "по фамилии"
                        sortedByInternet = BY_INVENTORY_NUMBER
                        header = "№ п/п;ФИО;Таб. №;Телефон;Отдел;Компьютеры"
                        headerInternet = "№ п/п;Инв. №;IP-адрес;Кабинет;Отдел"
                    }

                    BY_SERVICE_NUMBER -> {
                        sort = "по табельному номеру"
                        sortedByInternet = BY_SERVICE_NUMBER
                        header = "№ п/п;Таб. №;ФИО;Телефон;Отдел;Компьютеры"
                        headerInternet = "№ п/п;IP-адрес;Инв. №;Кабинет;Отдел"
                    }

                    BY_OFFICES -> {
                        sort = "по отделам"
                        sortedByInternet = BY_OFFICES
                        header = "№ п/п;Отдел;ФИО;Таб. №;Телефон;Компьютеры"
                        headerInternet = "№ п/п;Отдел;Инв. №;IP-адрес;Кабинет"
                    }
                }

                val file = createExtFile(filename, SUB_DIR_USERS)

                val fullPath = "${Environment.DIRECTORY_DOWNLOADS}/ $DIR_DOCS/ $SUB_DIR_USERS/ $filename"

                var fileOutputStream: FileOutputStream? = null
                try {
                    fileOutputStream = FileOutputStream(file, true)

                    fileOutputStream.write("Список сотрудиков на $date\n".toByteArray())
                    fileOutputStream.write("($sort)\n".toByteArray())
                    fileOutputStream.write("$header\n".toByteArray())

                    users.filter { !it.family.contains(INTERNET) }.sortedList(sortedBy).forEachIndexed { index, user ->
                        val data = when (sortedBy) {
                            BY_FAMILY -> "${index + 1};${user.family} ${user.name} ${user.patronymic};" +
                                    "${user.serviceNumber};${user.phone};${user.office};${user.pcList}\n"

                            BY_SERVICE_NUMBER -> "${index + 1};${user.serviceNumber};" +
                                    "${user.family} ${user.name} ${user.patronymic};" +
                                    "${user.phone};${user.office};${user.pcList}\n"

                            BY_OFFICES -> "${index + 1};${user.office};" +
                                    "${user.family} ${user.name} ${user.patronymic};" +
                                    "${user.serviceNumber};${user.phone};${user.pcList}\n"

                            else -> ""
                        }
                        fileOutputStream.write(data.toByteArray())
                    }
                    val usersInternet = users.filter { it.family == INTERNET }.sortedList(sortedByInternet)
                    if (usersInternet.isNotEmpty()) {
                        fileOutputStream.write("Список компьютеров под Internet\n".toByteArray())
                        fileOutputStream.write("$headerInternet\n".toByteArray())

                        usersInternet.forEachIndexed { index, user ->
                            val data = when (sortedBy) {
                                BY_FAMILY -> "${index + 1};${user.pcList};${user.serviceNumber};" +
                                        "${user.name};${user.office}\n"

                                BY_SERVICE_NUMBER -> "${index + 1};${user.serviceNumber};${user.pcList};" +
                                        "${user.name};${user.office}\n"

                                BY_OFFICES -> "${index + 1};${user.office};${user.pcList};" +
                                        "${user.serviceNumber};${user.name}\n"

                                else -> ""
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

            is UserListEvent.OnUserListWithoutInternet -> {
                withoutInternet = !withoutInternet

                foundUsers = if (withoutInternet)
                    users.filter { it.family != INTERNET }
                else
                    users

                val idx = foundUsers.indexOf(users.find { it.id == currentId })
                position = if (idx > 0) idx else 0

                title = setTitle(USERS, foundUsers.size, users.size)
            }
        }
    }
}