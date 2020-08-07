package cn.huangchengxi.uihlib.widget.popmenu

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import cn.huangchengxi.uihlib.R
import java.lang.ref.WeakReference
import kotlin.math.abs

open class HPopupMenuBase(private val context: Context):
    HPopMenu {
    companion object{
        private const val DEFAULT_DIM_AMOUNT=0.5F
        private val DEFAULT_ANIMATION_STYLE=
            R.style.PopupAnimationStyle
    }
    private val windowManager by lazy { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager}
    protected val popupWindow by lazy { PopupWindow(context) }
    private var attachView:WeakReference<View>?=null
    private var dismissWhenTouchOutside=true
    private var dismissListener:(()->Unit)?=null
    var animStyle:Int=
        DEFAULT_ANIMATION_STYLE
    var gravity:Int=Gravity.NO_GRAVITY
    var dimAmount:Float=
        DEFAULT_DIM_AMOUNT
        set(value) {
            field=if (value>1.0f) 1.0f else if (value<0.0f) 0.0f else value
        }

    init {
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.isTouchable=true
        popupWindow.isFocusable=true
        popupWindow.setOnDismissListener {
            dismissListener?.invoke()
        }
        popupWindow.animationStyle=animStyle
    }
    protected fun setContentView(view:View){
        popupWindow.contentView=view
    }
    fun setOnDismissListener(listener:(()->Unit)?){
        this.dismissListener=listener
        popupWindow.setOnDismissListener {
            this.dismissListener?.invoke()
        }
    }
    override fun showAttachToView(v: View) {
        if (!popupWindow.isShowing){
            popupWindow.animationStyle=animStyle

            val view=popupWindow.contentView.rootView
            view.measure(0,0)
            val width=view.measuredWidth
            val height=view.measuredHeight
            when (gravity){
                Gravity.TOP->{
                    popupWindow.showAsDropDown(v,-abs(width-v.width) /2,-height-v.height)
                }
                Gravity.BOTTOM->{
                    popupWindow.showAsDropDown(v,-abs(width-v.width)/2,0)
                }
                Gravity.LEFT->{
                    popupWindow.showAsDropDown(v,-width,-height+abs(height-v.height)/2)
                }
                Gravity.RIGHT->{
                    popupWindow.showAsDropDown(v,v.width,-v.height- abs(height-v.height)/2)
                }
                else->{
                    popupWindow.showAsDropDown(v)
                }
            }
            updateDimAmount()
        }
    }
    private fun updateDimAmount(){
        val decorView=popupWindow.contentView.rootView
        val lp =decorView.layoutParams as WindowManager.LayoutParams
        lp.flags=lp.flags.or(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        lp.dimAmount=dimAmount
        windowManager.updateViewLayout(decorView,lp)
    }

    override fun dismiss() {
        popupWindow.dismiss()
    }
}