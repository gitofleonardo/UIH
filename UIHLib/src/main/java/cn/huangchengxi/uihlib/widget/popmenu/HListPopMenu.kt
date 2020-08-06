package cn.huangchengxi.uihlib.widget.popmenu

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.uihlib.R
import cn.huangchengxi.uihlib.system.dp2px

class HListPopMenu(private val context: Context,private val items:ArrayList<HPopListItem>):HNormalPopMenu(context) {
    var orientation=LinearLayoutManager.VERTICAL
    var dismissOnItemSelected=true
    private val adapter=HPopListAdapter(items,orientation)
    private val layoutManager=LinearLayoutManager(context,orientation,false)
    private val recyclerView=RecyclerView(context)
    private val rootView=FrameLayout(context)
    private var onItemClickListener:((HPopListItem)->Unit)?=null

    init {
        val rvlp=RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.MATCH_PARENT)
        recyclerView.layoutParams=rvlp
        recyclerView.overScrollMode=RecyclerView.OVER_SCROLL_NEVER
        val rootlp=FrameLayout.LayoutParams(dp2px(context,200.0f),FrameLayout.LayoutParams.WRAP_CONTENT)
        rootView.layoutParams=rootlp
        rootView.addView(recyclerView)
        setContentNormalView(rootView)

        adapter.setOnItemClick {
            if (dismissOnItemSelected){
                dismiss()
            }
            onItemClickListener?.invoke(items[it])
        }
        recyclerView.adapter=adapter
        recyclerView.layoutManager=layoutManager
    }
    fun addItem(item:HPopListItem){
        items.add(item)
        adapter.notifyItemInserted(items.size-1)
    }
    fun addAll(items:List<HPopListItem>){
        val currentSize=this.items.size
        this.items.addAll(items)
        adapter.notifyItemRangeInserted(currentSize,items.size)
    }

    class HPopListAdapter(private val list:List<HPopListItem>,private val orientation:Int): RecyclerView.Adapter<PopViewHolder>() {
        private var onItemClickListener:((Int)->Unit)?=null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopViewHolder {
            return if (orientation==LinearLayout.VERTICAL){
                val view=LayoutInflater.from(parent.context).inflate(R.layout.view_item_vertical_pop_list,parent,false)
                PopViewHolder(view)
            }else{
                val view=LayoutInflater.from(parent.context).inflate(R.layout.view_item_horizontal_pop_list,parent,false)
                PopViewHolder(view)
            }
        }
        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: PopViewHolder, position: Int) {
            val item=list[position]
            if (!item.showIcon){
                holder.icon.visibility=View.GONE
            }else{
                holder.icon.visibility=View.VISIBLE
                holder.icon.setImageResource(item.iconResource)
            }
            holder.name.text=SpannableStringBuilder(item.name)
            holder.container.setOnClickListener {
                onItemClickListener?.invoke(position)
            }
        }
        fun setOnItemClick(listener:((Int)->Unit)){
            this.onItemClickListener=listener
        }
    }
    class PopViewHolder(view:View): RecyclerView.ViewHolder(view) {
        val icon=view.findViewById<ImageView>(R.id.icon)
        val name=view.findViewById<TextView>(R.id.name)
        val container=view.findViewById<LinearLayout>(R.id.container)
    }
    data class HPopListItem(var showIcon:Boolean,var iconResource:Int,var name:String)
}