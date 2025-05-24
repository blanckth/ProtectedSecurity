package com.android.prosecagent.collectors

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager
import com.android.prosecagent.data.entity.DeviceMetadata
import java.util.*

object DeviceInfoCollector {

    @SuppressLint("HardwareIds", "MissingPermission")
    fun collect(context: Context): DeviceMetadata {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics().apply {
            wm.defaultDisplay.getMetrics(this)
        }

        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val pm = context.packageManager
        val appsCount = pm.getInstalledApplications(PackageManager.GET_META_DATA).size

        val statFs = Environment.getDataDirectory().let { android.os.StatFs(it.path) }
        val totalStorage = statFs.blockCountLong * statFs.blockSizeLong
        val availableStorage = statFs.availableBlocksLong * statFs.blockSizeLong

        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: UUID.randomUUID().toString()

        return DeviceMetadata(
            deviceId = deviceId,
            deviceName = Build.DEVICE,
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            brand = Build.BRAND,
            hardware = Build.HARDWARE,
            board = Build.BOARD,
            product = Build.PRODUCT,
            bootloader = Build.BOOTLOADER,
            androidVersion = Build.VERSION.RELEASE,
            sdkInt = Build.VERSION.SDK_INT,
            securityPatch = Build.VERSION.SECURITY_PATCH,
            baseband = try {
                Build.getRadioVersion() // May return null on newer Android
            } catch (_: Exception) {
                null
            },
            fingerprint = Build.FINGERPRINT,
            buildId = Build.ID,
            buildTags = Build.TAGS,
            buildType = Build.TYPE,
            buildTime = Build.TIME,
            isEmulator = Build.FINGERPRINT.contains("generic") || Build.MODEL.contains("Emulator"),
            screenResolution = "${displayMetrics.widthPixels}x${displayMetrics.heightPixels}",
            screenDensity = "${displayMetrics.densityDpi}dpi",
            locale = Locale.getDefault().toString(),
            timezone = TimeZone.getDefault().id,
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY),
            isCharging = batteryManager.isCharging(context),
            batteryHealth = getBatteryHealth(context),
            batteryTemperature = getBatteryTemperature(context),
            networkType = telephonyManager.networkType.toString(),
            ipAddress = null,// NetworkUtils.getLocalIpAddress(), // You’ll implement
            macAddress = null,// NetworkUtils.getMacAddress(context), // You’ll implement
            simOperatorName = telephonyManager.simOperatorName,
            simCountryIso = telephonyManager.simCountryIso,
            simState = simStateToString(telephonyManager.simState),
            carrierId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) telephonyManager.simCarrierId.toString() else null,
            isRoaming = telephonyManager.isNetworkRoaming,
            installedAppsCount = appsCount,
            activeAdminCount = null,// AdminUtils.getActiveAdminsCount(context), // Optional util
            accessibilityServicesEnabled = false,// AccessibilityUtils.isEnabled(context), // Optional util
            isDeviceEncrypted = false,//EncryptionUtils.isEncrypted(context),
            isUsbDebuggingEnabled = false,// DeveloperUtils.isUsbDebuggingEnabled(context),
            isPlayProtectEnabled = true, // Needs extra effort
            isVpnActive = false,// VpnUtils.isVpnActive(context), // You’ll implement
            totalRam = null,// DeviceMemoryUtils.getTotalRAM(),
            availableRam = null,// DeviceMemoryUtils.getAvailableRAM(context),
            totalStorage = totalStorage,
            availableStorage = availableStorage
        )
    }

    private fun BatteryManager.isCharging(context: Context): Boolean {
        val intent = context.registerReceiver(null, android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED))
        val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
    }

    private fun getBatteryHealth(context: Context): String {
        val intent = context.registerReceiver(null, android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED))
        return when (intent?.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Failure"
            else -> "Unknown"
        }
    }

    private fun getBatteryTemperature(context: Context): Float {
        val intent = context.registerReceiver(null, android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED))
        return (intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0) / 10f
    }

    private fun simStateToString(state: Int): String {
        return when (state) {
            TelephonyManager.SIM_STATE_ABSENT -> "Absent"
            TelephonyManager.SIM_STATE_READY -> "Ready"
            TelephonyManager.SIM_STATE_PIN_REQUIRED -> "PIN Required"
            TelephonyManager.SIM_STATE_PUK_REQUIRED -> "PUK Required"
            TelephonyManager.SIM_STATE_NETWORK_LOCKED -> "Network Locked"
            TelephonyManager.SIM_STATE_UNKNOWN -> "Unknown"
            else -> "Other"
        }
    }
}
