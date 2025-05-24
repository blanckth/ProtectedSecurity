package com.android.prosecagent.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_metadata")
data class DeviceMetadata(
    @PrimaryKey val deviceId: String, // UUID یا Android ID
    val deviceName: String?, // Build.DEVICE
    val manufacturer: String?, // Build.MANUFACTURER
    val model: String?, // Build.MODEL
    val brand: String?, // Build.BRAND
    val hardware: String?, // Build.HARDWARE
    val board: String?, // Build.BOARD
    val product: String?, // Build.PRODUCT
    val bootloader: String?, // Build.BOOTLOADER
    val androidVersion: String?, // Build.VERSION.RELEASE
    val sdkInt: Int?, // Build.VERSION.SDK_INT
    val securityPatch: String?, // Build.VERSION.SECURITY_PATCH
    val baseband: String?, // SystemProp if accessible
    val fingerprint: String?, // Build.FINGERPRINT
    val buildId: String?, // Build.ID
    val buildTags: String?, // Build.TAGS
    val buildType: String?, // Build.TYPE
    val buildTime: Long?, // Build.TIME
    val isEmulator: Boolean,
    val screenResolution: String?,
    val screenDensity: String?,
    val locale: String?,
    val timezone: String?,
    val batteryLevel: Int?,
    val isCharging: Boolean?,
    val batteryHealth: String?,
    val batteryTemperature: Float?,
    val networkType: String?, // WiFi/Mobile/etc
    val ipAddress: String?,
    val macAddress: String?,
    val simOperatorName: String?,
    val simCountryIso: String?,
    val simState: String?,
    val carrierId: String?,
    val isRoaming: Boolean?,
    val installedAppsCount: Int?,
    val activeAdminCount: Int?,
    val accessibilityServicesEnabled: Boolean,
    val isDeviceEncrypted: Boolean,
    val isUsbDebuggingEnabled: Boolean,
    val isPlayProtectEnabled: Boolean?, // نیازمند گواهی Play Services
    val isVpnActive: Boolean,
    val totalRam: Long?,
    val availableRam: Long?,
    val totalStorage: Long?,
    val availableStorage: Long?,
    val timeCaptured: Long = System.currentTimeMillis()
)
