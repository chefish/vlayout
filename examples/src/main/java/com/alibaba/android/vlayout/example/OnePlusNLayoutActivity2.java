/*
 * MIT License
 *
 * Copyright (c) 2016 Alibaba Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.alibaba.android.vlayout.example;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.RecyclablePagerAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.VirtualLayoutManager.LayoutParams;
import com.alibaba.android.vlayout.layout.FixLayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelperEx;
import com.alibaba.android.vlayout.layout.ScrollFixLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * @author villadora
 */
public class OnePlusNLayoutActivity2 extends Activity {

    private static final boolean BANNER_LAYOUT = false;

    private static final boolean LINEAR_LAYOUT = false;

    private static final boolean ONEN_LAYOUT = true;

    private static final boolean GRID_LAYOUT = false;

    private static final boolean STICKY_LAYOUT = false;

    private static final boolean HORIZONTAL_SCROLL_LAYOUT = false;

    private static final boolean SCROLL_FIX_LAYOUT = false;

    private TextView mFirstText;
    private TextView mLastText;

    private TextView mCountText;

    private TextView mTotalOffsetText;

    private Runnable trigger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mFirstText = (TextView) findViewById(R.id.first);
        mLastText = (TextView) findViewById(R.id.last);
        mCountText = (TextView) findViewById(R.id.count);
        mTotalOffsetText = (TextView) findViewById(R.id.total_offset);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_view);

        findViewById(R.id.jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText position = (EditText) findViewById(R.id.position);
                if (!TextUtils.isEmpty(position.getText())) {
                    try {
                        int pos = Integer.parseInt(position.getText().toString());
                        recyclerView.scrollToPosition(pos);
                    } catch (Exception e) {
                        Log.e("VlayoutActivity", e.getMessage(), e);
                    }
                } else {
                    recyclerView.requestLayout();
                }
            }
        });


        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                mFirstText.setText("First: " + layoutManager.findFirstVisibleItemPosition());
                mLastText.setText("Existing: " + MainViewHolder.existing + " Created: " + MainViewHolder.createdTimes);
                mCountText.setText("Count: " + recyclerView.getChildCount());
                mTotalOffsetText.setText("Total Offset: " + layoutManager.getOffsetToStart());
            }
        });


        recyclerView.setLayoutManager(layoutManager);

        // layoutManager.setReverseLayout(true);

        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = ((LayoutParams) view.getLayoutParams()).getViewPosition();
                outRect.set(4, 4, 4, 4);
            }
        };


        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

        recyclerView.setRecycledViewPool(viewPool);

        // recyclerView.addItemDecoration(itemDecoration);

        viewPool.setMaxRecycledViews(0, 20);

        final DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);

        recyclerView.setAdapter(delegateAdapter);

        List<DelegateAdapter.Adapter> adapters = new LinkedList<>();


        if (BANNER_LAYOUT) {
            adapters.add(new SubAdapter(this, new LinearLayoutHelper(), 1) {

                @Override
                public void onViewRecycled(MainViewHolder holder) {
                    if (holder.itemView instanceof ViewPager) {
                        ((ViewPager) holder.itemView).setAdapter(null);
                    }
                }

                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    if (viewType == 1)
                        return new MainViewHolder(
                                LayoutInflater.from(OnePlusNLayoutActivity2.this).inflate(R.layout.view_pager, parent, false));

                    return super.onCreateViewHolder(parent, viewType);
                }

                @Override
                public int getItemViewType(int position) {
                    return 1;
                }

                @Override
                protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {

                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {
                    if (holder.itemView instanceof ViewPager) {
                        ViewPager viewPager = (ViewPager) holder.itemView;

                        viewPager.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));

                        // from position to get adapter
                        viewPager.setAdapter(new PagerAdapter(this, viewPool));
                    }
                }
            });
        }
//    1-8
        if (GRID_LAYOUT) {
            GridLayoutHelper layoutHelper;
            layoutHelper = new GridLayoutHelper(4);
            layoutHelper.setMargin(0, 10, 0, 10);
            layoutHelper.setHGap(3);
            layoutHelper.setAspectRatio(4f);
            adapters.add(new SubAdapter(this, layoutHelper, 8));
        }

        if (HORIZONTAL_SCROLL_LAYOUT) {

        }
//        9-10
        if (GRID_LAYOUT) {
            GridLayoutHelper layoutHelper;
            layoutHelper = new GridLayoutHelper(2);
            layoutHelper.setMargin(0, 10, 0, 10);
            layoutHelper.setHGap(3);
            layoutHelper.setAspectRatio(3f);
            adapters.add(new SubAdapter(this, layoutHelper, 2));
        }

//        23-27
        if (ONEN_LAYOUT) {
            OnePlusNLayoutHelperEx helper = new OnePlusNLayoutHelperEx();
            helper.setBgColor(0xff876384);
            adapters.add(new SubAdapter(this, helper, 5){
                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);

                    LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
//                    layoutParams.leftMargin = 10;
//                    layoutParams.topMargin = 10;
//                    layoutParams.rightMargin = 10;
                    layoutParams.bottomMargin = 10;
                    holder.itemView.setLayoutParams(layoutParams);
                }
            });
        }


        if (STICKY_LAYOUT) {
            StickyLayoutHelper layoutHelper = new StickyLayoutHelper();
            layoutHelper.setAspectRatio(4);
            adapters.add(new SubAdapter(this, layoutHelper, 1, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)));
        }

        if (SCROLL_FIX_LAYOUT) {
            ScrollFixLayoutHelper layoutHelper = new ScrollFixLayoutHelper(FixLayoutHelper.BOTTOM_RIGHT, 20, 20);
            layoutHelper.setShowType(ScrollFixLayoutHelper.SHOW_ON_LEAVE);
            adapters.add(new SubAdapter(this, layoutHelper, 1) {
                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                    LayoutParams layoutParams = new LayoutParams(50, 50);
                    holder.itemView.setLayoutParams(layoutParams);
                }
            });
        }

        if (LINEAR_LAYOUT)
            adapters.add(new SubAdapter(this, new LinearLayoutHelper(), 100));

        delegateAdapter.setAdapters(adapters);

        final Handler mainHandler = new Handler(Looper.getMainLooper());

        trigger = new Runnable() {
            @Override
            public void run() {
                // recyclerView.scrollToPosition(22);
                // recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.requestLayout();
                // mainHandler.postDelayed(trigger, 1000);
            }
        };


        mainHandler.postDelayed(trigger, 1000);
    }

    // RecyclableViewPager

    static class PagerAdapter extends RecyclablePagerAdapter<MainViewHolder> {
        public PagerAdapter(SubAdapter adapter, RecyclerView.RecycledViewPool pool) {
            super(adapter, pool);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public void onBindViewHolder(MainViewHolder viewHolder, int position) {
            // only vertical
            viewHolder.itemView.setLayoutParams(
                    new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            TextView textView = ((TextView) viewHolder.itemView.findViewById(R.id.title));
            textView.setText("Banner: " + position);

        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }
    }


    static class SubAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

        private Context mContext;

        private LayoutHelper mLayoutHelper;


        private LayoutParams mLayoutParams;
        private int mCount = 0;


        public SubAdapter(Context context, LayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        }

        public SubAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item1, parent, false));
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            // only vertical
//            holder.itemView.setLayoutParams(
//                    new LayoutParams(mLayoutParams));
        }


        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(Integer.toString(offsetTotal));
            ((TextView) holder.itemView.findViewById(R.id.title)).setBackgroundColor(ColorManager.getInstance().colorList.get(offsetTotal));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }


    static class MainViewHolder extends RecyclerView.ViewHolder {

        public static volatile int existing = 0;
        public static int createdTimes = 0;

        public MainViewHolder(View itemView) {
            super(itemView);
            createdTimes++;
            existing++;
        }

        @Override
        protected void finalize() throws Throwable {
            existing--;
            super.finalize();
        }
    }
}
