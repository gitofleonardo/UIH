package cn.huangchengxi.uihlib.widget.recyclerview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.VelocityTracker
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
/**
 * not done yet
 * still remains problems
 */
class PageLayoutManager(private val context: Context, private val orientation:Int):HLayoutManagerBase(context) {
    companion object{
        const val VERTICAL=LinearLayout.VERTICAL
        const val HORIZONTAL=LinearLayout.HORIZONTAL
    }
    private var scrollBy=0
    private var startOffset=0
    private val props=ArrayList<ViewProperty>()
    private var currentPosition=0
    private var isScrolling=false
    private var animator:ValueAnimator?=null
    private var tracker:VelocityTracker= VelocityTracker.obtain()
    private var offsetStartDrag=0
    private var offsetEndDrag=0

    override fun layoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        clearParams()
        if (orientation== VERTICAL){
            layoutVertically(recycler, state)
        }else if (orientation== HORIZONTAL){
            layoutHorizontally(recycler, state)
        }else{
            Log.w("SwitchLayoutManager","Wrong orientation")
        }
    }
    private fun clearParams(){
        props.clear()
        startOffset=0
    }
    private fun layoutVertically(recycler: RecyclerView.Recycler,state: RecyclerView.State){
        val parentWidth=width
        val parentHeight=height
        for (i in 0 until state.itemCount){
            val child=recycler.getViewForPosition(i)
            addView(child)
            measureChild(child,0,0)
            val width=getDecoratedMeasuredWidth(child)
            val height=getDecoratedMeasuredHeight(child)
            val top=child.marginTop
            val left=child.marginLeft
            val right=child.marginRight
            val bottom=child.marginBottom

            val firstChild= i==0

            val layoutTop=if (firstChild) parentHeight/2-(height+top+bottom)/2 else startOffset+top
            val layoutLeft=parentWidth/2-(width+left+right)/2
            val layoutRight=parentWidth/2+(width+left+right)/2
            val layoutBottom=if (firstChild) parentHeight/2+(height+top+bottom)/2 else startOffset+height+bottom

            layoutDecorated(child,layoutLeft,layoutTop,layoutRight,layoutBottom)
            val rect=Rect(layoutLeft,layoutTop,layoutRight,layoutBottom)
            val size=Size(width,height)
            val prop=ViewProperty(rect, size)
            props.add(prop)

            startOffset+=if(firstChild) layoutBottom else height+bottom
        }
        scrollToPosition(0)
    }
    private fun layoutHorizontally(recycler: RecyclerView.Recycler,state: RecyclerView.State){

    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        if (isScrolling) return 0
        var willScroll=dy
        val lastViewHeight=props[props.size-1].size.height
        if (willScroll<0 && scrollBy<=0){
            willScroll=-scrollBy
        }else if (willScroll>0 && scrollBy>=startOffset-height/2-lastViewHeight/2){
            willScroll=(startOffset-height/2-lastViewHeight/2)-scrollBy
        }
        offsetChildrenVertical(-willScroll)
        scrollBy+=willScroll
        return willScroll
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        when (state){
            RecyclerView.SCROLL_STATE_IDLE->{

            }
            RecyclerView.SCROLL_STATE_DRAGGING->{
                offsetStartDrag=scrollBy
            }
            RecyclerView.SCROLL_STATE_SETTLING->{
                offsetEndDrag=scrollBy
                val position=findNearestPosition()
                scrollToPosition(position)
            }
        }
    }
    private fun findNearestPosition():Int{
        val dist=offsetEndDrag-offsetStartDrag
        if (dist>0 && dist>0.3*props[currentPosition].size.height){
            return if (currentPosition+1>itemCount-1) currentPosition else currentPosition+1
        }else if (dist<0 && abs(dist)>0.3*props[currentPosition].size.height){
            return if (currentPosition-1<0) currentPosition else currentPosition-1
        }
        return 0
    }
    override fun scrollToPosition(position: Int) {
        if (isScrolling) return
        isScrolling=true
        val prop=props[currentPosition]
        /*
        val currentProp=props[currentPosition]
        val topDist=currentProp.rect.top-prop.rect.top
        val willScroll=topDist-(currentProp.size.height-prop.size.height)/2
         */
        val willScroll=prop.size.height-(offsetEndDrag-offsetStartDrag)

        var lastValue=0.0f
        animator=ValueAnimator.ofFloat(0.0f,willScroll.toFloat())
        animator!!.duration=200
        animator!!.interpolator=LinearInterpolator()
        animator!!.addUpdateListener {
            val scroll=it.animatedValue as Float - lastValue
            offsetChildrenVertical(scroll.toInt())
            lastValue=it.animatedValue as Float
            scrollBy+=scroll.toInt()
            if (it.animatedFraction>=1.0f){
                currentPosition=position
                isScrolling=false
            }
        }
        animator!!.start()
    }

    override fun canScrollVertically(): Boolean {
        return orientation== VERTICAL
    }

    override fun canScrollHorizontally(): Boolean {
        return orientation== HORIZONTAL
    }
    private data class ViewProperty(val rect: Rect,val size: Size)
    private data class Size(val width:Int,val height:Int)
}