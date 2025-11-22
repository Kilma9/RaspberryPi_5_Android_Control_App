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
                val formatted = formatJsonResponse(response)
                appendToTerminal(formatted)
            } else {
                appendToTerminal("> ERROR: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    // Format JSON response for better readability
    private fun formatJsonResponse(json: String): String {
        if (json.isBlank()) return "(empty response)"
        
        return try {
            val jsonArray = org.json.JSONArray(json)
            val formatted = StringBuilder()
            
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                formatted.append("\n--- Item ${i + 1} ---")
                
                val keys = obj.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val value = obj.getString(key)
                    // Skip internal fields starting with dot
                    if (!key.startsWith(".")) {
                        formatted.append("\n  $key: $value")
                    } else if (key == ".id") {
                        formatted.append("\n  ID: $value")
                    }
                }
                formatted.append("\n")
            }
            
            formatted.toString()
        } catch (e: Exception) {
            // Try as single object
            try {
                val obj = org.json.JSONObject(json)
                val formatted = StringBuilder("\n")
                val keys = obj.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val value = obj.getString(key)
                    if (!key.startsWith(".")) {
                        formatted.append("\n  $key: $value")
                    } else if (key == ".id") {
                        formatted.append("\n  ID: $value")
                    }
                }
                formatted.toString()
            } catch (e2: Exception) {
                // Return as-is if not JSON
                json
            }
        }
    }

    // Quick actions
    fun getSystemResource() {
        sendApiRequest("/system/resource", "GET")
    }

    fun getSystemIdentity() {
        sendApiRequest("/system/identity", "GET")
    }

    fun getInterfaces() {
        sendApiRequest("/interface", "GET")
    }

    fun getIpAddresses() {
        sendApiRequest("/ip/address", "GET")
    }

    fun getRoutes() {
        sendApiRequest("/ip/route", "GET")
    }

    fun getFirewallRules() {
        sendApiRequest("/ip/firewall/filter", "GET")
    }

    fun getDhcpLeases() {
        sendApiRequest("/ip/dhcp-server/lease", "GET")
    }

    fun getWireless() {
        sendApiRequest("/interface/wireless", "GET")
    }

    fun getWirelessClients() {
        sendApiRequest("/interface/wireless/registration-table", "GET")
    }

    fun getSystemLogs() {
        sendApiRequest("/log", "GET")
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
