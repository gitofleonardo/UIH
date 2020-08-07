package cn.huangchengxi.uihlib.activity

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import cn.huangchengxi.uihlib.system.RomUtils
import cn.huangchengxi.uihlib.system.RomUtils.lightStatusBarAvailableRomType
import java.lang.reflect.Field
import java.lang.reflect.Method


open class HBaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        fitWindowWhenUseNativeLight()
    }
    private fun fitWindowWhenUseNativeLight(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val content =
                (findViewById<View>(R.id.content) as ViewGroup).getChildAt(0)
            if (content != null && !isUseFullScreenMode()) {
                content.fitsSystemWindows = true
            }
        }
    }
    private fun isUseFullScreenMode():Boolean{
        val decor=window.decorView
        return decor.systemUiVisibility.and(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)!=0
    }
    protected fun fullScreenActivity(){
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
    protected fun setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT){
            return
        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.statusBarColor=Color.TRANSPARENT
        }else{
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
    protected fun setStatusColor(@ColorInt color:Int,@IntRange(from = 0,to = 255) statusBarAlpha:Int){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor=calculateStatusColor(color,statusBarAlpha)
        }else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val decorView=window.decorView as ViewGroup
            val fake=decorView.findViewById<View>(getFakeStatusBarId())
            if (fake!=null){
                if (fake.visibility==View.GONE){
                    fake.visibility=View.VISIBLE
                }
                fake.setBackgroundColor(calculateStatusColor(color,statusBarAlpha))
            }else{
                decorView.addView(createStatusBarView(this,color,statusBarAlpha))
            }
        }
    }
    protected fun fitToolbarToWindow(toolbar: Toolbar){
        val lp=toolbar.layoutParams
        lp.height+=getStatusBarHeight(this)
        toolbar.setPadding(toolbar.paddingLeft,getStatusBarHeight(this),toolbar.paddingRight,toolbar.paddingBottom)
        toolbar.layoutParams=lp
    }
    protected fun setDarkStatusBar(dark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            when (lightStatusBarAvailableRomType) {
                RomUtils.AvailableRomType.MIUI -> MIUISetStatusBarLightMode(this, dark)
                RomUtils.AvailableRomType.FLYME -> setFlymeLightStatusBar(this, dark)
                RomUtils.AvailableRomType.ANDROID_NATIVE -> if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) setNativeStatusBarColor(dark)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setNativeStatusBarColor(dark: Boolean){
        val decor=window.decorView
        if (dark){
            decor.systemUiVisibility=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }else{
            decor.systemUiVisibility=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
    @SuppressLint("PrivateApi")
    private fun MIUISetStatusBarLightMode(activity: Activity, dark: Boolean): Boolean {
        var result = false
        val window: Window? = activity.window
        if (window != null) {
            val clazz: Class<*> = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams =
                    Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field: Field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField: Method = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag) //状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag) //清除黑色字体
                }
                result = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && RomUtils.isMiUIV7OrAbove) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        activity.window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    }
                }
            } catch (e: Exception) {
            }
        }
        return result
    }
    private fun setFlymeLightStatusBar(
        activity: Activity?,
        dark: Boolean
    ): Boolean {
        var result = false
        if (activity != null) {
            try {
                val lp = activity.window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                activity.window.attributes = lp
                result = true
            } catch (e: java.lang.Exception) {
            }
        }
        return result
    }
    protected fun getFakeStatusBarId():Int{
        return 0
    }
    private fun createStatusBarView(
        activity: Activity,
        @ColorInt color: Int,
        alpha: Int
    ): View? {
        val statusBarView = View(activity)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(activity)
        )
        statusBarView.layoutParams = params
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha))
        statusBarView.id = getFakeStatusBarId()
        return statusBarView
    }
    private fun calculateStatusColor(@ColorInt color: Int, alpha: Int): Int {
        if (alpha == 0) {
            return color
        }
        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }
    open fun getStatusBarHeight(context: Context): Int {
        val resourceId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }
}