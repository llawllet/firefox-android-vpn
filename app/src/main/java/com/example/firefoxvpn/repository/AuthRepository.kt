package com.example.firefoxvpn.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.firefoxvpn.network.TokenResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.util.*

/**
 * Repository for handling Firefox Account authentication.
 * Uses EncryptedSharedPreferences for secure token storage.
 */
class AuthRepository(private val context: Context) {

    private val masterKeyAlias by lazy {
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    }

    private val prefs by lazy {
        EncryptedSharedPreferences.create(
            "firefox_vpn_auth_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val scope by lazy {
        prefs.getString("scope", "") ?: ""
    }

    private val accessToken by lazy {
        prefs.getString("access_token", "") ?: ""
    }

    private val refreshToken by lazy {
        prefs.getString("refresh_token", "") ?: ""
    }

    private val expiresIn by lazy {
        prefs.getLong("expires_in", 0)
    }

    private val tokenType by lazy {
        prefs.getString("token_type", "") ?: ""
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    /**
     * Log in with email and password to get a session token, then exchange for OAuth token.
     * @param email Firefox Account email
     * @param password Firefox Account password
     * @return OAuth token response
     */
    suspend fun login(email: String, password: String): TokenResponse {
        return withContext(Dispatchers.IO) {
            // Step 1: Get session token from FxA
            val sessionToken = getSessionToken(email, password)
            // Step 2: Exchange session token for OAuth token
            val tokenResponse = exchangeSessionTokenForOAuth(sessionToken)
            // Save tokens
            saveTokenResponse(tokenResponse)
            tokenResponse
        }
    }

    /**
     * Refresh the OAuth token using the refresh token.
     * @return New token response
     */
    suspend fun refreshToken(): TokenResponse {
        return withContext(Dispatchers.IO) {
            if (this.refreshToken.isEmpty()) {
                throw IllegalStateException("No refresh token available")
            }
            val tokenResponse = refreshOAuthToken(this.refreshToken)
            saveTokenResponse(tokenResponse)
            tokenResponse
        }
    }

    /**
     * Get the current access token if it's still valid.
     * @return Access token or null if expired/not available
     */
    fun getValidAccessToken(): String? {
        // In a real implementation, we would check the expiration time
        // For now, we just return the token if it exists
        return if (accessToken.isNotEmpty()) accessToken else null
    }

    /**
     * Clear stored tokens (e.g., on logout).
     */
    fun clearTokens() {
        prefs.edit().clear().apply()
    }

    // ======================
    // Private helper methods
    // ======================

    /**
     * Get session token from FxA using email and password.
     * This is a simplified version - the actual FxA login flow is more complex.
     */
    private fun getSessionToken(email: String, password: String): String {
        // TODO: Implement actual FxA login flow
        // This involves:
        // 1. Generating a verifier and challenge for PKCE
        // 2. Making a request to https://api.accounts.firefox.com/v1/account
        // 3. Handling the response and getting a session token
        // For now, we return a dummy token
        throw UnsupportedOperationException("FxA login not implemented")
    }

    /**
     * Exchange session token for OAuth token.
     */
    private fun exchangeSessionTokenForOAuth(sessionToken: String): TokenResponse {
        // TODO: Implement actual token exchange
        // This involves making a request to the FxA token endpoint with the session token
        throw UnsupportedOperationException("OAuth token exchange not implemented")
    }

    /**
     * Refresh the OAuth token using the refresh token.
     */
    private fun refreshOAuthToken(refreshToken: String): TokenResponse {
        // TODO: Implement actual token refresh
        // This involves making a request to the FxA token endpoint with the refresh token
        throw UnsupportedOperationException("OAuth token refresh not implemented")
    }

    /**
     * Save the token response to EncryptedSharedPreferences.
     */
    private fun saveTokenResponse(response: TokenResponse) {
        prefs.edit()
            .putString("access_token", response.AccessToken)
            .putString("refresh_token", response.RefreshToken)
            .putLong("expires_in", response.ExpiresIn)
            .putString("scope", response.Scope ?: "")
            .putString("token_type", response.TokenType)
            .apply()
    }
}