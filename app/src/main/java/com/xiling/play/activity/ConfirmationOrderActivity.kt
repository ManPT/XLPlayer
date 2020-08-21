package com.xiling.play.activity

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.lib.base.BaseActivity
import com.xiling.play.R
import kotlinx.android.synthetic.main.activity_confirmation_order.*

class ConfirmationOrderActivity : BaseActivity() {

    var hasAddress = true

    override fun getContentViewId(): Int {
        return R.layout.activity_confirmation_order
    }

    override fun getIntentData(intent: Intent?) {

    }

    override fun requestData() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ButterKnife.bind(this)
        setLeftBlack()
        setTitle("确认订单")
        hasAddress(hasAddress)

        tvCrossedPrice.getPaint().setAntiAlias(true) //抗锯齿
        tvCrossedPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG)

    }


    private fun hasAddress(has: Boolean) {
        if (has) {
            ivDefault.visibility = View.VISIBLE
            tvAddress.visibility = View.VISIBLE
            tvAddressDetails.visibility = View.VISIBLE
            tvUserName.visibility = View.VISIBLE
            tvUserPhone.visibility = View.VISIBLE
            btnAddAddress.visibility = View.GONE

        } else {
            ivDefault.visibility = View.GONE
            tvAddress.visibility = View.GONE
            tvAddressDetails.visibility = View.GONE
            tvUserName.visibility = View.GONE
            tvUserPhone.visibility = View.GONE
            btnAddAddress.visibility = View.VISIBLE
        }

    }


    @OnClick(R.id.conAddress)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.conAddress -> {
                // 添加收货地址
                hasAddress = !hasAddress
                hasAddress(hasAddress)
            }

        }
    }

}