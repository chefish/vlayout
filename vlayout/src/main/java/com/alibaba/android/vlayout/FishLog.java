package com.alibaba.android.vlayout;

import android.util.Log;

/**
 * Created by fish on 2019-10-31.
 * yuxm_zju@aliyun.com
 */
public class FishLog {
    public static void d(String s){
        Log.d("childfish",s);
    }

    public static void d(String tag,String msg){
        Log.d("childfish."+tag,msg);
    }
}
