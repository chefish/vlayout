
package com.alibaba.android.vlayout.example;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.DelegateAdapter.Adapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by longerian on 2017/11/14.
 *
 * @author longerian
 * @date 2017/11/14
 */

public class FishTestHActivity extends Activity {
    private static final String TAG = "FishTestActivity";

    DelegateAdapter delegateAdapter;
    SubAdapter subAdapter0;
    ColorManager colorManager;
    List<Adapter> adapterList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_fish_h);

        colorManager= new ColorManager();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_view);

        initData();

        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(this, OrientationHelper.HORIZONTAL,false);
        //virtualLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        adapterList = new ArrayList<>();
        subAdapter0 = new SubAdapter(new LinearLayoutHelper(0), 30);
        adapterList.add(subAdapter0);
        //////3-22
        //GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4,-1,0,20);
        //gridLayoutHelper.setMargin(0,0,20,0);
        //adapterList.add(new SubAdapter(gridLayoutHelper, 20));
        //LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper(0);
        //linearLayoutHelper.setMarginTop(15);
        ////只有头一个有效果
        //adapterList.add(new SubAdapter(linearLayoutHelper, 5));
        //adapterList.add(new SubAdapter(new GridLayoutHelper(4), 72));
        //
        //

        delegateAdapter.addAdapters(adapterList);
        recyclerView.setLayoutManager(virtualLayoutManager);
        recyclerView.setAdapter(delegateAdapter);


        Button btn = (Button) findViewById(R.id.jump);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInSubAdapter();
                //addData();

                //addData2();
            }
        });

    }

    private void addData2() {
        data.add(1,777);
        subAdapter0.mItemCount++;
        LayoutHelper layoutHelper = subAdapter0.onCreateLayoutHelper();
        layoutHelper.setItemCount(layoutHelper.getItemCount()+1);
        subAdapter0.notifyDataSetChanged();
    }

    //插组件 要用这种方式
    private void addData() {
        data.add(23,1111);
        SubAdapter sss = new SubAdapter(new LinearLayoutHelper(30), 1);

        delegateAdapter.addAdapter(2,sss);
        delegateAdapter.notifyDataSetChanged();


//        adapterList.add(2,sss);
//        delegateAdapter.setAdapters(adapterList);
    }

    private void addInSubAdapter() {
        data.add(2, 4);
        subAdapter0.mItemCount++;
        //LayoutHelper layoutHelper = subAdapter0.onCreateLayoutHelper();
        //layoutHelper.setItemCount(layoutHelper.getItemCount()+1);
        subAdapter0.notifyDataSetChanged();
        //delegateAdapter.notifyDataSetChanged();
        //这个效率是很高的
        //subAdapter0.notifyItemInserted(3);
    }

    private void initData() {
        for (int i = 0; i < 100; i++) {
            data.add(i);
        }
    }

    private List<Integer> data = new ArrayList<>();


    private class SubAdapter extends Adapter<SubViewHolder> {

        private LayoutHelper mLayoutHelper;
        private int mItemCount;
        //private List subDataList;

        private SubAdapter(LayoutHelper layoutHelper, int itemCount) {
            mLayoutHelper = layoutHelper;
            mItemCount = itemCount;
            //this.subDataList=subDataList;
        }


        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public SubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder: ");
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new SubViewHolder(inflater.inflate(R.layout.item1, parent, false));
        }

        //这里position是相对布局
        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(SubViewHolder holder, int position) {
            // do nothing
        }

        @Override
        protected void onBindViewHolderWithOffset(SubViewHolder holder, int position, int offsetTotal) {
            super.onBindViewHolderWithOffset(holder, position, offsetTotal);
            Log.d(TAG, "onBindViewHolderWithOffset: position="+position+" offsetTotal="+offsetTotal+" "+this);
            holder.setText("" + data.get(offsetTotal),colorManager.colorList.get(position));
            holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }

    private static class SubViewHolder extends RecyclerView.ViewHolder {

        public static volatile int existing = 0;
        public static int createdTimes = 0;

        public SubViewHolder(View itemView) {
            super(itemView);
            createdTimes++;
            existing++;
            Log.d(TAG, "cnt =" + existing);
        }

        public void setText(String title,int color) {
            ((TextView) itemView.findViewById(R.id.title)).setText(title);
            itemView.setBackgroundColor(color);
        }

        @Override
        protected void finalize() throws Throwable {
            existing--;
            super.finalize();
            Log.d(TAG, "cnt =" + existing);
        }
    }


}
