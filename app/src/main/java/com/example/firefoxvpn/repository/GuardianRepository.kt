package com.example.firefoxvpn.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.firefoxvpn.network.ProxyPassInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import org.json.JSONObject
import java.io.IOException
import java.util.*

/**
 * Repository for handling Guardian proxy pass.
 * Uses EncryptedSharedPreferences for secure storage.
 */
class GuardianRepository(
    private val context: Context,
    private val authRepository: AuthRepository
) {

    private val masterKeyAlias by lazy {
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    }

    private val prefs by lazy {
        EncryptedSharedPreferences.create(
            "firefox_vpn_guardian_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val proxyPassJson by lazy {
        prefs.getString("proxy_pass", "") ?: ""
    }

    private val proxyPass by lazy {
        if (proxyPassJson.isNotEmpty()) {
            ProxyPassInfo.fromJson(proxyPassJson)
        } else {
            null
        }
    }

    private val refreshMutex = Mutex()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    /**
     * Get a valid proxy pass, refreshing if necessary.
     * @return Proxy pass info
     */
    suspend fun getProxyPass(): ProxyPassInfo {
        return withContext(Dispatchers.IO) {
            val accessToken = authRepository.getValidAccessToken()
                ?: throw IllegalStateException("No valid access token available")

            // If we have a proxy pass that is not expired and not close to expiration, use it
            val currentPass = proxyPass
            if (currentPass != null && !currentPass.isExpired && !isCloseToExpiration(currentPass)) {
                currentPass
            } else {
                // Otherwise, fetch a new one
                fetchNewProxyPass(accessToken)
            }
        }
    }

    /**
     * Start a background task to renew the proxy pass before it expires.
     * This should be called when a proxy pass is obtained.
     */
    fun startAutoRenew() {
        coroutineScope.launch {
            while (true) {
                val pass = proxyPass
                if (pass == null) {
                    delay(60 * 1000) // Wait a minute and check again
                    continue
                }

                val timeToExpiration = pass.Exp * 1000 - System.currentTimeMillis()
                if (timeToExpiration > 0) {
                    // Wait until 5 minutes before expiration, or half the time, whichever is smaller
                    val waitTime = minOf(timeToExpiration - 5 * 60 * 1000, timeToExpiration / 2)
                    if (waitTime > 0) {
                        delay(waitTime)
                    }
                } else {
                    // Already expired, renew immediately
                    delay(0)
                }

                // Try to renew
                try {
                    renewProxyPass()
                } catch (e: Exception) {
                    // Log and continue
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Renew the proxy pass using the current access token.
     */
    suspend fun renewProxyPass() {
        val accessToken = authRepository.getValidAccessToken()
            ?: throw IllegalStateException("No valid access token available for renewal")
        fetchNewProxyPass(accessToken)
    }

    /**
     * Clear the stored proxy pass.
     */
    fun clearProxyPass() {
        prefs.edit().remove("proxy_pass").apply()
    }

    // ======================
    // Private helper methods
    // ======================

    /**
     * Fetch a new proxy pass from Guardian.
     */
    private suspend fun fetchNewProxyPass(accessToken: String): ProxyPassInfo {
        return withContext(Dispatchers.IO) {
            // TODO: Implement actual Guardian API call
            // This involves making a request to https://vpn.mozilla.org/api/v1/account/proxy-token
            // with the access token in the Authorization header
            throw UnsupportedOperationException("Guardian proxy pass fetch not implemented")
        }
    }

    /**
     * Check if the proxy pass is close to expiration (within 5 minutes).
     */
    private fun isCloseToExpiration(pass: ProxyPassInfo): Boolean {
        val timeToExpiration = pass.Exp * 1000 - System.currentTimeMillis()
        return timeToExpiration < 5 * 60 * 1000 // 5 minutes
    }
}