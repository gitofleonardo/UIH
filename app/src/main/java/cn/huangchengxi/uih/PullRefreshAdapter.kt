package cn.huangchengxi.uih

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PullRefreshAdapter(private val context: Context,private val list:ArrayList<PullItem>):RecyclerView.Adapter<PullRefreshAdapter.PullViewHolder>() {
    class PullViewHolder(val view:View):RecyclerView.ViewHolder(view){
        val name=view.findViewById<TextView>(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PullViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.view_pull_item,parent,false)
        return PullViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PullViewHolder, position: Int) {
        val item=list[position]
        holder.name.text=SpannableStringBuilder(item.name)
        holder.view.setOnClickListener {
            Log.e("click","$position")
        }
    }
    data class PullItem(val name:String)
}