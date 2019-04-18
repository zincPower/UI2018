package com.zinc.bezier.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.zinc.bezier.R;
import com.zinc.bezier.activity.BezierActivity;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/2
 * @description
 */
public class BezierDialog extends AppCompatDialogFragment {

    private Animation dismissAnim;

    // 阶级最大值
    private static final int ORDER_MAX = 7;
    // 速率最大值
    private static final int RATE_MAX = 5;
    // 速率的间隔
    private static final int RATE_INTERVAL = 5;

    private RelativeLayout rlBackground;
    private LinearLayout llMenu;
    private Switch reduceSwitch;
    private Switch loopSwitch;
    private SeekBar orderSeekbar;
    private SeekBar rateSeekbar;
    private TextView tvOrder;
    private TextView tvRate;

    private boolean isDismissing = false;

    // 是否显示降阶线
    private boolean isShowReduceOrderLine;
    // 是否循环播放
    private boolean isLoopPlay;
    // 阶数
    private int order;
    // 速率
    private int rate;

    public static BezierDialog getInstance() {
        Bundle bundle = new Bundle();

        BezierDialog fragment = new BezierDialog();
        fragment.setArguments(bundle);

        return fragment;
    }

    public void setShowReduceOrderLine(boolean showReduceOrderLine) {
        this.isShowReduceOrderLine = showReduceOrderLine;
    }

    public void setLoopPlay(boolean loopPlay) {
        this.isLoopPlay = loopPlay;
    }

    public void setOrder(int order) {
        if (order > ORDER_MAX) {
            order = ORDER_MAX;
        } else if (order < 1) {
            order = 1;
        }

        this.order = order - 1;
    }

    public void setRate(int rate) {
        rate = rate / RATE_INTERVAL;
        if (rate > RATE_MAX) {
            rate = RATE_MAX;
        } else if (rate < 1) {
            rate = 1;
        }

        this.rate = rate - 1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TranslucentNoTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_bezier_setting, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rlBackground = view.findViewById(R.id.rl_background);
        llMenu = view.findViewById(R.id.ll_menu);
        reduceSwitch = view.findViewById(R.id.reduce_switch);
        loopSwitch = view.findViewById(R.id.loop_switch);
        orderSeekbar = view.findViewById(R.id.order_seekbar);
        rateSeekbar = view.findViewById(R.id.rate_seekbar);
        tvOrder = view.findViewById(R.id.tv_order);
        tvRate = view.findViewById(R.id.tv_rate);

        reduceSwitch.setChecked(isShowReduceOrderLine);
        loopSwitch.setChecked(isLoopPlay);

        orderSeekbar.setProgress(order);
        tvOrder.setText(getString(R.string.order, order + 1));

        rateSeekbar.setProgress(rate);
        tvRate.setText(getString(R.string.rate, (rate + 1) * RATE_INTERVAL));

        // 设置阶数最大值
        orderSeekbar.setMax(ORDER_MAX - 1);

        // 设置速率最大值
        rateSeekbar.setMax(RATE_MAX - 1);

        this.getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        llMenu.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dialog_show_anim));

        dismissAnim = AnimationUtils.loadAnimation(getContext(), R.anim.dialog_dismiss_anim);
        dismissAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                BezierDialog.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                dismiss();
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        orderSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (getActivity() == null || !(getActivity() instanceof BezierActivity)) {
                    return;
                }

                tvOrder.setText(getString(R.string.order, progress + 1));
                BezierActivity bezierActivity = (BezierActivity) getActivity();
                bezierActivity.setOrder(progress + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rateSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (getActivity() == null || !(getActivity() instanceof BezierActivity)) {
                    return;
                }

                int result = (progress + 1) * RATE_INTERVAL;
                tvRate.setText(getString(R.string.rate, result));
                BezierActivity bezierActivity = (BezierActivity) getActivity();
                bezierActivity.setRate(result);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        loopSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (getActivity() == null || !(getActivity() instanceof BezierActivity)) {
                    return;
                }

                BezierActivity bezierActivity = (BezierActivity) getActivity();
                bezierActivity.setLoopPlay(isChecked);
            }
        });

        reduceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (getActivity() == null || !(getActivity() instanceof BezierActivity)) {
                    return;
                }

                BezierActivity bezierActivity = (BezierActivity) getActivity();
                bezierActivity.setShowReduceOrderLine(isChecked);
            }
        });

        rlBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        isDismissing = false;

    }

    @Override
    public void dismiss() {
        if (isDismissing) {
            return;
        }

        isDismissing = true;
        llMenu.startAnimation(dismissAnim);
    }

}
