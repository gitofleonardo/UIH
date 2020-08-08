package cn.huangchengxi.uihlib.widget.recyclerview

import android.content.Context
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView

class FlowLayoutManager(private val context: Context):
    HLayoutManagerBase(context){
    private var currentTopOffset=0
    private var currentLeftOffset=0
    private var verticalOffset=0
    private var totalChildHeight=0
    private var singleItemHeightWithMarginAndPadding=0

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,RecyclerView.LayoutParams.WRAP_CONTENT)
    }
    override fun layoutChildren(recycler: RecyclerView.Recycler,state: RecyclerView.State){
        val screenWidth=width
        currentLeftOffset=0
        currentTopOffset=0
        totalChildHeight=0
        verticalOffset=0
        singleItemHeightWithMarginAndPadding=0

        for (i in 0 until state.itemCount){
            val view=recycler.getViewForPosition(i)
            addView(view)
            measureChildWithMargins(view,0,0)
            var width=getDecoratedMeasuredWidth(view)
            val height=getDecoratedMeasuredHeight(view)
            val top=view.marginTop
            val left=view.marginLeft
            val right=view.marginRight
            val bottom=view.marginBottom
            singleItemHeightWithMarginAndPadding=height+top+bottom

            if (width+left+right>screenWidth){
                width=screenWidth-left-right
            }
            if (currentTopOffset==0){
                currentTopOffset+=top
            }
            if (currentLeftOffset==0){
                currentLeftOffset=left
            }
            if (currentLeftOffset+width>= screenWidth){
                currentLeftOffset=left
                currentTopOffset+=height+top
            }
            layoutDecorated(view,currentLeftOffset,currentTopOffset,currentLeftOffset+width+right,currentTopOffset+height+bottom)
            currentLeftOffset+=width+left
        }
        //scrollVerticallyBy(verticalOffset,recycler, state)
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        var willScroll=dy
        totalChildHeight=currentTopOffset+singleItemHeightWithMarginAndPadding
        if (dy<0 && verticalOffset==0){
            willScroll=0
        }else if (dy>0 && verticalOffset>totalChildHeight-height){
            willScroll=0
        }else if (dy<0 && verticalOffset<0){
            willScroll=-verticalOffset
        }
        offsetChildrenVertical(-willScroll)
        verticalOffset+=willScroll
        return willScroll
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }
}