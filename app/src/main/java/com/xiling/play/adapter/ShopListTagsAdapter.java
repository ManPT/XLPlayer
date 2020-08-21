package com.xiling.play.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.play.R;

/**
 * pt
 * 商品标签
 */
public class ShopListTagsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ShopListTagsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (item != null){
            helper.setText(R.id.tv_title,item);
        }

    }

}
