package com.alibaba.android.vlayout.example;

import android.view.View.MeasureSpec;

/**
 * Created by fish on 2019-10-21.
 * yuxm_zju@aliyun.com
 */
public class Utils {
    public static String getMode(int measuspec) {
        int mode = MeasureSpec.getMode(measuspec);
        if(mode==MeasureSpec.AT_MOST){
            return "AT_MOST";
        }else if(mode==MeasureSpec.EXACTLY){
            return "EXACTLY";
        }else if(mode==MeasureSpec.UNSPECIFIED){
            return "UNSPECIFIED";
        }
        return "";
    }
}
