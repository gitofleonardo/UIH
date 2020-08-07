package cn.huangchengxi.uihlib.widget.popmenu

import android.content.Context
import android.content.res.ColorStateList
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import cn.huangchengxi.uihlib.R

open class HNormalPopMenu(private val context: Context):
    HPopupMenuBase(context) {
    private var view=View.inflate(context,
        R.layout.widget_h_normal_menu,null) as FrameLayout
    private var content=view.findViewById(R.id.normalContent) as FrameLayout
    private var titleView=view.findViewById<TextView>(R.id.titleView)

    private var childView:View?=null
    private var showTitle=true
    private var titleText=""
    var titleColor:ColorStateList?=null

    init {
        setContentView(view)
    }
    fun setContentNormalView(view:View){
        this.childView=view
    }
    override fun showAttachToView(v: View) {
        adjustViewByGravity()
        setAllProperties()
        super.showAttachToView(v)
    }
    fun setTitleVisible(v:Boolean){
        showTitle=v
        if (showTitle){
            titleView.visibility=View.VISIBLE
        }else{
            titleView.visibility=View.GONE
        }
    }
    fun setTitle(title:String){
        titleText=title
    }
    private fun setAllProperties(){
        if (showTitle){
            titleView.visibility=View.VISIBLE
        }else{
            titleView.visibility=View.GONE
        }
        titleView.text=SpannableStringBuilder(titleText)
        if (titleColor!=null)
            titleView.setTextColor(titleColor)
    }
    private fun adjustViewByGravity(){
        when (gravity){
            Gravity.BOTTOM->{
                setContentLayout(R.layout.widget_h_normal_menu_bottom)
            }
            Gravity.TOP->{
                setContentLayout(R.layout.widget_h_normal_menu_top)
            }
            Gravity.LEFT->{
                setContentLayout(R.layout.widget_h_normal_menu_left)
            }
            Gravity.RIGHT->{
                setContentLayout(R.layout.widget_h_normal_menu_right)
            }
            else->{
                setContentLayout(R.layout.widget_h_normal_menu)
            }
        }
    }
    private fun setContentLayout(@LayoutRes resId:Int){
        content.removeAllViews()
        view=View.inflate(context,resId,null) as FrameLayout
        content=view.findViewById(R.id.normalContent) as FrameLayout
        titleView=view.findViewById(R.id.titleView) as TextView
        content.removeAllViews()
        content.addView(childView)
        setContentView(view)
    }
}