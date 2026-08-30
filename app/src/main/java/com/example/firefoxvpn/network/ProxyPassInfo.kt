package com.example.firefoxvpn.network

import kotlinx.serializable.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date

@Serializable
data class ProxyPassInfo(
    val RawToken: String,
    val Sub: String,
    val Aud: String,
    val Iss: String?,
    val Exp: Long, // expiration timestamp in seconds
    val Nbf: Long, // not before timestamp in seconds
    val Iat: Long, // issued at timestamp in seconds
    val QuotaMax: String?,
    val QuotaLeft: String?,
    // We'll compute the expiration date as a convenience
    val expirationDate: Date by lazy {
        Date(Exp * 1000)
    },
    val isExpired: Boolean by lazy {
        System.currentTimeMillis() > Exp * 1000
    }
) {
    companion object {
        private val json = Json { ignoreUnknownKeys = true; isLenient = true }

        fun fromJson(jsonString: String): ProxyPassInfo {
            return json.decodeFromString<ProxyPassInfo>(jsonString)
        }

        fun toJson(info: ProxyPassInfo): String {
            return json.encodeToString(info)
        }
    }
}