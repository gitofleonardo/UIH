package cn.huangchengxi.uihlib.widget.recyclerview

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

abstract class HLayoutManagerBase(private val context: Context): RecyclerView.LayoutManager() {
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
    protected abstract fun layoutChildren(recycler: RecyclerView.Recycler,state: RecyclerView.State)
}