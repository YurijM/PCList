package com.mu.pclist.presentation.screen.office

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mu.pclist.data.entity.OfficeEntity
import com.mu.pclist.domain.repository.OfficeRepository
import com.mu.pclist.presentation.navigation.Destinations.OfficeDestination
import com.mu.pclist.presentation.util.NEW_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfficeViewModel @Inject constructor(
    private val officeRepository: OfficeRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var id by mutableLongStateOf(NEW_ID)
    var office by mutableStateOf(OfficeEntity())

    init {
        val args = savedStateHandle.toRoute<OfficeDestination>()
        id = args.id

        if (id != NEW_ID) {
            viewModelScope.launch {
                officeRepository.office(id).collect { item ->
                    office = OfficeEntity(
                        id = item.id,
                        code = item.code,
                        office = item.office,
                        shortName = item.shortName,
                        userId = item.userId
                    )
                }
            }
        }
    }
}