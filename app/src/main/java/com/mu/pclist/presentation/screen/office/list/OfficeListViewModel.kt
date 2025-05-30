package com.mu.pclist.presentation.screen.office.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.pclist.data.entity.OfficeEntity
import com.mu.pclist.domain.repository.OfficeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfficeListViewModel @Inject constructor(
    private val officeRepository: OfficeRepository
) : ViewModel() {
    var offices = officeRepository.officeList()

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