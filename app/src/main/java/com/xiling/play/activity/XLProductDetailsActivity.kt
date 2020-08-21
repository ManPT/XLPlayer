package com.xiling.play.activity

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.listener.OnPageChangeListener
import com.lib.base.mvp.BaseMVPActivity
import com.mr.base.tools.ToastTool
import com.xiling.play.R
import com.xiling.play.adapter.CarouseViewHolder
import com.xiling.play.adapter.ShopListTagsAdapter
import com.xiling.play.bean.ShopDetailsBean
import com.xiling.play.dialog.SkuSelectDialog
import com.xiling.play.mananger.NumberManager
import com.xiling.play.mvp.ProductDetailsMode
import com.xiling.play.mvp.ProductDetailsPresenter
import com.xiling.play.mvp.ProductDetailsView
import com.xiling.play.tools.ShopTool
import com.xiling.play.view.AutoLayoutManager
import kotlinx.android.synthetic.main.activity_x_l_product_details.*

/**
 * 商品详情
 * @author 逄涛
 */
class XLProductDetailsActivity :
    BaseMVPActivity<ProductDetailsView, ProductDetailsMode, ProductDetailsPresenter>(),
    ProductDetailsView {

    var spuId: String? = null

    lateinit var webView: WebView
    lateinit var tagsAdapter: ShopListTagsAdapter

    companion object {
        public fun jump(context: Context, spuId: String) {
            var mIntent = Intent(context, XLProductDetailsActivity::class.java)
            mIntent.putExtra("spuId", spuId)
            context.startActivity(mIntent)
        }
    }


    override fun getContentViewId(): Int {
        return R.layout.activity_x_l_product_details
    }

    override fun getIntentData(intent: Intent?) {
        if (intent != null) {
            spuId = intent.getStringExtra("spuId")
        }

        if (TextUtils.isEmpty(spuId)) {
            ToastTool.showToast("spuId == null")
            finish()
        }


    }

    override fun requestData() {
        mMode!!.requestShopDetails(spuId!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ButterKnife.bind(this)
        setLeftBlack()
        setTitle("商品详情")
        setStatusBar(true)
        setStatusBarVisable(true)

        banner.startTurning(5000)

        tvMinmarketPrice.getPaint().setAntiAlias(true) //抗锯齿
        tvMinmarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG)

        webView = WebView(mContext)
        webView.setLayoutParams(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        webView.getSettings().setDomStorageEnabled(true)
        mProductDetailWebView.addView(webView)

        val autoLayoutManager = AutoLayoutManager()
        autoLayoutManager.setAutoMeasureEnabled(true)
        recyclerTag.setLayoutManager(autoLayoutManager)
        tagsAdapter = ShopListTagsAdapter(R.layout.item_shop_list_tag)
        recyclerTag.adapter = tagsAdapter


        requestData()
    }


    override fun updateBanner(bannerList: List<String>) {
        if (bannerList.size > 0) {
            tv_indicator.visibility = View.VISIBLE
        } else {
            tv_indicator.visibility = View.GONE
        }

        val indicatorText = "%s/" + bannerList.size
        tv_indicator.setText(String.format(indicatorText, "1"))

        banner.setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View?): CarouseViewHolder<String> {
                var holder = CarouseViewHolder<String>(itemView, mContext)

                return holder
            }

            override fun getLayoutId(): Int {
                return R.layout.item_banner
            }


        }, bannerList)
            .setOnPageChangeListener(object : OnPageChangeListener {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {

                }

                override fun onPageSelected(index: Int) {
                    var position = index
                    tv_indicator.setText(kotlin.String.format(indicatorText, ++position))
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {

                }

            })

        banner.setCanLoop(bannerList.size > 1)
    }

    private fun setStatus(status: Int, stock: Int) {
        val mStatus: String = ShopTool.checkShopStatus(status, stock)
        if (!TextUtils.isEmpty(mStatus)) {
            // 已售罄
            tv_sold_out.visibility = View.VISIBLE
            tv_sold_out.text = mStatus
            tv_rmb.isEnabled = false
            tvMinmarketPrice.isEnabled = false
            tvDiscountPriceDecimal.isEnabled = false
            btnBuy.isEnabled = false
        } else {
            tv_sold_out.visibility = View.GONE
            tv_rmb.isEnabled = true
            tvMinmarketPrice.isEnabled = true
            tvDiscountPriceDecimal.isEnabled = true
            btnBuy.isEnabled = true
        }
    }

    override fun updateDetails(shopDetailsBean: ShopDetailsBean) {

        setStatus(1, shopDetailsBean.stock)

        NumberManager.setPriceText(
            shopDetailsBean.originalPrice,
            tvMinmarketPrice,
            tvDiscountPriceDecimal
        )

        //划线价
        tvMinMarketPrice.setText("¥" + NumberManager.reservedDecimalFor2(shopDetailsBean.originalPrice))

        tvSpuName.text = shopDetailsBean.spuName

        if (!TextUtils.isEmpty(shopDetailsBean.labelName)) {
            var tags: List<String> = shopDetailsBean.labelName.split(",")
            tagsAdapter.setNewData(tags)
        } else {
            tagsAdapter.setNewData(ArrayList<String>())
        }


        if (shopDetailsBean.saleCount >= 10000) {
            tvSaleSize.text =
                "已售" + NumberManager.reservedDecimalFor2(shopDetailsBean.saleCount / 10000) as Int + "万"
        } else {
            tvSaleSize.text = "已售" + shopDetailsBean.saleCount.toInt()
        }

        tvSkuInfo.text = shopDetailsBean.skuName

    }

    override fun updateWebView(htmlString: String) {

        var content = htmlString

        mProductDetailWebView.isFocusable = false

        if (!content.endsWith("<p><br/></p>")) {
            content += "<p><br/></p>"
        }
        content = String.format(
            "<!DOCTYPE html><html lang=\"zh-CN\"><head><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\"><style>body{padding:0;margin:0;}p{padding:0;margin:0;} p:last-child {line-height:0;} img{vertical-align:bottom;width:100%%;font-size:0;padding:0;margin:0;}</style></head><body>%s</body></html>",
            content
        )
        webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
    }


    @OnClick(R.id.btn_collection, R.id.btn_share, R.id.relSkuInfo,R.id.btnBuy)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_collection -> {
                // 收藏
            }

            R.id.btn_share -> {
                // 分享
            }

            R.id.btnBuy -> {
                // 立即购买
                //SkuSelectDialog(mContext).show()
                startActivity(Intent(mContext,ConfirmationOrderActivity ::class.java))
            }

            R.id.relSkuInfo -> {
                // 规格选择
                SkuSelectDialog(mContext).show()
            }
        }
    }


    override fun onDestroy() {
        val parent = webView.parent as ViewGroup
        parent?.removeView(webView)
        webView.settings.javaScriptEnabled = false
        webView.removeAllViews()
        webView.tag = null
        webView.clearHistory()
        webView.destroy()
        super.onDestroy()
    }

}
