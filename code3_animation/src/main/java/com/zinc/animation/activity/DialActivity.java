package com.zinc.animation.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zinc.animation.R;
import com.zinc.animation.widget.DialView;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/19
 * @description
 */
public class DialActivity extends AppCompatActivity {

    // 最多五段
    private static final int LINE_MAX = 5;

    private DialView dialView;

    private TextView tvLevel;
    private SeekBar sbLevel;
    private TextView tvLevelNum;
    private SeekBar sbLevelNum;

    private int lineCount = 3;
    private int curCount = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial);
        dialView = findViewById(R.id.dial_view);

        tvLevel = findViewById(R.id.tv_level);
        sbLevel = findViewById(R.id.sb_level);
        tvLevelNum = findViewById(R.id.tv_level_num);
        sbLevelNum = findViewById(R.id.sb_level_num);

        setLevelTv(lineCount);
        setCurLevelNumTv(curCount);
        dialView.setValue(curCount);
        dialView.setLineCount(lineCount);

        sbLevel.setMax(LINE_MAX);
        sbLevelNum.setMax(lineCount);

        sbLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lineCount = progress+2;

                setLevelTv(lineCount);
                dialView.reset();
                dialView.setLineCount(lineCount);

                sbLevelNum.setMax(lineCount-1);

                int oldMax = sbLevelNum.getMax();
                if (oldMax > lineCount) {
                    dialView.setValue(lineCount);

                    sbLevelNum.setProgress(lineCount-1);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbLevelNum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                curCount = progress + 1;
                dialView.setValue(curCount);
                setCurLevelNumTv(curCount);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void onRun(View view) {
        dialView.start();
    }

    public void onReset(View view) {
        dialView.reset();
    }

    private void setLevelTv(int num) {
        tvLevel.setText(String.format(getString(R.string.level_num), num));
    }

    private void setCurLevelNumTv(int num) {
        tvLevelNum.setText(String.format(getString(R.string.cur_level_num), num));
    }

}
