package com.alibaba.android.vlayout.example;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.DelegateAdapter.Adapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longerian on 2017/11/14.
 *
 * @author longerian
 * @date 2017/11/14
 */

public class FishTestActivity extends Activity {
    private static final String TAG = "FishTestActivity";

    DelegateAdapter delegateAdapter;
    SubAdapter subAdapter0;
    ColorManager colorManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        colorManager= new ColorManager();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_view);

        initData();

        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        List<Adapter> adapterList = new ArrayList<>();
        subAdapter0 = new SubAdapter(new LinearLayoutHelper(0), 3);
        adapterList.add(subAdapter0);
//        adapterList.add(new SubAdapter(new StickyLayoutHelper(true), 1));
//        adapterList.add(new SubAdapter(new LinearLayoutHelper(20), 20));
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4,-1,0,20);
        gridLayoutHelper.setMargin(0,0,20,0);
        adapterList.add(new SubAdapter(gridLayoutHelper, 20));
        adapterList.add(new SubAdapter(new LinearLayoutHelper(0), 5));
        adapterList.add(new SubAdapter(new GridLayoutHelper(4), 72));
        // adapterList.add(new SubAdapter(new FixLayoutHelper(0, 0), 1));
//        adapterList.add(new SubAdapter(new FixLayoutHelper(TOP_RIGHT, 0, 0), 1));
        delegateAdapter.addAdapters(adapterList);
        recyclerView.setLayoutManager(virtualLayoutManager);
        recyclerView.setAdapter(delegateAdapter);


        Button btn = (Button) findViewById(R.id.jump);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.add(3, 4);
                subAdapter0.mItemCount++;
                subAdapter0.notifyDataSetChanged();

            }
        });

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

        private SubAdapter(LayoutHelper layoutHelper, int itemCount) {
            mLayoutHelper = layoutHelper;
            mItemCount = itemCount;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public SubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new SubViewHolder(inflater.inflate(R.layout.item1, parent, false));
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
