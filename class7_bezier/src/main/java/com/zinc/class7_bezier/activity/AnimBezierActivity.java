package com.zinc.class7_bezier.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zinc.class7_bezier.AnimBezierView;
import com.zinc.class7_bezier.CircleBezierView;
import com.zinc.class7_bezier.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/9
 * @description 带动画过程的贝塞尔
 */
public class AnimBezierActivity extends AppCompatActivity {

    private AnimBezierView animBezierView;
    private RadioGroup rgStatus;

    private RadioGroup rgAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_bezier);

        animBezierView = findViewById(R.id.circle_bezier_view);
        rgStatus = findViewById(R.id.rg_status);

        rgAnim = findViewById(R.id.rg_anim);

        rgStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.status_free) {    // 自由变换
                    animBezierView.setStatus(AnimBezierView.Status.FREE);
                } else if (checkedId == R.id.status_mirror) { // 镜像变动
                    animBezierView.setStatus(AnimBezierView.Status.MIRROR);
                } else if (checkedId == R.id.status_three) {   // 三点拽动
                    animBezierView.setStatus(AnimBezierView.Status.THREE);
                }
            }
        });

        rgAnim.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.heart) {

                } else if (checkedId == R.id.square) {
                } else if (checkedId == R.id.tadpole) {
                }
            }
        });

    }

    public void onRunAnim(View view) {

    }
}
