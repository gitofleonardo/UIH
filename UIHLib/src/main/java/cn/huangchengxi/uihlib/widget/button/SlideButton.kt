package cn.huangchengxi.uihlib.widget.button

import android.animation.ValueAnimator
import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.uihlib.R
import cn.huangchengxi.uihlib.system.dp2px
import kotlin.math.abs

class SlideButton(context: Context,attrs:AttributeSet?):FrameLayout(context,attrs) {
    private val MIN_VELOCITY=200
    private var contentView:View?=null
    private var contentContainer:ViewGroup?=null
    private var buttonsContainer:ViewGroup?=null
    private var childParent:ViewGroup?=null
    private var startX=0.0f
    private var offsetScroll=0.0f
    private var velocityTracker=VelocityTracker.obtain()
    private var animator:ValueAnimator?=null
    private var interceptedEvent=true
    private var isOpen=false
    private var trackV=false

    private var defaultStrategy=DefaultStrategyImp() as ToggleStrategy

    init {
        contentView= View.inflate(context,R.layout.widget_slide_btn,this)
        contentContainer=findViewById(R.id.content)
        buttonsContainer=findViewById(R.id.btns)
        childParent=findViewById(R.id.childParent)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event==null) return false
        val x=event.x
        val btnWidth=buttonsContainer!!.width
        velocityTracker.addMovement(event)

        when (event.action){
            MotionEvent.ACTION_DOWN->{
                startX=x
                animator?.cancel()
                interceptedEvent=false
            }
            MotionEvent.ACTION_MOVE->{
                if (!interceptedEvent){
                    velocityTracker.computeCurrentVelocity(1000)
                    val vx= abs(velocityTracker.xVelocity)
                    val vy= abs(velocityTracker.yVelocity)
                    //Log.e("x y","$vx $vy")
                    if (vx>vy && vx>=MIN_VELOCITY){
                        trackV=true
                        interceptedEvent=true
                    }
                }
                if (interceptedEvent){
                    //for recyclerview or other scrolling view
                    disallowViewGroupScrolling()
                }
                val dist=startX-x
                scrollWithinDist(dist)
                startX=x

                event.action=MotionEvent.ACTION_CANCEL
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{
                val velocity=velocityTracker.xVelocity

                if (defaultStrategy.canCloseButton(offsetScroll.toInt(),btnWidth,velocity.toInt())){
                    closeButton()
                }else if (defaultStrategy.canOpenButtons(offsetScroll.toInt(),btnWidth,velocity.toInt())){
                    openButton()
                }else{
                    if (defaultStrategy.defaultStrategy()==ToggleStrategy.Strategy.OPEN) openButton()
                    else closeButton()
                }
                interceptedEvent=false
            }
        }
        if (event.action==MotionEvent.ACTION_UP || event.action==MotionEvent.ACTION_CANCEL){
            super.onTouchEvent(event)
        }
        return true
    }

    private fun disallowViewGroupScrolling(){
        var viewParent=parent
        val decor=rootView
        while (viewParent !=decor){
            if (checkIfIsScrollingView(viewParent as ViewGroup)){
                viewParent.requestDisallowInterceptTouchEvent(true)
                break
            }
            viewParent=viewParent.parent
        }
    }
    private fun checkIfIsScrollingView(viewGroup: ViewGroup):Boolean{
        if (viewGroup is RecyclerView ||
                viewGroup is ScrollView ||
                viewGroup is NestedScrollView ||
                viewGroup is HorizontalScrollView){
            return true
        }
        return false
    }
    private fun scrollWithinDist(dist:Float){
        var willScroll=dist
        val btnWidth=buttonsContainer!!.width

        if (willScroll+offsetScroll>btnWidth){
            willScroll=btnWidth-offsetScroll
        }else if (willScroll<0 && offsetScroll<=0.0f){
            willScroll=-offsetScroll
        }
        contentContainer!!.scrollBy(willScroll.toInt(),0)
        offsetScroll+=willScroll.toInt()
    }
    fun openButton(){
        scrollOpen()
    }
    fun closeButton(){
        scrollClose()
    }
    private fun scrollOpen(){
        val btnWidth=buttonsContainer!!.width
        val dist=btnWidth-offsetScroll.toInt()
        var lastDist=0

        animator=ValueAnimator.ofInt(0,dist)
        animator!!.duration=200
        animator!!.interpolator=LinearInterpolator()
        animator!!.addUpdateListener {
            val willScroll=it.animatedValue as Int - lastDist
            lastDist=it.animatedValue as Int
            contentContainer!!.scrollBy(willScroll,0)
            offsetScroll+=willScroll
            if (it.animatedFraction>=1.0f){
                isOpen=true
            }
        }
        animator!!.start()
    }
    private fun scrollClose(){
        val dist=-offsetScroll.toInt()
        var lastDist=0

        animator=ValueAnimator.ofInt(0,dist)
        animator!!.duration=200
        animator!!.interpolator=LinearInterpolator()
        animator!!.addUpdateListener {
            val willScroll=it.animatedValue as Int - lastDist
            lastDist=it.animatedValue as Int
            contentContainer!!.scrollBy(willScroll,0)
            offsetScroll+=willScroll
            if (it.animatedFraction>=1.0f){
                isOpen=false
            }
        }
        animator!!.start()
    }
    fun isButtonsOpened():Boolean{
        return isOpen
    }
    fun setContentView(view:View){
        childParent!!.addView(view)
    }

    override fun addView(child: View?) {
        childParent!!.addView(child)
    }

    override fun addView(child: View?, index: Int) {
        childParent!!.addView(child, index)
    }

    fun addButton(title:String,color: Int,listener: OnClickListener){
        val btn=TextView(context)
        val lp=ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT)
        btn.text=SpannableStringBuilder(title)
        btn.setOnClickListener(listener)
        btn.gravity=Gravity.CENTER
        val padding=dp2px(context,10.0f)
        btn.setPadding(padding,padding,padding,padding)
        btn.setBackgroundColor(color)
        buttonsContainer!!.addView(btn,lp)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev!=null){
            val action=ev.action
            if (action==MotionEvent.ACTION_DOWN){
                trackV=false
                super.dispatchTouchEvent(ev)
                onTouchEvent(ev)
                return true
            }
            if (action==MotionEvent.ACTION_UP || action==MotionEvent.ACTION_CANCEL){
                if (trackV){
                    return onTouchEvent(ev)
                }
                onTouchEvent(ev)
                return super.dispatchTouchEvent(ev)
            }
            return onTouchEvent(ev)
        }
        return super.dispatchTouchEvent(ev)
    }
    override fun canScrollHorizontally(direction: Int): Boolean {
        return false
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return false
    }

    fun setToggleStrategy(strategy:ToggleStrategy){
        this.defaultStrategy=strategy
    }
    interface ToggleStrategy{
        enum class Strategy{
            OPEN,
            CLOSE
        }
        fun canOpenButtons(offset:Int,btnWidth:Int,velocity:Int):Boolean
        fun canCloseButton(offset: Int,btnWidth: Int,velocity: Int):Boolean
        fun defaultStrategy():Strategy
    }
    private class DefaultStrategyImp:ToggleStrategy{
        override fun canOpenButtons(offset: Int, btnWidth: Int, velocity: Int): Boolean {
            if (offset>btnWidth/2){
                return true
            }
            return false
        }
        override fun canCloseButton(offset: Int, btnWidth: Int, velocity: Int): Boolean {
            if (offset<btnWidth/2){
                return true
            }
            return false
        }

        override fun defaultStrategy(): ToggleStrategy.Strategy {
            return ToggleStrategy.Strategy.CLOSE
        }
    }
}