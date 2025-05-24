import android.content.Context
import android.os.Build
import android.os.SystemClock
import java.util.Locale
import java.util.TimeZone

class MetadataCollector(private val context: Context) {
    fun collect(): DeviceMetadataEntity {
        val build = Build::class.java
        val metrics = context.resources.displayMetrics

        return DeviceMetadataEntity(
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            brand = Build.BRAND,
            device = Build.DEVICE,
            product = Build.PRODUCT,
            hardware = Build.HARDWARE,
            board = Build.BOARD,
            fingerprint = Build.FINGERPRINT,
            bootloader = Build.BOOTLOADER,
            radioVersion = Build.getRadioVersion(),
            serial = Build.SERIAL.takeIf { Build.VERSION.SDK_INT < 29 },

            versionRelease = Build.VERSION.RELEASE,
            sdkInt = Build.VERSION.SDK_INT,
            securityPatch = Build.VERSION.SECURITY_PATCH,
            buildId = Build.ID,
            buildTags = Build.TAGS,
            buildTime = Build.TIME,

            screenWidth = metrics.widthPixels,
            screenHeight = metrics.heightPixels,
            screenDensityDpi = metrics.densityDpi,
            screenSizeInches = 0f, // TODO: calculate with metrics

            supportedAbis = Build.SUPPORTED_ABIS.joinToString(","),
            cpuCores = Runtime.getRuntime().availableProcessors(),
            totalRam = 0L, // TODO: get from ActivityManager
            availableRam = 0L,

            totalStorage = 0L, // TODO
            availableStorage = 0L,

            batteryLevel = 0,
            isCharging = false,
            batteryHealth = "Unknown",
            batteryTemperature = 0f,

            locale = Locale.getDefault().toString(),
            timezone = TimeZone.getDefault().id,
            bootTime = SystemClock.elapsedRealtime(),

            wifiSsid = null, // TODO: with ACCESS_FINE_LOCATION + ACCESS_WIFI_STATE
            wifiBssid = null,
            wifiIpAddress = null,
            macAddress = null,
            bluetoothMac = null,
            isVpnActive = false,

            simOperatorName = null,
            simCountryIso = null,
            simSerialNumber = null,
            networkType = "UNKNOWN",
            isRoaming = false,
            imeiList = null,

            isAccessibilityEnabled = false,
            isDeviceAdmin = false,
            isUsbDebuggingEnabled = false,
            isUnknownSourcesAllowed = false,
            isDevelopmentSettingsEnabled = false,
            canDrawOverlays = false,
            canUsageStats = false,
            isKeyguardSecure = false,

            appVersionName = "1.0",
            appVersionCode = 1L,
            installerPackage = context.packageManager.getInstallerPackageName(context.packageName),
            isDebugBuild = BuildConfig.DEBUG,

            isEmulator = false,
            bootCount = Settings.Global.getInt(context.contentResolver, Settings.Global.BOOT_COUNT, 0),
            isRootedPossibly = false,
            isPlayProtectEnabled = true
        )
    }
}
