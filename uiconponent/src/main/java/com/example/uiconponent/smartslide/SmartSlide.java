package com.example.uiconponent.smartslide;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.example.uiconponent.R;

public class SmartSlide {

    public static SmartSlideWrapper wrap(View view) {
        SmartSlideWrapper wrapper = peekWrapperFor(view);
        if (wrapper != null){
            return wrapper;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (view.getParent() != null) {
            ViewGroup viewParent = (ViewGroup) view.getParent();
            wrapper = createNewWrapper(view.getContext());
            int index = viewParent.indexOfChild(view);
            viewParent.removeView(view);
            viewParent.addView(wrapper, index, layoutParams);
        } else {
            wrapper = createNewWrapper(view.getContext());
            wrapper.setLayoutParams(layoutParams);
        }
        wrapper.setContentView(view);
        wrapper.setBackgroundColor(view.getResources().getColor(R.color.colorPrimaryDark));
        return wrapper;
    }

    private static SmartSlideWrapper createNewWrapper(Context context) {
        return new SmartSlideWrapper(context);
    }

    public static SmartSlideWrapper peekWrapperFor(View view) {
        if (view.getParent() instanceof SmartSlideWrapper) {
            return (SmartSlideWrapper) view.getParent();
        }
        return null;
    }

    public static float ensureBetween(float origin, float min, float max) {
        return Math.max(min, Math.min(origin, max));
    }

    public static int dp2px(int dp, Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
