package cn.huangchengxi.uihlib.system

import android.os.Build
import android.os.Environment
import android.text.TextUtils
import java.io.File
import java.io.FileInputStream
import java.util.*


object RomUtils {
    val lightStatusBarAvailableRomType: Int
        get() {
            if (isMiUIV7OrAbove) {
                return AvailableRomType.ANDROID_NATIVE
            }
            if (isMiUIV6OrAbove) {
                return AvailableRomType.MIUI
            }
            if (isFlymeV4OrAbove) {
                return AvailableRomType.FLYME
            }
            return if (isAndroidMOrAbove) {
                AvailableRomType.ANDROID_NATIVE
            } else AvailableRomType.NA
        }//版本号4以上，形如4.x.

    private val isFlymeV4OrAbove: Boolean
        private get() {
            val displayId = Build.DISPLAY
            if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
                val displayIdArray =
                    displayId.split(" ".toRegex()).toTypedArray()
                for (temp in displayIdArray) {
                    //版本号4以上，形如4.x.
                    if (temp.matches(Regex("^[4-9]\\.(\\d+\\.)+\\S*"))) {
                        return true
                    }
                }
            }
            return false
        }

    //Android Api 23以上
    private val isAndroidMOrAbove: Boolean
        private get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    private const val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"
    private val isMiUIV6OrAbove: Boolean
        private get() = try {
            val properties = Properties()
            properties.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
            val uiCode: String =
                properties.getProperty(KEY_MIUI_VERSION_CODE, null)
            if (uiCode != null) {
                val code = uiCode.toInt()
                code >= 4
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }

    val isMiUIV7OrAbove: Boolean
        get() {
            return try {
                val properties = Properties()
                properties.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
                val uiCode: String =
                    properties.getProperty(KEY_MIUI_VERSION_CODE, null)
                if (uiCode != null) {
                    val code = uiCode.toInt()
                    code >= 5
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }

    internal object AvailableRomType {
        const val MIUI = 1
        const val FLYME = 2
        const val ANDROID_NATIVE = 3
        const val NA = 4
    }
}
