package cn.huangchengxi.uih

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.uihlib.widget.button.SlideButton

class SlideAdapter(private val context:Context,private val items:ArrayList<SlideItem>):RecyclerView.Adapter<SlideAdapter.SlideHolder>() {
    class SlideHolder(view:View):RecyclerView.ViewHolder(view){
        val slide by lazy {
            val v=view.findViewById<SlideButton>(R.id.slideBtn)
            v.addButton("Delete", Color.RED, View.OnClickListener {
                Toast.makeText(v.context, "Delete", Toast.LENGTH_SHORT).show()
            })
            v.addButton("Top", Color.YELLOW, View.OnClickListener {
                Toast.makeText(v.context, "Top", Toast.LENGTH_SHORT).show()
            })
            v
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideHolder {
        return SlideHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_slide_item,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SlideHolder, position: Int) {
        holder.slide.isButtonsOpened()
    }
    data class SlideItem(val name:String)
}