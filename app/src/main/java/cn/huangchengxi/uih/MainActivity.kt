package cn.huangchengxi.uih

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.*
import android.widget.*
import cn.huangchengxi.uihlib.widget.popmenu.HListPopMenu
import cn.huangchengxi.uihlib.widget.popmenu.HNormalPopMenu
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val top by lazy { findViewById<Button>(R.id.top) }
    private val bottom by lazy { findViewById<Button>(R.id.bottom) }
    private val left by lazy { findViewById<Button>(R.id.left) }
    private val right by lazy { findViewById<Button>(R.id.right) }
    private val list by lazy { findViewById<Button>(R.id.menus) }

    private val items=ArrayList<HListPopMenu.HPopListItem>()
    private val listMenu by lazy {HListPopMenu(this,items)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
            menu.setTitle("Right")
            menu.showAttachToView(it)
        }
        list.setOnClickListener {
            listMenu.addItem(HListPopMenu.HPopListItem(true,R.drawable.archlinux,"Archlinux"))
            listMenu.addItem(HListPopMenu.HPopListItem(true,R.drawable.archlinux,"Archlinux"))
            listMenu.addItem(HListPopMenu.HPopListItem(true,R.drawable.archlinux,"Archlinux"))
            listMenu.setTitle("Archlinux")
            listMenu.dismissOnItemSelected=true
            listMenu.gravity=Gravity.TOP
            listMenu.showAttachToView(it)
        }
    }
}