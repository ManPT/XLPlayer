package com.xiling.play.view

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshKernel
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.xiling.play.R

class XLRefreshHeader  constructor(
    context: Context?,
    attrs: AttributeSet? = null
) :
    LinearLayout(context, attrs, 0), RefreshHeader {
    private val mImageView: ImageView
    private val doorOpenDrawable: AnimationDrawable
    private val doorLoadingDrawable: AnimationDrawable

    override fun getView(): View {
        return this //真实的视图就是自己，不能返回null
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate //指定为平移，不能null
    }

    override fun onStartAnimator(
        layout: RefreshLayout,
        height: Int,
        maxDragHeight: Int
    ) {
        startAnimate() //开始动画
    }

    override fun onFinish(layout: RefreshLayout, success: Boolean): Int {
        mImageView.postDelayed({
            stopAnimate() //停止动画
        }, 550)
        return 600 //
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        when (newState) {
            RefreshState.None, RefreshState.PullDownToRefresh -> mImageView.setImageDrawable(
                doorOpenDrawable
            )
            RefreshState.Refreshing -> {
            }
            RefreshState.ReleaseToRefresh -> mImageView.setImageDrawable(doorOpenDrawable)
            RefreshState.PullDownCanceled, RefreshState.PullUpCanceled -> mImageView.setImageDrawable(
                doorOpenDrawable
            )
            else -> {
            }
        }
    }

    private fun startAnimate() {
        mImageView.setImageDrawable(doorOpenDrawable)
        if (doorOpenDrawable.isRunning) {
            doorOpenDrawable.stop()
        }
        if (doorLoadingDrawable.isRunning) {
            doorLoadingDrawable.stop()
        }
        doorOpenDrawable.start()
        mImageView.postDelayed({
            mImageView.setImageDrawable(doorLoadingDrawable)
            doorLoadingDrawable.start()
        }, doorOpenDrawableDuration.toLong())
    }

    private val doorOpenDrawableDuration: Int
        private get() {
            var duration = 0
            for (i in 0 until doorOpenDrawable.numberOfFrames) {
                duration += doorOpenDrawable.getDuration(i)
            }
            return duration
        }

    private fun stopAnimate() {
        if (doorOpenDrawable.isRunning) {
            doorOpenDrawable.stop()
        }
        if (doorLoadingDrawable.isRunning) {
            doorLoadingDrawable.stop()
        }
        mImageView.setImageDrawable(doorOpenDrawable)
    }

    override fun setPrimaryColors(vararg colors: Int) {}
    override fun onInitialized(
        kernel: RefreshKernel,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onReleased(
        refreshLayout: RefreshLayout,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onHorizontalDrag(
        percentX: Float,
        offsetX: Int,
        offsetMax: Int
    ) {
    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    init {
        gravity = Gravity.CENTER
        mImageView = ImageView(context)
        mImageView.setImageResource(R.drawable.anim_door_open)
        doorOpenDrawable = mImageView.drawable as AnimationDrawable

        doorLoadingDrawable = ContextCompat.getDrawable(
            getContext(),
            R.drawable.anim_door_loading
        ) as AnimationDrawable


        addView(mImageView, -1, -1)
        minimumHeight = DensityUtil.dp2px(60f)
    }
}