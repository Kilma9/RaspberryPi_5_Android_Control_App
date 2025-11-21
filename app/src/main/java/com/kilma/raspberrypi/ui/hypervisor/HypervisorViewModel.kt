package com.kilma.raspberrypi.ui.hypervisor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HypervisorViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Hypervisor Control"
    }
    val text: LiveData<String> = _text
}
