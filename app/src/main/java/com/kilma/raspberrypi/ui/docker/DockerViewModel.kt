package com.kilma.raspberrypi.ui.docker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DockerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Docker Control"
    }
    val text: LiveData<String> = _text
}
