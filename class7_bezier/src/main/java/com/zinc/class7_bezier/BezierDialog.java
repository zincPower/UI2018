package com.zinc.class7_bezier;

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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/2
 * @description
 */
public class BezierDialog extends AppCompatDialogFragment {

    private Animation dismissAnim;

    private RelativeLayout rlBackground;
    private LinearLayout llMenu;
    private Switch reduceSwitch;
    private Switch loopSwitch;
    private SeekBar orderSeekbar;
    private Switch pathSwitch;
    private SeekBar rateSeekbar;

    private boolean isDismissing = false;

    public static BezierDialog getInstance() {
        Bundle bundle = new Bundle();

        BezierDialog fragment = new BezierDialog();
        fragment.setArguments(bundle);
        return fragment;
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

        rlBackground = (RelativeLayout) view.findViewById(R.id.rl_background);
        llMenu = (LinearLayout) view.findViewById(R.id.ll_menu);
        reduceSwitch = (Switch) view.findViewById(R.id.reduce_switch);
        loopSwitch = (Switch) view.findViewById(R.id.loop_switch);
        orderSeekbar = (SeekBar) view.findViewById(R.id.order_seekbar);
        pathSwitch = (Switch) view.findViewById(R.id.path_switch);
        rateSeekbar = (SeekBar) view.findViewById(R.id.rate_seekbar);

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
