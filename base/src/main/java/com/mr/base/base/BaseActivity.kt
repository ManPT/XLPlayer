package com.lib.base

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.lib.tools.ActivityManagerTool
import com.lib.tools.LogTool
import com.mr.base.R
import com.mr.base.base.HeaderLayout
import kotlinx.android.synthetic.main.base_layout.*

open abstract class BaseActivity : AppCompatActivity() {

    var TAG: String = ""
    public lateinit var mContext:Context
    /**
     * parentView的Id
     */
    abstract fun getContentViewId(): Int


    /**
     * 获取传值
     */
    abstract fun getIntentData(intent: Intent?)

    /**
     * 数据请求
     */
    abstract fun requestData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        ActivityManagerTool.createActivity(this)
        setContentView(R.layout.base_layout)
        layoutInflater.inflate(getContentViewId(), baseContentLayout, true)
        TAG = javaClass.simpleName
        LogTool.i("Life Cycle ：", TAG + " :onCreate()")
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        if (intent != null) {
            getIntentData(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("Life Cycle ：", TAG + " :onResume()")
    }

    override fun onStop() {
        super.onStop()
        LogTool.i("Life Cycle ：", TAG + " :onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManagerTool.destroyActivity(this)
        LogTool.i("Life Cycle ：", TAG + " :onDestroy()")
    }

    /**
     *
     * desc:状态栏设置 设置布局侵入到状态栏
     *
     */
    protected fun setStatusBar(dark : Boolean) {
        //StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            val decorView: View = window.getDecorView()
            decorView.setOnApplyWindowInsetsListener { v, insets ->
                val defaultInsets = v.onApplyWindowInsets(insets)
                defaultInsets.replaceSystemWindowInsets(
                    defaultInsets.systemWindowInsetLeft,
                    0,
                    defaultInsets.systemWindowInsetRight,
                    defaultInsets.systemWindowInsetBottom
                )
            }

            setStatusBarTextColor(dark)

            ViewCompat.requestApplyInsets(decorView)

            //将状态栏设成透明，如不想透明可设置其他颜色
            window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent))
        }
    }

    /**
     * 设置状态栏文字颜色
     */
    protected fun setStatusBarTextColor(dark : Boolean) {
        val decorView: View = window.getDecorView()
        if (dark) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        } else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        }

    }

    protected fun setStatusBarVisable(open:Boolean){
        headerLayout.setVisiableStatusBar(open)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        headerLayout.setTitle(title)
    }

    open fun setLeftBlack(backListener: View.OnClickListener?) {
        headerLayout.setLeftDrawable(R.mipmap.icon_back_black)
        headerLayout.setOnLeftClickListener(backListener)
    }

    open fun setLeftBlack() {
        headerLayout.setLeftDrawable(R.mipmap.icon_back_black)
        headerLayout.setOnLeftClickListener(View.OnClickListener { onBackPressed() })
    }

    open fun setLeftWhite() {
        headerLayout.setLeftDrawable(R.mipmap.icon_back_white)
        headerLayout.setOnLeftClickListener(View.OnClickListener { finish() })
    }

    open fun showHeader(title: CharSequence?, isBlack: Boolean) {
        showHeader()
        setTitle(title)
        if (isBlack) {
            setLeftBlack()
        }
    }


    /**
     * EditText获取焦点并显示软键盘
     */
    open fun showSoftInputFromWindow(editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        this.window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    /**
     * 隐藏键盘
     */
    open fun hideKeyboard() {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    open fun getHeaderLayout() :HeaderLayout{
        return headerLayout
    }

    open fun showHeader() {
        headerLayout.show()
    }
}
