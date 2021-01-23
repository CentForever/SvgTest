package jp.shts.android.trianglelabelview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import jp.shts.android.trianglelabelview.DrawableUtil.OnDrawableListener
import jp.shts.android.trianglelabelview.reflection.CustomToast
import jp.shts.android.trianglelabelview.reflection.KotlinAnnotationReflectionExt


class MainActivity : AppCompatActivity() {
    private var activity:Activity? =null
    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setTitle(R.string.app_name)
        stateMachine.initialState(State.None)
        activity = this
        val msg="从前面的app导出dex，我们已得到：（一个或）多个dex文件，而这么多dex，其中只有一个是真正包含了安卓app的业务逻辑的dex文件。"
        val span3 = SpannableString(msg)
        val image = ImageSpan(this, R.mipmap.icon_info, DynamicDrawableSpan.ALIGN_BOTTOM)
        span3.setSpan(image, msg.length - 1, msg.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(R.id.mTextView).text = span3
        mode8()
        // mode9()
        //createLink(msg)
        //createLink()
        //setSpanLink("百度手机卫士","https://www.baidu.com",0,2)
        val drawableUtil = DrawableUtil(findViewById<TextView>(R.id.mTextView2), object : OnDrawableListener {
            override fun onLeft(v: View?, left: Drawable?) {
                Toast.makeText(applicationContext, "left", Toast.LENGTH_SHORT).show()
            }

            override fun onRight(v: View?, right: Drawable?) {
                Toast.makeText(applicationContext, "right", Toast.LENGTH_SHORT).show()
            }
        })
        KotlinAnnotationReflectionExt(MainActivity::class.java, TestAnnotation::class.java).findPropertiesWithAnnotationInternal()

        val c: Class<MainActivity> = MainActivity::class.java
        c.declaredMethods.filter {
            return@filter it.isAnnotationPresent(TestAnnotation2::class.java)
        }.forEach {
            val annotation = it.getAnnotation(TestAnnotation2::class.java) as TestAnnotation2
            val propertyName = it.name
            annotation.value.forEach {
                Log.e("findPropertiesWithAnnotationInternal:", "annotationValue:${it}")
            }
        }

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        // intent.type = "video/*"
        startActivityForResult(intent, 11)
    }

    @TestAnnotation()
    fun setSpanLink(txt: String, url: String, start: Int, end: Int) {
        if (txt.length > end) {
            findViewById<TextView>(R.id.mTextView).text = txt
            val sp = SpannableString(txt)
            sp.setSpan(URLSpan(url), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sp.setSpan(object : ClickableSpan() {
                override fun onClick(textView: View) {}
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ds.linkColor
                    /** Remove the underline */
                    ds.isUnderlineText = false
                }
            }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sp.setSpan(ForegroundColorSpan(ContextCompat.getColor(activity!!, R.color.blue_ribbon_alpha_100)), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            //findViewById<TextView>(R.id.mTextView).highlightColor = ContextCompat.getColor(activity!!, android.R.color.transparent)
            findViewById<TextView>(R.id.mTextView).text = sp
            findViewById<TextView>(R.id.mTextView).movementMethod = LinkMovementMethod.getInstance()
        } else {
            // logInfo { "setBottomTip:$txt input txt error" }
        }
    }

    fun createLink(link: String) {
        val spannableString = SpannableString(link)
        spannableString.setSpan(URLSpan("http://www.baidu.com"), 0, link.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(R.id.mTextView).highlightColor = Color.parseColor("#B4DDF4")
        findViewById<TextView>(R.id.mTextView).text = spannableString
        findViewById<TextView>(R.id.mTextView).movementMethod = LinkMovementMethod.getInstance()
    }

    @SuppressLint("ResourceAsColor")
    @TestAnnotation2("Hello testMethod 1", "dded")
    fun createLink() {
        val sp = SpannableString("百度手机卫士")
        sp.setSpan(ForegroundColorSpan(Color.YELLOW), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sp.setSpan(BackgroundColorSpan(Color.TRANSPARENT), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sp.setSpan(URLSpan("http://www.baidu.com"), 0, 2,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sp.setSpan(object : ClickableSpan() {
            override fun onClick(textView: View) {}

            //去除连接下划线
            override fun updateDrawState(ds: TextPaint) {
                /**set textColor */
                ds.color = ds.linkColor
                /**Remove the underline */
                ds.isUnderlineText = false
            }
        }, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sp.setSpan(BackgroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        sp.setSpan(ForegroundColorSpan(activity!!.let { ContextCompat.getColor(it, R.color.blue_ribbon_alpha_100) }), 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        findViewById<TextView>(R.id.mTextView).highlightColor = activity?.let { ContextCompat.getColor(it, android.R.color.transparent) }!! //不设置该属性，点击后会有背景色
        findViewById<TextView>(R.id.mTextView).text = sp
        findViewById<TextView>(R.id.mTextView).movementMethod = LinkMovementMethod.getInstance()
    }

    private open class NoLineClickSpan(var text: String) : ClickableSpan() {
        override fun onClick(widget: View) {}
        override fun updateDrawState(ds: TextPaint) {
            ds.color = ds.linkColor
            ds.isUnderlineText = false //去掉下划线
        }

    }

    private fun mode8() {

        val spannableString = SpannableStringBuilder()
        spannableString.append("暗影IV已经开始暴走了")
        //val imageSpan = ImageSpan(this, R.mipmap.ic_launcher)
        //也可以这样
        //val drawable = resources.getDrawable(R.mipmap.ic_launcher);
        val drawable = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
        drawable?.setBounds(0, 0, dp2px(40f), dp2px(40f))
        val imageSpan1 = drawable?.let { ImageSpan(it) };
        //将index为6、7的字符用图片替代
        spannableString.setSpan(imageSpan1, 6, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                showMsg()
            }
        }
        spannableString.setSpan(clickableSpan, 6, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        (findViewById<TextView>(R.id.mTextView)).text = spannableString
        (findViewById<TextView>(R.id.mTextView)).movementMethod = LinkMovementMethod.getInstance()
    }



    private fun mode9() {
        val spannableString = SpannableStringBuilder()
        val msg= "仅您邀请的微信好友或群成员可入会，该邀请无法被他人转发  "
        spannableString.append(msg)
        //val imageSpan = ImageSpan(this, R.mipmap.ic_launcher);
        val drawable = activity?.let { ContextCompat.getDrawable(it, R.mipmap.icon_info) }
        drawable?.setBounds(0, 0, 80, 80)
        val imageSpan = ImageSpan(drawable!!)
        spannableString.setSpan(imageSpan, msg.length - 1, msg.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                //showMsg()
                val completeText =(view as TextView).text as SpannableString
                Log.v("characters position", "" + completeText.getSpanStart(this) + " / " + completeText.getSpanEnd(this));
                initPopWindow(view as TextView, activity, completeText.getSpanEnd(this))
                getTextViewSelectionBottomY(view as TextView, completeText.getSpanStart(this))
            }
        }
        spannableString.setSpan(clickableSpan, msg.length - 1, msg.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //文字颜色
        val colorSpan =  ForegroundColorSpan(Color.parseColor("#FFFFFF"));
        spannableString.setSpan(colorSpan, 5, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //文字背景颜色
        val bgColorSpan = BackgroundColorSpan(Color.parseColor("#009ad6"));
        spannableString.setSpan(bgColorSpan, 5, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        val textView = (findViewById<TextView>(R.id.mTextView))
        textView.text = spannableString;
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    fun initPopWindow(v: TextView, mContext: Context?, index: Int) {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_popip, null, false)
        val btn_xixi = view.findViewById<View>(R.id.btn_xixi) as Button
        val btn_hehe = view.findViewById<View>(R.id.btn_hehe) as Button
        //btn_xixi.text = "这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的"
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        val popWindow = PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        //popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.isTouchable = true
        popWindow.setTouchInterceptor { v, event ->
            false
            // 这里如果返回true的话，touch事件将被拦截
            // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
        }
        popWindow.setBackgroundDrawable(ColorDrawable(0x00000000)) //要为popWindow设置一个背景才有效

        val layout = v.layout
        val bound = Rect()
        val line = layout.getLineForOffset(index)
        layout.getLineBounds(line, bound)
        val yAxisTop = bound.top;//字符顶部y坐标
        val xAxisLeft = layout.getPrimaryHorizontal(index);//字符左边x坐标
        val xAxisRight = layout.getSecondaryHorizontal(index);//字符右边x坐标

        val width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED)
        val height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED)
        view.measure(width, height)
        view.measuredWidth // 获取宽度

        view.measuredHeight // 获取高度


        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, xAxisLeft.toInt() - view.measuredWidth / 2 - 40, yAxisTop - 50)

        //设置popupWindow里的按钮的事件
        btn_xixi.setOnClickListener { Toast.makeText(mContext, "你点击了嘻嘻~", Toast.LENGTH_SHORT).show() }
        btn_hehe.setOnClickListener {
            // Toast.makeText(mContext, "你点击了呵呵~", Toast.LENGTH_SHORT).show()
            popWindow.dismiss()
        }
    }

    /**
     * 获取TextView某一个字符的坐标
     *
     * @parms tv
     * @parms index 字符下标
     * @return 返回的是相对坐标
     */
    private fun getTextViewSelectionBottomY(tv: TextView, index: Int): Int {
        val layout = tv.layout
        val bound = Rect()
        val line = layout.getLineForOffset(index)
        layout.getLineBounds(line, bound)
        val yAxisTop = bound.top;//字符顶部y坐标
        val xAxisLeft = layout.getPrimaryHorizontal(index);//字符左边x坐标
        val xAxisRight = layout.getSecondaryHorizontal(index);//字符右边x坐标
        return bound.bottom
    }

    fun showMsg(){
        Toast.makeText(this, "menggang", Toast.LENGTH_LONG).show()
    }

    fun dp2px(dpValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun dp2px(@DimenRes resId: Int): Int {
        return resources.getDimensionPixelSize(resId)
    }

    val measuredWidth: Int
        get() = 20 and View.MEASURED_SIZE_MASK

    enum class State { None,Select }

    enum class Transition { Selected, Canceled }

    private val stateMachine: StateMachine<State, Transition> by lazy {
        StateMachine<State, Transition>().apply {

            state(State.None) {
                lastState?.apply {
                    println("I have been ${State.None} from $this by $lastTransition")
                } ?: with(State.None) {
                    println("I have been initialState with $this")
                }
            }

            state(State.Select) {
                lastState?.apply {
                    println("I have been ${State.Select} from $this by $lastTransition")
                } ?: with(State.Select) {
                    println("I have been initialState with $this")
                }
            }
            addTransition(State.None, Transition.Selected, State.Select)
            addTransition(State.Select, Transition.Canceled, State.None)
        }
    }

}

