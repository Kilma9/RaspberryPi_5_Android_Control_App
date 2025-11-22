package com.kilma.raspberrypi.ui.mikrotik

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kilma.raspberrypi.api.MikrotikApiClient
import kotlinx.coroutines.launch

class MikrotikViewModel : ViewModel() {

    private val _terminalOutput = MutableLiveData<String>().apply {
        value = "MikroTik REST API Terminal\n" +
                "-------------------------\n" +
                "Enter connection details and click Connect.\n" +
                "HTTP (port 80) - www service\n" +
                "HTTPS (port 443) - www-ssl service\n"
    }
    val terminalOutput: LiveData<String> = _terminalOutput

    private val apiClient = MikrotikApiClient()
    private var currentIp = ""
    private var isConnected = false

    fun connect(ip: String, port: String, username: String, password: String, useHttps: Boolean = false) {
        currentIp = ip
        val protocol = if (useHttps) "HTTPS" else "HTTP"
        
        appendToTerminal("\n> Connecting to $ip:$port ($protocol)...")
        appendToTerminal("> User: $username")
        
        apiClient.configure(ip, port, username, password, useHttps)
        
        viewModelScope.launch {
            val result = apiClient.testConnection()
            if (result.isSuccess) {
                isConnected = true
                appendToTerminal("> Status: ${result.getOrNull()}")
                appendToTerminal("> Ready to send API requests.\n")
            } else {
                isConnected = false
                appendToTerminal("> ERROR: ${result.exceptionOrNull()?.message}")
                appendToTerminal("> Connection failed. Check IP, port, credentials, and www/www-ssl service.\n")
            }
        }
    }

    fun sendApiRequest(path: String, method: String = "GET", params: Map<String, String>? = null) {
        if (!isConnected) {
            appendToTerminal("\n> ERROR: Not connected. Click Connect first.")
            return
        }

        appendToTerminal("\n> $method $path")
        if (params != null && params.isNotEmpty()) {
            appendToTerminal("> Params: $params")
        }
        
        viewModelScope.launch {
            val result = apiClient.executeCommand(path, method, params)
            if (result.isSuccess) {
                val response = result.getOrNull() ?: ""
                appendToTerminal("> Response:")
                appendToTerminal(response)
            } else {
                appendToTerminal("> ERROR: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    // Quick actions
    fun getInterfaces() {
        sendApiRequest("/interface", "GET")
    }

    fun enableInterface(interfaceId: String) {
        sendApiRequest("/interface/$interfaceId", "PATCH", mapOf("disabled" to "false"))
    }

    fun disableInterface(interfaceId: String) {
        sendApiRequest("/interface/$interfaceId", "PATCH", mapOf("disabled" to "true"))
    }

    fun getSystemResource() {
        sendApiRequest("/system/resource", "GET")
    }

    fun disconnect() {
        if (isConnected) {
            appendToTerminal("\n> Disconnected from $currentIp.\n")
            isConnected = false
            currentIp = ""
        } else {
            appendToTerminal("\n> Not connected")
        }
    }

    private fun appendToTerminal(text: String) {
        _terminalOutput.value = _terminalOutput.value + "\n" + text
    }
}
