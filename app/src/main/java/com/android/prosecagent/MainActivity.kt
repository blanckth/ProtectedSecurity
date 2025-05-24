package com.android.prosecagent

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.prosecagent.databinding.ActivityMainBinding
import com.android.prosecagent.ui.screens.DeviceInfoFragment



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // شناسه‌ی یکتا برای درخواست پرمیژن‌ها
    private val PERMISSION_REQUEST_CODE = 1001

    // لیست پرمیژن‌هایی که در زمان اجرا باید از کاربر درخواست بشه
    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.SEND_SMS,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.GET_ACCOUNTS,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // بررسی و درخواست پرمیژن‌ها
        checkAndRequestPermissions()
        // TODO: This is the starting point for ProtectedSecurityAgent core modules
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, DeviceInfoFragment())
            .commit()

    }
    /**
     * بررسی اینکه آیا تمام پرمیژن‌ها داده شده‌اند.
     * در غیر اینصورت، درخواست پرمیژن‌ها.
     */
    private fun checkAndRequestPermissions() {
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        } else {
            onAllPermissionsGranted()
        }
    }

    /**
     * هندل کردن نتیجه‌ی درخواست پرمیژن‌ها
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            val deniedPermissions = permissions.zip(grantResults.toTypedArray())
                .filter { it.second != PackageManager.PERMISSION_GRANTED }
                .map { it.first }

            if (deniedPermissions.isEmpty()) {
                onAllPermissionsGranted()
            } else {
                Toast.makeText(
                    this,
                    "The following permissions were denied:\n${deniedPermissions.joinToString("\n")}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * تابعی که زمانی اجرا می‌شود که همه‌ی پرمیژن‌ها با موفقیت اعطا شده‌اند.
     */
    private fun onAllPermissionsGranted() {
        Toast.makeText(this, "All permissions granted!", Toast.LENGTH_SHORT).show()
        // ادامه اجرای لاگیک اصلی اپ اینجا
    }
}
