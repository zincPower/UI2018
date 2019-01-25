package com.zinc.class12_animation.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;

import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zinc.class12_animation.R;
import com.zinc.class12_animation.adapter.TimeInterpolatorAdapter;
import com.zinc.class12_animation.bean.TimeInterpolatorBean;
import com.zinc.class12_animation.widget.TimeInterpolatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/24
 * @description
 */
public class TimeInterpolatorActivity extends AppCompatActivity implements TimeInterpolatorAdapter.ClickListener {
    // 取1000帧
    private static final int FRAME = 1000;

    private TimeInterpolatorView mTimeInterpolatorView;

    private final List<PointF> dataList = new ArrayList<>();
    private final List<TimeInterpolatorBean> interpolatorList = new ArrayList<>();

    private TimeInterpolator mInterpolator;

    private TextView tvRun;
    private View animView;
    private RecyclerView recycleView;
    private TextView tvDuration;
    private TextView tvStateInfo;

    private TimeInterpolatorAdapter mAdapter;

    private ObjectAnimator mAnimator;

    private boolean isRunning;

    private View getStartView() {
        return (View) findViewById(R.id.start_view);
    }

    private View getAnimView() {
        return (View) findViewById(R.id.anim_view);
    }

    private View getEndView() {
        return (View) findViewById(R.id.end_view);
    }

    private EditText getEtDuration() {
        return (EditText) findViewById(R.id.et_duration);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_interpolator);

        mTimeInterpolatorView = findViewById(R.id.time_interpolator_view);
        tvRun = findViewById(R.id.tv_run);
        animView = findViewById(R.id.anim_view);
        recycleView = findViewById(R.id.recycle_view);
        tvStateInfo = findViewById(R.id.tv_state_info);

        isRunning = false;

        buildInterpolatorList();
        createData();

        mAnimator = ObjectAnimator.ofFloat(animView, "y", dpToPx(this, 35),
                getScreenHeight(this) - dpToPx(this, 35 + 50) - getStatusHeight(this));
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                updateState(false);
            }
        });

        mTimeInterpolatorView.setLineData(dataList);

        tvRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    Toast.makeText(TimeInterpolatorActivity.this, "动画正在进行中，请稍等", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateState(true);
                String durationString = getEtDuration().getText().toString();
                long duration = TextUtils.isEmpty(durationString) ? 2000L : Long.parseLong(durationString);
                mAnimator.setDuration(duration);
                mAnimator.setInterpolator(mInterpolator);
                mAnimator.start();
            }
        });

        mAdapter = new TimeInterpolatorAdapter(this, interpolatorList);
        mAdapter.setListener(this);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(mAdapter);

    }

    private void createData() {
        dataList.clear();
        for (float x = 0; x <= 1; x += 1.0f / FRAME) {
            float y = mInterpolator.getInterpolation(x);

            PointF pointF = new PointF(x, y);
            dataList.add(pointF);
        }
    }

    private void buildInterpolatorList() {
        interpolatorList.clear();
        interpolatorList.add(new TimeInterpolatorBean(true, "AccelerateDecelerateInterpolator", new AccelerateDecelerateInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "AccelerateInterpolator", new AccelerateInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "AnticipateInterpolator", new AnticipateInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "AnticipateOvershootInterpolator", new AnticipateOvershootInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "BounceInterpolator", new BounceInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "CycleInterpolator(1)", new CycleInterpolator(1)));
        interpolatorList.add(new TimeInterpolatorBean(false, "DecelerateInterpolator", new DecelerateInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "LinearInterpolator", new LinearInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "OvershootInterpolator", new OvershootInterpolator()));

        mInterpolator = interpolatorList.get(0).getTimeInterpolator();

    }

    public static float getScreenHeight(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static float dpToPx(Context context, float dipValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return dipValue * density + 0.5f;
    }

    public static float getStatusHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onTimeInterpolatorClick(int position) {
        for (TimeInterpolatorBean bean : interpolatorList) {
            bean.setSelect(false);
        }

        interpolatorList.get(position).setSelect(true);

        mInterpolator = interpolatorList.get(position).getTimeInterpolator();

        mAdapter.notifyDataSetChanged();
    }

    private void updateState(boolean isRunning) {
        this.isRunning = isRunning;
        tvStateInfo.setText(isRunning ? "running" : "ready");
        tvStateInfo.setTextColor(isRunning ?
                Color.parseColor("#00FF7F") :
                Color.parseColor("#1E90FF"));
    }

}
