package com.example.firefoxvpn.network

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val AccessToken: String,
    val RefreshToken: String,
    val ExpiresIn: Long,
    val Scope: String?,
    val TokenType: String
)