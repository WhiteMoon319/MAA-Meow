package com.aliothmoon.maameow.manager

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import rikka.sui.Sui
import timber.log.Timber

object ShizukuInstallHelper {

    private const val SHIZUKU_PACKAGE = "moe.shizuku.privileged.api"

    enum class ShizukuStatus {
        READY,              // Shizuku 服务正在运行
        SUI_AVAILABLE,      // 通过 Sui 提供服务
        SETUP_REQUIRED,     // 需要设置（无线调试未启用或 Shizuku 未启动）
        WIRELESS_DEBUG_OFF, // 无线调试未启用
        SHIZUKU_NOT_RUNNING // Shizuku 服务未运行
    }

    fun checkStatus(context: Context): ShizukuStatus {
        // Check if Sui is available
        val isSui = try { Sui.init(context.packageName) } catch (_: Exception) { false }
        if (isSui) {
            return ShizukuStatus.SUI_AVAILABLE
        }

        // Check if Shizuku binder is available
        if (ShizukuManager.isShizukuAvailable()) {
            return ShizukuStatus.READY
        }

        // Check if wireless debugging is enabled
        if (!isWirelessDebugEnabled()) {
            return ShizukuStatus.WIRELESS_DEBUG_OFF
        }

        return ShizukuStatus.SHIZUKU_NOT_RUNNING
    }

    fun isWirelessDebugEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val process = Runtime.getRuntime().exec("settings get global adb_wifi_enabled")
                val reader = process.inputStream.bufferedReader()
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

    fun openWirelessDebugSettings(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Android 11+ has dedicated wireless debugging settings
                val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
                context.startActivity(intent)
            } else {
                // Fallback to developer options
                val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to open wireless debug settings")
        }
    }

    fun startShizukuSetup(context: Context) {
        ShizukuSetupManager.startSetup(context)
    }

    fun isSetupRunning(): Boolean {
        return ShizukuSetupManager.setupState.value.state != ShizukuSetupManager.SetupState.IDLE &&
               ShizukuSetupManager.setupState.value.state != ShizukuSetupManager.SetupState.SHIZUKU_RUNNING &&
               ShizukuSetupManager.setupState.value.state != ShizukuSetupManager.SetupState.ERROR
    }

    fun getSetupState(): ShizukuSetupManager.SetupStatus {
        return ShizukuSetupManager.setupState.value
    }
}
