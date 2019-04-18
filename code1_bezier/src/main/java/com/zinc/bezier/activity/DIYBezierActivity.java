package com.zinc.bezier.activity;

import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.zinc.bezier.widget.DIYBezierView;
import com.zinc.bezier.R;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/9
 * @description 带动画过程的贝塞尔
 */
public class DIYBezierActivity extends AppCompatActivity {

    private DIYBezierView diyBezierView;
    private RadioGroup rgStatus;
    private Switch showLineSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy_bezier);

        diyBezierView = findViewById(R.id.circle_bezier_view);
        rgStatus = findViewById(R.id.rg_status);
        showLineSwitch = findViewById(R.id.show_line_switch);

        diyBezierView.setIsShowHelpLine(true);
        showLineSwitch.setChecked(true);
        rgStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.status_free) {                // 自由变换
                    diyBezierView.setStatus(DIYBezierView.Status.FREE);
                } else if (checkedId == R.id.status_mirror_diff) {  // 镜像异向
                    diyBezierView.setStatus(DIYBezierView.Status.MIRROR_DIFF);
                } else if (checkedId == R.id.status_three) {        // 三点拽动
                    diyBezierView.setStatus(DIYBezierView.Status.THREE);
                } else if (checkedId == R.id.status_mirror_same) {  // 镜像同向
                    diyBezierView.setStatus(DIYBezierView.Status.MIRROR_SAME);
                }
            }
        });

        showLineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                diyBezierView.setIsShowHelpLine(isChecked);
            }
        });

    }

    public void onReset(View view) {
        diyBezierView.reset();
    }

    public void onLog(View view) {
        List<PointF> controlPointList = diyBezierView.getControlPointList();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        for (int i = 0; i < controlPointList.size(); ++i) {
            stringBuilder.append("第")
                    .append(i)
                    .append("个点坐标(单位dp)：[")
                    .append(px2dip(this, controlPointList.get(i).x))
                    .append(", ")
                    .append(px2dip(this, controlPointList.get(i).y))
                    .append("]")
                    .append("\n");
        }

        Log.i("DIY Bezier", "控制点日志: " + stringBuilder.toString());

    }

    public int px2dip(Context context, float pxValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }
}
