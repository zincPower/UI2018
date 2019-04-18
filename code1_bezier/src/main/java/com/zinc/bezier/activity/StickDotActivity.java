package com.zinc.bezier.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zinc.bezier.R;
import com.zinc.bezier.widget.StickDotView;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/11
 * @description
 */
public class StickDotActivity extends AppCompatActivity {

    private StickDotView stickDotView;
    private TextView btnReset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_dot);

        stickDotView = findViewById(R.id.stick_dot_view);
        btnReset = findViewById(R.id.btn_reset);

        stickDotView.setText("99+");
        stickDotView.setOnDragListener(new StickDotView.onDragStatusListener() {
            @Override
            public void onDrag() {
            }

            @Override
            public void onMove() {

            }

            @Override
            public void onRestore() {

            }

            @Override
            public void onDismiss() {
                stickDotView.setVisibility(View.GONE);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickDotView.setVisibility(View.VISIBLE);
            }
        });

    }
}
