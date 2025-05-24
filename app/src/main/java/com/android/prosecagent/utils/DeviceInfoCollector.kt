package com.android.prosecagent.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.TrafficStats
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.content.getSystemService
import com.android.prosecagent.data.entity.DeviceMetadataEntity
import java.util.*

/**
 * Utility class to collect device information for forensic metadata.
 * Uses available Android APIs without root.
 */
class DeviceInfoCollector(private val context: Context) {

    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    fun collect(): DeviceMetadataEntity {

        val batteryStatus: Intent? = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        // Battery info
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = if (level >= 0 && scale > 0) (level * 100) / scale else -1

        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

        val temp = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)?.toFloat()?.div(10f) ?: -1f

        // Memory info
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memInfo)

        // Storage info
        val statFs = android.os.StatFs(context.filesDir.absolutePath)
        val internalStorageTotal = statFs.blockCountLong * statFs.blockSizeLong
        val internalStorageAvailable = statFs.availableBlocksLong * statFs.blockSizeLong

        // Screen metrics
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        // Device ID (ANDROID_ID)
        val androidID = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown"

        // SIM info (with permission checks, but assume granted)
        val simOperator = telephonyManager.simOperatorName.takeIf { it.isNotEmpty() }
        val simCountry = telephonyManager.simCountryIso.takeIf { it.isNotEmpty() }
        val simSerialNumber = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) null else telephonyManager.simSerialNumber
        val carrierName = telephonyManager.networkOperatorName.takeIf { it.isNotEmpty() }

        // IMEI (for API < 29) requires READ_PHONE_STATE permission
        val imei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) null else telephonyManager.deviceId

        // Phone number (may be null and requires permission)
        val phoneNumber = telephonyManager.line1Number

        // Device secure check (lock screen)
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as android.app.KeyguardManager
        val isDeviceSecure = keyguardManager.isDeviceSecure

        // Biometrics (simplified, requires BiometricManager from androidx.biometric)
        val hasBiometrics = try {
            val biometricManager = androidx.biometric.BiometricManager.from(context)
            biometricManager.canAuthenticate() == androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
        } catch (e: Exception) {
            false
        }

        // Accessibility enabled check
        val isAccessibilityEnabled = android.provider.Settings.Secure.getInt(
            context.contentResolver,
            android.provider.Settings.Secure.ACCESSIBILITY_ENABLED, 0
        ) == 1

        // Device Admin active check
        val deviceAdminEnabled = isDeviceAdminActive()

        // Build properties
        val isEmulator = Build.FINGERPRINT.contains("generic") || Build.MODEL.contains("Emulator")

        // Timezone and build time
        val timezone = TimeZone.getDefault().id
        val buildTime = Build.TIME

        // OS version & API level
        val osVersion = Build.VERSION.RELEASE ?: "unknown"
        val apiLevel = Build.VERSION.SDK_INT
        val buildID = Build.ID ?: "unknown"
        val buildTags = Build.TAGS ?: "unknown"

        // Boot time (uptime in ms since boot)
        val bootTime = System.currentTimeMillis() - android.os.SystemClock.elapsedRealtime()

        return DeviceMetadataEntity(
            deviceManufacturer = Build.MANUFACTURER,
            deviceModel = Build.MODEL,
            deviceBrand = Build.BRAND,
            deviceName = Build.DEVICE,
            product = Build.PRODUCT,
            hardware = Build.HARDWARE,
            board = Build.BOARD,
            bootloader = Build.BOOTLOADER,
            fingerprint = Build.FINGERPRINT,
            deviceID = androidID,

            osVersion = osVersion,
            apiLevel = apiLevel,
            buildID = buildID,
            buildTags = buildTags,
            buildTime = buildTime,
            isEmulator = isEmulator,

            batteryLevel = batteryPct,
            isCharging = isCharging,
            batteryTemperature = temp,

            totalRAM = memInfo.totalMem,
            availableRAM = memInfo.availMem,
            internalStorageTotal = internalStorageTotal,
            internalStorageAvailable = internalStorageAvailable,

            isDeviceSecure = isDeviceSecure,
            hasBiometrics = hasBiometrics,

            screenWidth = metrics.widthPixels,
            screenHeight = metrics.heightPixels,
            screenDensity = metrics.density,

            simOperator = simOperator,
            simCountry = simCountry,
            simSerialNumber = simSerialNumber,
            carrierName = carrierName,
            imei = imei,
            phoneNumber = phoneNumber,

            androidSecurityPatch = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Build.VERSION.SECURITY_PATCH else null,
            bootTime = bootTime,
            timezone = timezone,

            isAccessibilityEnabled = isAccessibilityEnabled,
            isDeviceAdminActive = deviceAdminEnabled
        )
    }

    private fun isDeviceAdminActive(): Boolean {
        // ToDo: Implement actual DeviceAdminReceiver check if necessary
        // Placeholder returning false for now
        return false
    }
}
