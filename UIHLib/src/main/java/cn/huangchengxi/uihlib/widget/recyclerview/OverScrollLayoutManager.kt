package cn.huangchengxi.uihlib.widget.recyclerview

import android.animation.ValueAnimator
import android.content.Context
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView

class OverScrollLayoutManager(private val context: Context):
    HLayoutManagerBase(context) {
    private var verticalScroll=0
    private var topOffset=0
    private var lastScrollDirection=0
    private var factor=0.3f
    private var animator:ValueAnimator?=null
    private var state:RecyclerView.State?=null
    private var recycler:RecyclerView.Recycler?=null

    override fun layoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        topOffset=0

        this.state=state
        this.recycler=recycler

        for (i in 0 until state.itemCount){
            val child=recycler.getViewForPosition(i)
            addView(child)
            measureChildWithMargins(child,0,0)
            val width=getDecoratedMeasuredWidth(child)
            val height=getDecoratedMeasuredHeight(child)
            val left=child.paddingLeft
            val right=child.paddingRight
            val top=child.paddingTop
            val bottom=child.paddingBottom
            layoutDecorated(child,left,topOffset+top,width+right,topOffset+height+bottom)
            topOffset+=height
        }

        scrollVerticallyBy(verticalScroll,recycler,state)
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        lastScrollDirection=if (dy<0) -1 else 1
        var willScroll=dy
        if (dy<0 && verticalScroll<=0){
            willScroll=(willScroll*factor).toInt()
        }else if (dy>0 && verticalScroll >= topOffset-height){
            willScroll=(willScroll*factor).toInt()
        }
        verticalScroll+=willScroll
        offsetChildrenVertical(-willScroll)
        return willScroll
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state==RecyclerView.SCROLL_STATE_SETTLING){
            //
        }else if (state==RecyclerView.SCROLL_STATE_DRAGGING){
            animator?.cancel()
        }else if (state==RecyclerView.SCROLL_STATE_IDLE){
            scrollToNormalPositionWithAnimation()
        }
    }
    private fun scrollToNormalPositionWithAnimation(){
        if (verticalScroll>0 && verticalScroll< topOffset-height) return
        val willScroll=if (verticalScroll<0) verticalScroll else (height-(topOffset-verticalScroll))
        var lastPos=0
        animator=ValueAnimator.ofInt(0,willScroll)
        animator!!.duration=200
        animator!!.addUpdateListener {
            val movement=it.animatedValue as Int - lastPos
            lastPos=it.animatedValue as Int
            offsetChildrenVertical(movement)
            verticalScroll-=movement
        }
        animator!!.interpolator=LinearInterpolator()
        animator!!.start()
    }
    override fun canScrollHorizontally(): Boolean {
        return false
    }

    override fun canScrollVertically(): Boolean {
        return true
    }
}