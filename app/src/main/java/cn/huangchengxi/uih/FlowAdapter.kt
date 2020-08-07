package cn.huangchengxi.uih

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FlowAdapter(private val context:Context,private val items:ArrayList<FlowItem>): RecyclerView.Adapter<FlowAdapter.FlowHolder>() {
    class FlowHolder(view:View): RecyclerView.ViewHolder(view) {
        val name by lazy { view.findViewById<TextView>(R.id.name) }
        val contaienr by lazy { view.findViewById<ViewGroup>(R.id.container) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowHolder {
        return FlowHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_flow_item_test,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FlowHolder, position: Int) {
        val item=items[position]
        holder.name.text=SpannableStringBuilder(item.name)
        holder.contaienr.setOnClickListener {
            Log.e("click","$position")
        }
    }
    data class FlowItem(val name:String)
}