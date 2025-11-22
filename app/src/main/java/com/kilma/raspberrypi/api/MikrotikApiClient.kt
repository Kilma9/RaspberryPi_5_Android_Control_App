package com.kilma.raspberrypi.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class MikrotikApiClient {
    private var baseUrl = ""
    private var credentials = ""
    private val client: OkHttpClient

    init {
        // Trust all certificates for MikroTik self-signed certs
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        client = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    fun configure(ip: String, port: String, username: String, password: String, useHttps: Boolean = false) {
        baseUrl = if (useHttps) {
            "https://$ip:$port/rest"
        } else {
            "http://$ip:$port/rest"
        }
        credentials = Credentials.basic(username, password)
    }

    suspend fun testConnection(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/system/resource")
                .header("Authorization", credentials)
                .get()
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                Result.success("Connected successfully")
            } else {
                Result.failure(Exception("HTTP ${response.code}: ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun executeCommand(path: String, method: String = "GET", params: Map<String, String>? = null): Result<String> = withContext(Dispatchers.IO) {
        try {
            val url = "$baseUrl$path"
            val requestBuilder = Request.Builder()
                .url(url)
                .header("Authorization", credentials)

            when (method.uppercase()) {
                "GET" -> requestBuilder.get()
                "POST" -> {
                    val jsonBody = params?.let { JSONObject(it).toString() } ?: "{}"
                    requestBuilder.post(jsonBody.toRequestBody("application/json".toMediaType()))
                }
                "PATCH" -> {
                    val jsonBody = params?.let { JSONObject(it).toString() } ?: "{}"
                    requestBuilder.patch(jsonBody.toRequestBody("application/json".toMediaType()))
                }
                "DELETE" -> requestBuilder.delete()
                else -> requestBuilder.get()
            }

            val response = client.newCall(requestBuilder.build()).execute()
            val body = response.body?.string() ?: ""

            if (response.isSuccessful) {
                Result.success(body)
            } else {
                Result.failure(Exception("HTTP ${response.code}: $body"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isConfigured(): Boolean = baseUrl.isNotEmpty() && credentials.isNotEmpty()
}
