package com.aliothmoon.maameow.manager

import android.content.Context
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import java.net.InetSocketAddress
import java.net.Socket

object AdbConnectionManager {

    private const val ADB_PORT = 5555
    private const val ADB_PAIR_PORT_DEFAULT = 0

    enum class ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        PAIRING_REQUIRED,
        ERROR
    }

    data class AdbState(
        val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
        val host: String = "",
        val port: Int = ADB_PORT,
        val pairingPort: Int = ADB_PAIR_PORT_DEFAULT,
        val isWirelessDebugEnabled: Boolean = false,
        val errorMessage: String? = null
    )

    private val _state = MutableStateFlow(AdbState())
    val state: StateFlow<AdbState> = _state.asStateFlow()

    fun isWirelessDebugEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                // Check if wireless debugging is enabled via settings
                val contentResolver = android.app.ActivityThread.currentApplication()
                    .contentResolver
                android.provider.Settings.Global.getInt(
                    contentResolver,
                    "adb_wifi_enabled",
                    0
                ) == 1
            } catch (e: Exception) {
                Timber.w(e, "Failed to check wireless debug status")
                false
            }
        } else {
            false
        }
    }

    fun checkAdbConnection(host: String = "localhost", port: Int = ADB_PORT): Boolean {
        return try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(host, port), 3000)
                true
            }
        } catch (e: Exception) {
            Timber.d(e, "ADB connection check failed")
            false
        }
    }

    fun updateState(newState: AdbState) {
        _state.value = newState
    }

    fun setConnecting() {
        _state.value = _state.value.copy(
            connectionState = ConnectionState.CONNECTING,
            errorMessage = null
        )
    }

    fun setConnected() {
        _state.value = _state.value.copy(
            connectionState = ConnectionState.CONNECTED,
            errorMessage = null
        )
    }

    fun setPairingRequired() {
        _state.value = _state.value.copy(
            connectionState = ConnectionState.PAIRING_REQUIRED,
            errorMessage = null
        )
    }

    fun setError(message: String) {
        _state.value = _state.value.copy(
            connectionState = ConnectionState.ERROR,
            errorMessage = message
        )
    }

    fun reset() {
        _state.value = AdbState()
    }
}
