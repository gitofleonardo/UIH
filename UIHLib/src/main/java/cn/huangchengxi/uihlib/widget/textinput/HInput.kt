package cn.huangchengxi.uihlib.widget.textinput

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.widget.addTextChangedListener
import cn.huangchengxi.uihlib.R
import com.bumptech.glide.Glide

class HInput(context: Context, attrs:AttributeSet?, defStyle:Int):FrameLayout(context,attrs,defStyle){
    private var clearBtn:FrameLayout?=null
    private var toggleBtn:FrameLayout?=null
    private var passwordInput:EditText?=null
    private var eyeIcon:ImageView?=null
    private var clearIcon:ImageView?=null
    private var visibleNow=false
    private val imm by lazy { context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager}

    private var openId=R.drawable.eye_opened
    private var closeId=R.drawable.eye_closed

    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)
    init {
        val view= View.inflate(context,R.layout.view_password_input,this)
        clearBtn=view.findViewById(R.id.closeBtn)
        toggleBtn=view.findViewById(R.id.toggleVisibility)
        passwordInput=view.findViewById(R.id.editPassword)
        eyeIcon=view.findViewById(R.id.iconEye)
        clearIcon=view.findViewById(R.id.iconClose)

        val array=context.obtainStyledAttributes(attrs,R.styleable.HInput)
        val bg=array.getDrawable(R.styleable.HInput_android_background)
        val inputType=array.getInt(R.styleable.HInput_android_inputType,InputType.TYPE_CLASS_TEXT)
        if (inputType==InputType.TYPE_CLASS_TEXT.or(InputType.TYPE_TEXT_VARIATION_PASSWORD) || inputType==InputType.TYPE_CLASS_NUMBER.or(InputType.TYPE_NUMBER_VARIATION_PASSWORD)){
            toggleBtn!!.visibility= View.VISIBLE
        }else{
            toggleBtn!!.visibility= View.GONE
        }
        passwordInput!!.inputType=inputType
        view.background=bg
        array.recycle()

        clearBtn!!.setOnClickListener {
            passwordInput!!.text=SpannableStringBuilder("")
        }
        toggleBtn!!.setOnClickListener {
            val state=imm.isActive
            val hasSelection=passwordInput!!.hasSelection()
            val selectionStart=passwordInput!!.selectionStart
            val selectionEnd=passwordInput!!.selectionEnd
            if (!visibleNow){
                //invisible now
                passwordInput!!.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                Glide.with(context).load(openId).into(eyeIcon!!)
                visibleNow=true
            }else{
                passwordInput!!.inputType=InputType.TYPE_TEXT_VARIATION_PASSWORD.or(InputType.TYPE_CLASS_TEXT)
                Glide.with(context).load(closeId).into(eyeIcon!!)
                visibleNow=false
            }
            if (hasSelection){
                passwordInput!!.setSelection(selectionStart,selectionEnd)
            }else{
                passwordInput!!.setSelection(selectionStart)
            }
            if (!imm.isActive && state){
                imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }
    fun setText(text:String){
        passwordInput!!.text=SpannableStringBuilder(text)
    }
    fun setToggleEyeEnabled(enabled:Boolean){
        toggleBtn!!.visibility=if (enabled) View.VISIBLE else View.GONE
    }
    fun setClearEnabled(enabled: Boolean){
        clearBtn!!.visibility=if (enabled) View.VISIBLE else View.GONE
    }
    fun setOnTextChangeListener(listener:(Editable?)->Unit){
        passwordInput!!.addTextChangedListener {
            listener.invoke(it)
        }
    }
    fun setClearIcon(@DrawableRes id:Int){
        Glide.with(context).load(id).into(eyeIcon!!)
    }
    fun setToggleIconVisible(@DrawableRes id:Int){
        openId=id
    }
    fun setToggleIconInvisible(@DrawableRes id:Int){
        closeId=id
    }
}