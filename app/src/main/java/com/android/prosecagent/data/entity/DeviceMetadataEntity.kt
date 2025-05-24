package com.android.prosecagent.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Entity class to store device metadata for forensic and SIEM-level analysis.
 */
@Entity(tableName = "device_metadata")
data class DeviceMetadataEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),

    // Device Info
    val manufacturer: String,
    val model: String,
    val brand: String,
    val device: String,
    val product: String,
    val hardware: String,
    val board: String,
    val fingerprint: String,
    val bootloader: String,
    val radioVersion: String?,
    val serial: String?,  // Only pre-Android 10

    // OS and Build Info
    val versionRelease: String,
    val sdkInt: Int,
    val securityPatch: String?,
    val buildId: String,
    val buildTags: String,
    val buildTime: Long,

    // Screen Info
    val screenWidth: Int,
    val screenHeight: Int,
    val screenDensityDpi: Int,
    val screenSizeInches: Float,

    // CPU & Memory
    val supportedAbis: String, // comma-separated
    val cpuCores: Int,
    val totalRam: Long,
    val availableRam: Long,

    // Storage Info
    val totalStorage: Long,
    val availableStorage: Long,

    // Battery Info
    val batteryLevel: Int,
    val isCharging: Boolean,
    val batteryHealth: String,
    val batteryTemperature: Float,

    // Locale & Timezone
    val locale: String,
    val timezone: String,
    val bootTime: Long,

    // Network
    val wifiSsid: String?,
    val wifiBssid: String?,
    val wifiIpAddress: String?,
    val macAddress: String?,
    val bluetoothMac: String?,
    val isVpnActive: Boolean,

    // SIM & Telephony
    val simOperatorName: String?,
    val simCountryIso: String?,
    val simSerialNumber: String?, // only API < 29
    val networkType: String,
    val isRoaming: Boolean,
    val imeiList: String?, // comma-separated for multi-SIM

    // Permissions & Security
    val isAccessibilityEnabled: Boolean,
    val isDeviceAdmin: Boolean,
    val isUsbDebuggingEnabled: Boolean,
    val isUnknownSourcesAllowed: Boolean,
    val isDevelopmentSettingsEnabled: Boolean,
    val canDrawOverlays: Boolean,
    val canUsageStats: Boolean,
    val isKeyguardSecure: Boolean,

    // Application Info
    val appVersionName: String,
    val appVersionCode: Long,
    val installerPackage: String?,
    val isDebugBuild: Boolean,

    // Status & System
    val isEmulator: Boolean,
    val bootCount: Int,
    val isRootedPossibly: Boolean,
    val isPlayProtectEnabled: Boolean,

    val collectedAt: Long = System.currentTimeMillis()
)
