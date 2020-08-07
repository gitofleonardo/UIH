package cn.huangchengxi.uihlib.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.text.SpannableStringBuilder
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.cardview.widget.CardView
import cn.huangchengxi.uihlib.R
import cn.huangchengxi.uihlib.system.dp2px

open class HialogBase(context: Context,themeResId:Int):Dialog(context,themeResId){
    companion object{
        val DEFAULT_HIALOG_THEME=R.style.HialogDefaultTheme
    }
    private val mWindowManager by lazy { window?.windowManager }
    private val decorView by lazy { window?.decorView }

    constructor(context: Context):this(context, DEFAULT_HIALOG_THEME)

    var dimAmount=0.2f

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateDimAmount()
    }
    private fun updateDimAmount() {
        val lp = if (decorView?.layoutParams==null) WindowManager.LayoutParams() else decorView?.layoutParams as WindowManager.LayoutParams
        lp.dimAmount = dimAmount
        lp.width=WindowManager.LayoutParams.WRAP_CONTENT
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            decorView?.setBackgroundColor(context.resources.getColor(R.color.transparent, null))
        } else {
            decorView?.setBackgroundColor(Color.TRANSPARENT)
        }
        mWindowManager?.updateViewLayout(decorView, lp)
    }
    class Builder(private val context: Context){
        private val hialog=HialogBase(context)
        private val cardRoot=CardView(context)
        private val cardContainer=LinearLayout(context)
        init {
            val lp=FrameLayout.LayoutParams(dp2px(context,300.0f),FrameLayout.LayoutParams.WRAP_CONTENT)
            cardRoot.layoutParams=lp
            cardRoot.radius= dp2px(context,10.0f).toFloat()
            cardRoot.setCardBackgroundColor(Color.WHITE)
            cardRoot.cardElevation=0.0f
            val conlp=LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            cardContainer.layoutParams=conlp
            cardContainer.orientation=LinearLayout.VERTICAL
            cardRoot.addView(cardContainer)
            hialog.setContentView(cardRoot)
        }
        fun addText(text:String):Builder{
            val tv=TextView(context)
            val lp=ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            val padding= dp2px(context,10.0f)
            tv.setPadding(padding,padding,padding,padding)
            tv.layoutParams=lp
            tv.text=SpannableStringBuilder(text)
            cardContainer.addView(tv)
            return this
        }
        fun addList(list:Array<String>,listener:((Int)->Unit)?):Builder{
            val listView=ListView(context)
            val lp=ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            listView.layoutParams=lp
            val adapter=ArrayAdapter<String>(context,android.R.layout.simple_list_item_1)
            adapter.addAll(list.asList())
            listView.adapter=adapter
            listView.setOnItemClickListener { _, _, position, _ ->
                listener?.invoke(position)
                hialog.dismiss()
            }
            cardContainer.addView(listView)
            return this
        }

        fun show():DialogInterface{
            hialog.show()
            return hialog
        }
    }
}