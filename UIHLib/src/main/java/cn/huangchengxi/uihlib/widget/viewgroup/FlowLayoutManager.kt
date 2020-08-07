package cn.huangchengxi.uihlib.widget.viewgroup

import android.content.Context
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView

class FlowLayoutManager(private val context: Context): RecyclerView.LayoutManager() {
    private var currentTopOffset=0
    private var currentLeftOffset=0

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        if (state==null || recycler==null) return
        if (state.itemCount==0){
            removeAndRecycleAllViews(recycler)
        }
        if (state.isPreLayout) return
        detachAndScrapAttachedViews(recycler)
        layoutChildren(recycler,state)
    }
    private fun layoutChildren(recycler: RecyclerView.Recycler,state: RecyclerView.State){
        val screenWidth=width
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
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }
}