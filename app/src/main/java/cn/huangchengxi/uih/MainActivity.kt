package cn.huangchengxi.uih

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.uihlib.activity.HBaseActivity
import cn.huangchengxi.uihlib.widget.dialog.HialogBase
import cn.huangchengxi.uihlib.widget.popmenu.HListPopMenu
import cn.huangchengxi.uihlib.widget.popmenu.HNormalPopMenu
import cn.huangchengxi.uihlib.widget.recyclerview.FlowLayoutManager

class MainActivity : HBaseActivity(){
    private val top by lazy { findViewById<Button>(R.id.top) }
    private val bottom by lazy { findViewById<Button>(R.id.bottom) }
    private val left by lazy { findViewById<Button>(R.id.left) }
    private val right by lazy { findViewById<Button>(R.id.right) }
    private val list by lazy { findViewById<Button>(R.id.menus) }
    private val dialogBase by lazy { findViewById<Button>(R.id.baseDialog) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val pullBtn by lazy { findViewById<Button>(R.id.pullBtn) }

    private val items=ArrayList<HListPopMenu.HPopListItem>()
    private val listMenu by lazy {HListPopMenu(this)}
    private val flowItems=ArrayList<FlowAdapter.FlowItem>()
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerview) }
    private val adapter=FlowAdapter(this,flowItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setStatusBarTransparent()
        fitToolbarToWindow(toolbar)
        setDarkStatusBar(false)
        flowItems.add(FlowAdapter.FlowItem("1dasdasdsssssssssssssssssssssssssssssssssssssssssssssssssssssssssdasdsdsadasdsa"))
        flowItems.add(FlowAdapter.FlowItem("1dsadas"))
        flowItems.add(FlowAdapter.FlowItem("1ds"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasd"))
        flowItems.add(FlowAdapter.FlowItem("1dsadasd"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasdsa"))
        flowItems.add(FlowAdapter.FlowItem("1dsadas"))
        flowItems.add(FlowAdapter.FlowItem("1ds"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasd"))
        flowItems.add(FlowAdapter.FlowItem("1dsadasd"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasdsa"))
        flowItems.add(FlowAdapter.FlowItem("1dsadas"))
        flowItems.add(FlowAdapter.FlowItem("1ds"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasd"))
        flowItems.add(FlowAdapter.FlowItem("1dsadasd"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasdsssssssssssssssssssssssssssssssssssssssssssssssssssssssssdasdsdsadasdsa"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasdsssssssssssssssssssssssssssssssssssssssssssssssssssssssssdasdsdsadasdsa"))
        flowItems.add(FlowAdapter.FlowItem("1dsadas"))
        flowItems.add(FlowAdapter.FlowItem("1ds"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasd"))
        flowItems.add(FlowAdapter.FlowItem("1dsadasd"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasdsa"))
        flowItems.add(FlowAdapter.FlowItem("1dsadas"))
        flowItems.add(FlowAdapter.FlowItem("1ds"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasd"))
        flowItems.add(FlowAdapter.FlowItem("1dsadasd"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasdsa"))
        flowItems.add(FlowAdapter.FlowItem("1dsadas"))
        flowItems.add(FlowAdapter.FlowItem("1ds"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasd"))
        flowItems.add(FlowAdapter.FlowItem("1dsadasd"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasdsssssssssssssssssssssssssssssssssssssssssssssssssssssssssdasdsdsadasdsa"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasdsssssssssssssssssssssssssssssssssssssssssssssssssssssssssdasdsdsadasdsa"))
        flowItems.add(FlowAdapter.FlowItem("1dsadas"))
        flowItems.add(FlowAdapter.FlowItem("1ds"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasd"))
        flowItems.add(FlowAdapter.FlowItem("1dsadasd"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasdsa"))
        flowItems.add(FlowAdapter.FlowItem("1dsadas"))
        flowItems.add(FlowAdapter.FlowItem("1ds"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasd"))
        flowItems.add(FlowAdapter.FlowItem("1dsadasd"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasdsa"))
        flowItems.add(FlowAdapter.FlowItem("1dsadas"))
        flowItems.add(FlowAdapter.FlowItem("1ds"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasd"))
        flowItems.add(FlowAdapter.FlowItem("1dsadasd"))
        flowItems.add(FlowAdapter.FlowItem("1dasdasdsssssssssssssssssssssssssssssssssssssssssssssssssssssssssdasdsdsadasdsa"))

        recyclerView.layoutManager=
            FlowLayoutManager(this)
        recyclerView.adapter=adapter
        adapter.notifyDataSetChanged()
        top.setOnClickListener {
            val menu= HNormalPopMenu(this)
            menu.gravity=Gravity.TOP
            val view=View.inflate(this,R.layout.view_test_text,null)
            menu.setContentNormalView(view)
            menu.setTitle("Top")
            menu.showAttachToView(it)
        }
        bottom.setOnClickListener {
            val menu= HNormalPopMenu(this)
            menu.gravity=Gravity.BOTTOM
            val view=View.inflate(this,R.layout.view_test_text,null)
            menu.setContentNormalView(view)
            menu.setTitle("Bottom")
            menu.showAttachToView(it)
        }
        left.setOnClickListener {
            val menu= HNormalPopMenu(this)
            menu.gravity=Gravity.LEFT
            val view=View.inflate(this,R.layout.view_test_text,null)
            menu.setContentNormalView(view)
            menu.setTitle("Left")
            menu.showAttachToView(it)
        }
        right.setOnClickListener {
            val menu= HNormalPopMenu(this)
            menu.gravity=Gravity.RIGHT
            val view=View.inflate(this,R.layout.view_test_text,null)
            menu.setContentNormalView(view)
            menu.setTitleVisible(false)
            menu.setTitle("Right")
            menu.showAttachToView(it)
        }
        pullBtn.setOnClickListener {
            startActivity(Intent(this,PullRefreshActivity::class.java))
        }
        list.setOnClickListener {
            listMenu.removeAll()
            listMenu.addItem(HListPopMenu.HPopListItem(true,R.drawable.archlinux,"Archlinux",0))
            listMenu.addItem(HListPopMenu.HPopListItem(true,R.drawable.archlinux,"Archlinux",1))
            listMenu.addItem(HListPopMenu.HPopListItem(true,R.drawable.archlinux,"Archlinux",2))
            listMenu.addItem(HListPopMenu.HPopListItem(true,R.drawable.archlinux,"Archlinux",3))
            listMenu.addItem(HListPopMenu.HPopListItem(true,R.drawable.archlinux,"Archlinux",4))
            listMenu.addItem(HListPopMenu.HPopListItem(true,R.drawable.archlinux,"Archlinux",5))
            listMenu.setTitle("Archlinux")
            listMenu.dismissOnItemSelected=true
            listMenu.gravity=Gravity.TOP
            listMenu.setTitleVisible(false)
            listMenu.setOnItemClickListener {
                Toast.makeText(this,"${it.id}",Toast.LENGTH_SHORT).show()
            }
            listMenu.showAttachToView(it)
        }
        dialogBase.setOnClickListener {
            val dialog=HialogBase.Builder(this).addText("This is a simple dialog")
                .addList(arrayOf("hello","world","fuck","you")) {
                    Toast.makeText(this,"$it",Toast.LENGTH_SHORT).show()
                }.show()
        }
    }
}