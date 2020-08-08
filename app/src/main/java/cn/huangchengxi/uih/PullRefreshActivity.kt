package cn.huangchengxi.uih

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.uihlib.activity.HBaseActivity
import cn.huangchengxi.uihlib.widget.recyclerview.OverScrollLayoutManager

class PullRefreshActivity : HBaseActivity(){
    private val pullItems=ArrayList<PullRefreshAdapter.PullItem>()
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerview) }
    private val adapter by lazy { PullRefreshAdapter(this,pullItems) }
    private val layoutManager by lazy {
        OverScrollLayoutManager(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_refresh)
        init()
    }
    private fun init(){
        for (i in 0 until 20){
            pullItems.add(PullRefreshAdapter.PullItem("Hello World $i"))
        }
        recyclerView.adapter=adapter
        recyclerView.layoutManager=layoutManager
    }
}