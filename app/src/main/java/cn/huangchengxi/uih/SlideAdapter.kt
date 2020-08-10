package cn.huangchengxi.uih

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.uihlib.widget.button.SlideButton

class SlideAdapter(private val context:Context,private val items:ArrayList<SlideItem>):RecyclerView.Adapter<SlideAdapter.SlideHolder>() {
    class SlideHolder(view:View):RecyclerView.ViewHolder(view){
        val slide by lazy { view.findViewById<SlideButton>(R.id.slideBtn) }
        //val title by lazy { view.findViewById<TextView>(R.id.title) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideHolder {
        return SlideHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_slide_item,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SlideHolder, position: Int) {
        /*
        holder.title.setOnClickListener {
            Toast.makeText(context,"Click $position",Toast.LENGTH_SHORT).show()
        }

         */
    }
    data class SlideItem(val name:String)
}