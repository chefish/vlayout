package com.alibaba.android.vlayout.example;

import android.graphics.Color;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by fish on 2019/7/17.
 * yuxm_zju@aliyun.com
 */
public class ColorManager {
    private static volatile ColorManager sInstance;

    public static ColorManager getInstance() {
        if (sInstance == null) {
            synchronized (ColorManager.class) {
                if (sInstance == null) {
                    sInstance = new ColorManager();
                }
            }
        }
        return sInstance;
    }
    public List<Integer> colorList;

    public ColorManager() {
        colorList = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            colorList.add(randomColor());
        }
    }

    public void setColor(View v,int n){
        v.setBackgroundColor(colorList.get(n));
    }


    private int randomColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }
}
