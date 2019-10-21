package com.alibaba.android.vlayout.example;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by fish on 2019-10-21.
 * yuxm_zju@aliyun.com
 */
public class AFramelayout extends FrameLayout {
    public AFramelayout(@NonNull Context context) {
        super(context);
    }

    public AFramelayout(@NonNull Context context,
        @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AFramelayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AFramelayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr,
        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CLog.d("AFramelayout onMeasure >> =width"+MeasureSpec.getSize(widthMeasureSpec)+"-height="+MeasureSpec.getSize(heightMeasureSpec)+"-"+hashCode());
        CLog.d("AFramelayout onMeasure >> =width"+Utils.getMode(widthMeasureSpec)+"-height="+Utils.getMode(heightMeasureSpec)+"-"+hashCode());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        CLog.d("AFramelayout onMeasure << height="+getMeasuredHeight());

    }
}
