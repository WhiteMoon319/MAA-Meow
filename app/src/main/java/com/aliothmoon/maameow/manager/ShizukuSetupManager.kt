package com.aliothmoon.maameow.manager

import android.content.Context
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader

object ShizukuSetupManager {

    private const val TAG = "ShizukuSetupManager"
    private const val SHIZUKU_SERVER_CLASS = "rikka.shizuku.server.ShizukuService"
    private const val SHIZUKU_STARTER_CLASS = "moe.shizuku.starter.ServiceStarter"
    private const val ADB_PORT = 5555
    private const val CONNECTION_CHECK_INTERVAL = 2000L
    private const val MAX_CONNECTION_ATTEMPTS = 30

    enum class SetupState {
        IDLE,
        CHECKING_WIRELESS_DEBUG,
        WAITING_FOR_PAIRING,
        CONNECTING_ADB,
        STARTING_SHIZUKU,
        SHIZUKU_RUNNING,
        ERROR
    }

    data class SetupStatus(
        val state: SetupState = SetupState.IDLE,
        val message: String = "",
        val pairingCode: String = "",
        val pairingPort: Int = 0,
        val error: String? = null
    )

    private val _setupState = MutableStateFlow(SetupStatus())
    val setupState: StateFlow<SetupStatus> = _setupState.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var isSetupRunning = false

    fun startSetup(context: Context) {
        if (isSetupRunning) {
            Timber.w("Setup already running")
            return
        }

        isSetupRunning = true
        scope.launch {
            try {
                performSetup(context)
            } catch (e: Exception) {
                Timber.e(e, "Setup failed")
                _setupState.value = SetupStatus(
                    state = SetupState.ERROR,
                    error = e.message ?: "Unknown error"
                )
            } finally {
                isSetupRunning = false
            }
        }
    }

    private suspend fun performSetup(context: Context) {
        // Step 1: Check if wireless debugging is enabled
        _setupState.value = SetupStatus(
            state = SetupState.CHECKING_WIRELESS_DEBUG,
            message = "Checking wireless debugging..."
        )

        if (!isWirelessDebugEnabled()) {
            _setupState.value = SetupStatus(
                state = SetupState.WAITING_FOR_PAIRING,
                message = "Please enable wireless debugging"
            )
            // Wait for user to enable wireless debugging
            waitForWirelessDebug()
        }

        // Step 2: Check ADB connection
        _setupState.value = SetupStatus(
            state = SetupState.CONNECTING_ADB,
            message = "Connecting to ADB..."
        )

        if (!waitForAdbConnection()) {
            _setupState.value = SetupStatus(
                state = SetupState.ERROR,
                error = "Failed to connect to ADB"
            )
            return
        }

        // Step 3: Start Shizuku server
        _setupState.value = SetupStatus(
            state = SetupState.STARTING_SHIZUKU,
            message = "Starting Shizuku server..."
        )

        if (!startShizukuServer(context)) {
            _setupState.value = SetupStatus(
                state = SetupState.ERROR,
                error = "Failed to start Shizuku server"
            )
            return
        }

        // Step 4: Wait for Shizuku to be ready
        if (!waitForShizukuReady()) {
            _setupState.value = SetupStatus(
                state = SetupState.ERROR,
                error = "Shizuku server failed to start"
            )
            return
        }

        _setupState.value = SetupStatus(
            state = SetupState.SHIZUKU_RUNNING,
            message = "Shizuku is running"
        )
    }

    private fun isWirelessDebugEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val process = Runtime.getRuntime().exec("settings get global adb_wifi_enabled")
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                val result = reader.readLine()?.trim()
                process.waitFor()
                result == "1"
            } catch (e: Exception) {
                Timber.w(e, "Failed to check wireless debug status")
                false
            }
        } else {
            false
        }
    }

    private suspend fun waitForWirelessDebug() {
        var attempts = 0
        while (!isWirelessDebugEnabled() && attempts < MAX_CONNECTION_ATTEMPTS) {
            delay(CONNECTION_CHECK_INTERVAL)
            attempts++
        }
        if (!isWirelessDebugEnabled()) {
            throw Exception("Wireless debugging not enabled")
        }
    }

    private suspend fun waitForAdbConnection(): Boolean {
        var attempts = 0
        while (attempts < MAX_CONNECTION_ATTEMPTS) {
            if (AdbConnectionManager.checkAdbConnection()) {
                return true
            }
            delay(CONNECTION_CHECK_INTERVAL)
            attempts++
        }
        return false
    }

    private suspend fun startShizukuServer(context: Context): Boolean {
        return try {
            val command = buildShizukuStartCommand(context)
            Timber.d("Starting Shizuku server with command: $command")
            val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
            val exitCode = process.waitFor()
            exitCode == 0
        } catch (e: Exception) {
            Timber.e(e, "Failed to start Shizuku server")
            false
        }
    }

    private fun buildShizukuStartCommand(context: Context): String {
        val processName = "${context.packageName}:shizuku_server"
        val appProcess = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            "app_process"
        } else {
            "app_process"
        }
        val classpath = context.applicationInfo.sourceDir

        return "CLASSPATH='$classpath' $appProcess /system/bin --nice-name='$processName' $SHIZUKU_SERVER_CLASS &"
    }

    private suspend fun waitForShizukuReady(): Boolean {
        var attempts = 0
        while (attempts < MAX_CONNECTION_ATTEMPTS) {
            if (ShizukuManager.isShizukuAvailable()) {
                return true
            }
            delay(CONNECTION_CHECK_INTERVAL)
            attempts++
        }
        return false
    }

    fun reset() {
        _setupState.value = SetupStatus()
        isSetupRunning = false
    }
}
