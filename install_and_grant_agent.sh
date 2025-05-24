#!/data/data/com.termux/files/usr/bin/bash

# ==========================================
# ProtectedSecurityAgent ADB Installer & Permission Granter
# Written by Salar Muhammadi (mhmdi.salar@gmail.com)
# Tested on rooted Galaxy A54 using Termux
# ==========================================

# Agent APK file location (adjust path if needed)
AGENT_APK="/sdcard/ProtectedSecurityAgent.apk"

# Agent package name
PACKAGE_NAME="com.android.prosecagent"

# Permissions to grant
PERMISSIONS=(
  "android.permission.READ_PHONE_STATE"
  "android.permission.ACCESS_NETWORK_STATE"
  "android.permission.ACCESS_WIFI_STATE"
  "android.permission.ACCESS_COARSE_LOCATION"
  "android.permission.ACCESS_FINE_LOCATION"
  "android.permission.BATTERY_STATS"
  "android.permission.CAMERA"
  "android.permission.RECORD_AUDIO"
  "android.permission.READ_EXTERNAL_STORAGE"
  "android.permission.WRITE_EXTERNAL_STORAGE"
  "android.permission.PACKAGE_USAGE_STATS"
)

echo "📦 Installing ProtectedSecurityAgent to connected device..."

# If running on local rooted A54, use local install
if [[ "$1" == "local" ]]; then
    # pm install -r "$AGENT_APK"
    for perm in "${PERMISSIONS[@]}"; do
        echo "Granting : $perm"
        pm grant "$PACKAGE_NAME" "$perm"
    done
    echo "✅ Installed and granted all permissions on local (rooted) device."

# Else use adb for target non-rooted device
else
    adb wait-for-device

    echo "🔌 Installing APK via ADB..."
    adb install -r "$AGENT_APK"

    echo "🔑 Granting runtime permissions..."
    for perm in "${PERMISSIONS[@]}"; do
        adb shell pm grant "$PACKAGE_NAME" "$perm"
    done

    echo "⚙️ Manually activate usage access..."
    adb shell am start -a android.settings.USAGE_ACCESS_SETTINGS

    echo "✅ Agent installed and permissions granted on connected device."
    echo "👉 Please enable Usage Access manually if not auto-approved."
fi
