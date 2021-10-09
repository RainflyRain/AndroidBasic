package com.example.uiconponent.refresh;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uiconponent.R;

import java.util.Calendar;

/**
 * created by Fly on 2020/2/25
 */
public class FDefaultHeader extends RelativeLayout implements FHeader{

    private ImageView iconView;
    private TextView tvLastUpdateTime,tvReleaseUpdate;
    private String mLaskUpdateTime = "";

    public FDefaultHeader(Context context) {
        this(context,null);
    }

    public FDefaultHeader(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FDefaultHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootHeader = View.inflate(context, R.layout.default_header,this);
        iconView = rootHeader.findViewById(R.id.f_default_progress);
        tvLastUpdateTime = rootHeader.findViewById(R.id.f_default_update);
        tvReleaseUpdate = rootHeader.findViewById(R.id.f_default_title);
    }

    @Override
    public FHeader wrapper(View headerView) {
        return null;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onMoving(float percent, int offset, int height) {
        iconView.setRotation(percent*360);
        if (percent > 0.5){
            Calendar calendar = Calendar.getInstance();
            mLaskUpdateTime =  calendar.get(Calendar.YEAR)+"年"+calendar.get(Calendar.MONTH)+
                    "月"+calendar.get(Calendar.DAY_OF_MONTH)+"日 "+calendar.get(Calendar.HOUR_OF_DAY)
                    +"时"+calendar.get(Calendar.MINUTE)+"分"+calendar.get(Calendar.SECOND)+"秒";
            tvReleaseUpdate.setText("释放刷新");
        }

        if (!TextUtils.isEmpty(mLaskUpdateTime)){
            tvLastUpdateTime.setText(mLaskUpdateTime);
        }

    }

    @Override
    public void onRelease(int height, int maxHeight) {
        tvReleaseUpdate.setText("下拉刷新");
        iconView.setRotation(0);
    }

}
