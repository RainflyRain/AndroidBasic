package com.example.uiconponent.input;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.uiconponent.R;

/**
 * desc   :
 * author : fei
 * date   : 2021/01/29
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
public class InputAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public InputAdapter() {
        super(R.layout.item_input_layout);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tvTxt,s);
    }
}