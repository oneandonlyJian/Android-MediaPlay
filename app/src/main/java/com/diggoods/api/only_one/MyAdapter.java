package com.diggoods.api.only_one;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.SimpleTimeZone;

/**
 * Create by  FengJianyi on 2019/6/24
 */
public class MyAdapter extends BaseQuickAdapter<MyBean.ResultBean,BaseViewHolder> {

    public MyAdapter(int layoutResId, @Nullable List<MyBean.ResultBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyBean.ResultBean item) {
        SimpleDraweeView imageView = helper.getView(R.id.item_image);
        imageView.setImageURI(item.getPic());
        helper.setText(R.id.item_name,item.getAuthor());
        helper.setText(R.id.item_title,item.getTitle());
    }
}
