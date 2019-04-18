package com.zinc.code_x2_recycleview.recycler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zinc.code_x2_recycleview.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author : Jiang zinc
 * @version :
 * @email : 56002982@qq.com
 * @time : 2019/2/28 下午4:48
 * @desc :
 */
public class RecyclerActivity extends AppCompatActivity implements RecyclerAdapter.BindListener {

    private final static String TAG = RecyclerActivity.class.getSimpleName();

    private RecycleViewWrapper mRvWrapper;
    private TextView mTvCacheLog;
    private TextView mTvBindLog;
    private ScrollView mSvBindLog;

    private StringBuilder logString;

    private RecyclerAdapter mAdapter;
    private final List<RecyclerBean> mData = new ArrayList<>();

    private Random mRandom = new Random();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        mRvWrapper = findViewById(R.id.rv_wrapper);
        mTvCacheLog = findViewById(R.id.tv_cache_log);
        mTvBindLog = findViewById(R.id.tv_bind_log);
        mSvBindLog = findViewById(R.id.sv_bind_log);

        logString = new StringBuilder();

        mAdapter = new RecyclerAdapter(this, mData);
        mAdapter.setBindListener(this);

        mRvWrapper.setLayoutManager(new LinearLayoutManager(this));
        mRvWrapper.setAdapter(mAdapter);

        mRvWrapper.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                showCacheInfo();
            }
        });

        mRvWrapper.setLayoutListener(new RecycleViewWrapper.LayoutListener() {
            @Override
            public void beforeLayout() {
                try {
                    Field recyclerField =
                            Class.forName("android.support.v7.widget.RecyclerView")
                                    .getDeclaredField("mRecycler");
                    recyclerField.setAccessible(true);
                    RecyclerView.Recycler recyclerInstance =
                            (RecyclerView.Recycler) recyclerField.get(mRvWrapper);

                    Class<?> recyclerClass = Class.forName(recyclerField.getType().getName());
                    Field attachedScrapField = recyclerClass.getDeclaredField("mAttachedScrap");
                    attachedScrapField.setAccessible(true);
                    // 这里的 mAttachedScrap 属性值虽然是 final，但可以被反射修改
                    attachedScrapField.set(recyclerInstance,
                            new ArrayListWrapper<RecyclerView.ViewHolder>());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterLayout() {
                showCacheInfo();
            }
        });

        createData(40, 1);

    }

    @Override
    public void onBind(final String text) {
        mTvBindLog.post(new Runnable() {
            @Override
            public void run() {
                mTvBindLog.setText(mTvBindLog.getText() + text);
                mSvBindLog.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    /**
     * 产生数据
     *
     * @param count     数据量
     * @param typeCount 数据种类
     */
    private void createData(int count, int typeCount) {

        mData.clear();
        for (int i = 0; i < count; ++i) {
//            int type = mRandom.nextInt(typeCount);
            int type = i >= 20 ? RecyclerBean.TYPE_FOUR : RecyclerBean.TYPE_ONE;
            switch (type) {
                case RecyclerBean.TYPE_ONE:
                    mData.add(new RecyclerBean<>(type, "A" + i));
                    break;
                case RecyclerBean.TYPE_TWO:
                    break;
                case RecyclerBean.TYPE_THREE:
                    break;
                case RecyclerBean.TYPE_FOUR:
                    mData.add(new RecyclerBean<>(type, "D" + i));
                    break;
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    /**
     * 显示缓存数据
     */
    private void showCacheInfo() {
        try {
            // 获取 Recycler
            Field recyclerField = Class.forName("android.support.v7.widget.RecyclerView")
                    .getDeclaredField("mRecycler");
            recyclerField.setAccessible(true);
            RecyclerView.Recycler recycler = (RecyclerView.Recycler) recyclerField.get(mRvWrapper);

            Class<? extends RecyclerView.Recycler> recyclerClass = recycler.getClass();
            // 获取 mAttachScrap 属性
            Field attachScrapFiled = recyclerClass.getDeclaredField("mAttachedScrap");
            // 获取 mChangedScrap 属性
            Field changeScrapField = recyclerClass.getDeclaredField("mChangedScrap");
            // 获取 mCachedViews 属性
            Field cacheViewField = recyclerClass.getDeclaredField("mCachedViews");
            // 获取 mRecyclerPool 属性
            Field recyclePoolField = recyclerClass.getDeclaredField("mRecyclerPool");
            // 获取 mViewCacheMax 属性
            Field viewCacheMaxField = recyclerClass.getDeclaredField("mViewCacheMax");

            // 设置为可以访问
            attachScrapFiled.setAccessible(true);
            changeScrapField.setAccessible(true);
            cacheViewField.setAccessible(true);
            recyclePoolField.setAccessible(true);
            viewCacheMaxField.setAccessible(true);

            // 获取 mAttachScrap 属性值
            ArrayListWrapper<RecyclerView.ViewHolder> attachedScrap =
                    (ArrayListWrapper<RecyclerView.ViewHolder>) attachScrapFiled.get(recycler);
            // 获取 mChangedScrap 属性值
            ArrayList<RecyclerView.ViewHolder> changeScrap =
                    (ArrayList<RecyclerView.ViewHolder>) changeScrapField.get(recycler);
            // 获取 mCachedViews 属性值
            ArrayList<RecyclerView.ViewHolder> cachedView =
                    (ArrayList<RecyclerView.ViewHolder>) cacheViewField.get(recycler);
            // 获取 mRecyclerPool 属性值
            RecyclerView.RecycledViewPool recycledViewPool =
                    (RecyclerView.RecycledViewPool) recyclePoolField.get(recycler);

            // 获取 mViewCacheMax 属性值
            int viewCacheSize = (int) viewCacheMaxField.get(recycler);

            // 获取 RecycleViewPool 中的 mScrap 属性
            Field scrapField = recycledViewPool.getClass().getDeclaredField("mScrap");
            scrapField.setAccessible(true);
            SparseArray poolScrap = (SparseArray) scrapField.get(recycledViewPool);

            String info = "mAttachedScrap（一缓）size is:" + attachedScrap.maxSize + "\n"
                    + "================\n"
                    + "mCachedViews（二缓） max size is:" + viewCacheSize + "\n"
                    + "================\n"
                    + getViewHolderArrInfo("mChangedScrap", changeScrap)
                    + getCacheViewTargetPositionInfo(mRvWrapper,
                    (LinearLayoutManager) mRvWrapper.getLayoutManager())
                    + getViewHolderArrInfo("mCachedViews(二缓)", cachedView)
                    + getRVPoolInfo(poolScrap);

            mTvCacheLog.setText(info);
            Log.i(TAG, "log\n" + info);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private String getViewHolderArrInfo(String typeName,
                                        ArrayList<RecyclerView.ViewHolder> viewHolders) {
        if (viewHolders != null) {

            StringBuilder result = new StringBuilder();

            result.append("[")
                    .append(typeName)
                    .append("]")
                    .append(" size is: ")
                    .append(viewHolders.size())
                    .append("\n");

            result.append("[")
                    .append(typeName)
                    .append("]")
                    .append("info: \n");

            if (viewHolders.size() == 0) {
                result.append("NO ViewHolders.\n");
                result.append("================\n");
                return result.toString();
            }

            for (int i = 0; i < viewHolders.size(); i++) {
                RecyclerView.ViewHolder item = viewHolders.get(i);
                result.append(typeName)
                        .append("[")
                        .append(i)
                        .append("]")
                        .append(item.toString())
                        .append("\n");
            }

            result.append("================\n");
            return result.toString();

        }
        return "";
    }

    private String getRVPoolInfo(SparseArray poolScrap) {
        StringBuilder result = new StringBuilder();

        result.append("mRecyclerPool(四缓) info: \n");
        for (int i = 0; i < poolScrap.size(); i++) {
            Object scrapDataItem = poolScrap.get(i);
            if(scrapDataItem == null){
                continue;
            }
            try {
                // 获取该 scrapData 的 mScrapHeap 和 mMaxScrap 属性
                Class<?> scrapDataClass = Class
                        .forName("android.support.v7.widget.RecyclerView$RecycledViewPool$ScrapData");
                Field scrapHeapField = scrapDataClass.getDeclaredField("mScrapHeap");
                Field maxScrapField = scrapDataClass.getDeclaredField("mMaxScrap");
                scrapHeapField.setAccessible(true);
                maxScrapField.setAccessible(true);

                ArrayList<RecyclerView.ViewHolder> item =
                        (ArrayList<RecyclerView.ViewHolder>) scrapHeapField.get(scrapDataItem);

                for (int j = 0; j < item.size(); j++) {
                    if (j == 0) {
                        result.append("----------第")
                                .append(j)
                                .append("个-----------\n");
                        result.append("mScrap[")
                                .append(i)
                                .append("] max size is:")
                                .append(maxScrapField.get(poolScrap.get(i)))
                                .append("\n");
                    } else if (j == item.size() - 1) {
                        result.append("-----------------------------\n");
                    }

                    result.append("mScrap[")
                            .append(i)
                            .append("] 中的 mScrapHeap[")
                            .append(j)
                            .append("] info is:")
                            .append(item.get(j))
                            .append("\n");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        result.append("================\n");

        return result.toString();
    }

    private String getCacheViewTargetPositionInfo(RecyclerView recycledView,
                                                  LinearLayoutManager layoutManager) {
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int lastItemPosition = layoutManager.findLastVisibleItemPosition();

        try {
            Field mLayoutStateField = layoutManager.getClass().getDeclaredField("mLayoutState");
            mLayoutStateField.setAccessible(true);
            Class<?> layoutStateClass =
                    Class.forName("android.support.v7.widget.LinearLayoutManager$LayoutState");
            Field mCurrentPositionField = layoutStateClass.getDeclaredField("mCurrentPosition");
            mCurrentPositionField.setAccessible(true);

            return "target mCachedView position:" +
                    mCurrentPositionField.get(mLayoutStateField.get(recycledView.getLayoutManager()))
                    + "\n"
                    + "firstItemPosition: " + firstItemPosition + "\n"
                    + "lastItemPosition: " + lastItemPosition + "\n"
                    + "================\n";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
