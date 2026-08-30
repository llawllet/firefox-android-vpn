package com.example.firefoxvpn.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.firefoxvpn.R
import com.example.firefoxvpn.vpn.FirefoxVpnService
import com.example.firefoxvpn.vpn.VpnState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Check

class MainActivity : ComponentActivity() {

    private val startVpnServiceIntent = Intent(this, FirefoxVpnService::class.java)
    private val stopVpnServiceIntent = Intent(this, FirefoxVpnService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen(
                    startVpnService = { startService(startVpnServiceIntent) },
                    stopVpnService = { stopService(stopVpnServiceIntent) },
                    isBound = /* TODO */ false,
                    vpnState = /* TODO */ VpnState.Disconnected,
                    onLocationSelected = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    startVpnService: () -> Unit,
    stopVpnService: () -> Unit,
    isBound: Boolean,
    vpnState: VpnState,
    onLocationSelected: ((String) -> Unit) = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Firefox VPN") },
                actions = {
                    IconButton(onClick = { /* TODO: open settings */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Status",
                    style = MaterialTheme.typography.titleMedium
                )
                when (vpnState) {
                    VpnState.Connected -> {
                        Text(
                            text = "Connected",
                            color = Color.Green,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    VpnState.Connecting -> {
                        Text(
                            text = "Connecting...",
                            color = Color.Yellow,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    VpnState.Disconnected -> {
                        Text(
                            text = "Disconnected",
                            color = Color.Red,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Connect/Disconnect Button
            Button(
                onClick = {
                    if (vpnState == VpnState.Connected) {
                        stopVpnService()
                    } else {
                        startVpnService()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isBound /* TODO */
            ) {
                when (vpnState) {
                    VpnState.Connected -> Text("Disconnect")
                    VpnState.Connecting -> Text("Connecting...")
                    else -> Text("Connect")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Location Selector
            Text(
                text = "Location",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .border(1.dp, Color.Gray)
            ) {
                items(listOf("United States", "Canada", "United Kingdom", "Japan", "Germany")) { country ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLocationSelected(country) }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = country,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            alpha = if (/* TODO */ false) 1f else 0f
                        )
                    }
                }
            }
        }
    }
}