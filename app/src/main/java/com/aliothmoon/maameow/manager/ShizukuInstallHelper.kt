package com.aliothmoon.maameow.manager

import android.content.Context
import com.topjohnwu.superuser.Shell
import rikka.sui.Sui

object ShizukuInstallHelper {

    enum class ShizukuStatus {
        READY,
        SUI_AVAILABLE,
        NOT_RUNNING
    }

    fun checkStatus(context: Context): ShizukuStatus {
        val isSui = try { Sui.init(context.packageName) } catch (_: Exception) { false }
        if (isSui) return ShizukuStatus.SUI_AVAILABLE
        if (ShizukuManager.isShizukuAvailable()) return ShizukuStatus.READY
        return ShizukuStatus.NOT_RUNNING
    }

    fun hasRoot(): Boolean = try { Shell.getShell().isRoot } catch (_: Exception) { false }

    fun startSetup(context: Context) = ShizukuSetupManager.startSetup(context)

    fun getSetupState() = ShizukuSetupManager.setupState.value
}
