package com.kilma.raspberrypi.ui.minecraft

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MinecraftViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Minecraft Server Control"
    }
    val text: LiveData<String> = _text
}
