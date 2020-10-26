package com.poc.installedapps.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.ArraySet
import android.util.Log

class PackageHelper constructor(private var context: Context) {
    companion object {
        const val TAG = "PackageHelper"
    }

    private val packageManager by lazy { context.packageManager }

    fun getInstalledPackages(systemAppsOnly: Boolean? = null): List<String> {
        val list = ArraySet<String>()

        val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        for (packageInfo in packageInfoList) {
            val packageName = packageInfo.applicationInfo.packageName
            Log.d(TAG, "Found $packageName")
            when (systemAppsOnly) {
                true -> {
                    if (packageInfo.applicationInfo.isSystemPackage()) {
                        Log.d(TAG, "Found system app : $packageName")
                        list.add(packageName)
                    }
                }
                false -> {
                    if (!packageInfo.applicationInfo.isSystemPackage()) {
                        Log.d(TAG, "Found user app : $packageName")
                        list.add(packageName)
                    }
                }
                null -> {
                    Log.d(TAG, "Found installed app : $packageName")
                    list.add(packageName)
                }
            }
        }
        return list.sorted().toList()
    }

    private fun ApplicationInfo.isSystemPackage(): Boolean {
        val filterFlags = ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP
        return flags and filterFlags != 0
    }

    fun getAppIconByPackageName(packageName: String): Drawable? {
        val drawable = with(packageManager) {
            try {
                Log.d(
                    TAG,
                    "Found drawable"
                )
                getApplicationIcon(packageName)
            } catch (nnfe: PackageManager.NameNotFoundException) {
                Log.d(
                    TAG,
                    "Unable to get icon !"
                )
                null
            }
        }
        return drawable
    }

    fun getAppNameByPackageName(packageName: String): String? {
        val name = try {
            val packageManager = packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val appName = packageManager.getApplicationLabel(applicationInfo).toString()
            Log.d(
                TAG,
                "Found appName: $appName for packageName: $packageName"
            )
            appName
        } catch (nnfe: PackageManager.NameNotFoundException) {
            Log.d(
                TAG,
                "Unable to get app name !"
            )
            null
        }
        return name
    }

    fun getAppVersionByPackageName(packageName: String): String? {
        val version = try {
            val packageManager = packageManager
            val applicationInfo = packageManager.getPackageInfo(packageName, 0)
            Log.d(
                TAG,
                "Found versionName:${applicationInfo.versionName} for packageName:$packageName"
            )
            applicationInfo.versionName
        } catch (nnfe: PackageManager.NameNotFoundException) {
            Log.d(
                TAG,
                "Unable to get app version !"
            )
            null
        }
        return version
    }

    fun getAppVersionCodeByPackageName(packageName: String): Long? {
        val versionCode = try {
            val info = packageManager.getPackageInfo(packageName, 0)
            val vc = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
                info.versionCode.toLong()
            else
                info.longVersionCode
            Log.d(
                TAG,
                "Found versionCode:$vc for packageName:$packageName"
            )
            vc
        } catch (nnfe: PackageManager.NameNotFoundException) {
            Log.d(
                TAG,
                "Unable to get app version code !\n${nnfe.localizedMessage}"
            )
            null
        }
        return versionCode
    }

    fun getAppInstallationSourceByPackageName(packageName: String): String? {
        return try {
            val installationSource = packageManager.getInstallerPackageName(packageName)
            Log.d(
                TAG,
                "Found installationSource:$installationSource for packageName:$packageName"
            )
            installationSource
        } catch (iae: IllegalArgumentException) {
            Log.d(
                TAG,
                "Unable to get app installation source !\n${iae.localizedMessage}"
            )
            null
        }
    }

    fun isSystemApp(packageName: String): Boolean {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.applicationInfo.isSystemPackage()

        } catch (nnf: PackageManager.NameNotFoundException) {
            Log.d(
                TAG,
                "App $packageName is not installed"
            )
            false
        }
    }
}
