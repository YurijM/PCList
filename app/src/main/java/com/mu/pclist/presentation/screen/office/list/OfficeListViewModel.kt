package com.mu.pclist.presentation.screen.office.list

import androidx.lifecycle.ViewModel
import com.mu.pclist.domain.repository.OfficeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OfficeListViewModel @Inject constructor(
    officeRepository: OfficeRepository
) : ViewModel() {
    var offices = officeRepository.officeList()
}