package com.zinc.animation.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.zinc.animation.R;
import com.zinc.animation.widget.RadarChartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/16
 * @description
 */
public class RadarActivity extends AppCompatActivity {

    // 数据最大数
    private int DATA_MAX = 4;
    // 平均数最大数
    private int BASE_MAX = 3;
    // 维度最大数
    private int DIMEN_MAX = 9;

    // 基线数据
    private int baseDataCount;
    // 比较数据
    private int dataCount;

    // 维度
    private int dimenCount;

    private List<String> textDataList = new ArrayList<>();

    private RadarChartView radarChartView;

    private boolean isShowText;

    private SeekBar dataSeekBar;
    private SeekBar baseSeekBar;
    private SeekBar dimenSeekBar;

    private Switch swTextDes;

    private TextView tvDataNum;
    private TextView tvBaseNum;
    private TextView tvDimenNum;

    private static final List<Integer> BASE_COLOR = new ArrayList<>();
    private static final List<Integer> DATA_COLOR = new ArrayList<>();

    private final List<RadarChartView.Data> baseDataList = new ArrayList<>();
    private final List<RadarChartView.Data> dataList = new ArrayList<>();

    static {
        BASE_COLOR.add(Color.parseColor("#00BFFF"));    // 蓝色
        BASE_COLOR.add(Color.parseColor("#FFA500"));    // 橙色
        BASE_COLOR.add(Color.parseColor("#FFB6C1"));    // 粉色

        DATA_COLOR.add(Color.parseColor("#DC143C"));    // 红色
        DATA_COLOR.add(Color.parseColor("#FFD700"));    // 黄色
        DATA_COLOR.add(Color.parseColor("#00FF7F"));    // 绿色
        DATA_COLOR.add(Color.parseColor("#9932CC"));    // 紫色

    }

    private Random random = new Random();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);

        radarChartView = findViewById(R.id.radar_chart_view);

        dataSeekBar = findViewById(R.id.data_seek_bar);
        baseSeekBar = findViewById(R.id.base_seek_bar);
        dimenSeekBar = findViewById(R.id.dimen_seek_bar);

        tvDataNum = findViewById(R.id.tv_data_num);
        tvBaseNum = findViewById(R.id.tv_base_num);
        tvDimenNum = findViewById(R.id.tv_dimen_num);

        swTextDes = findViewById(R.id.sw_text_des);

        baseDataCount = 1;
        dataCount = 2;
        dimenCount = 6;

        dataSeekBar.setMax(DATA_MAX);
        baseSeekBar.setMax(BASE_MAX);
        dimenSeekBar.setMax(DIMEN_MAX);

        dataSeekBar.setProgress(dataCount);
        baseSeekBar.setProgress(baseDataCount);
        dimenSeekBar.setProgress(dimenCount);
        tvDataNum.setText(String.format(getString(R.string.data_num), dataCount));
        tvBaseNum.setText(String.format(getString(R.string.base_num), baseDataCount));
        tvDimenNum.setText(String.format(getString(R.string.dimen_num), dimenCount));

        dataSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dataCount = progress;
                tvDataNum.setText(String.format(getString(R.string.data_num), progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        baseSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                baseDataCount = progress;
                tvBaseNum.setText(String.format(getString(R.string.base_num), progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dimenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dimenCount = progress + 3;
                tvDimenNum.setText(String.format(getString(R.string.dimen_num), dimenCount));
                radarChartView.setDimenCount(dimenCount);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        isShowText = true;
        swTextDes.setChecked(isShowText);
        swTextDes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShowText = isChecked;
            }
        });

    }

    private void createData(List<RadarChartView.Data> dataList,
                            int count,
                            int dimen,
                            List<Integer> colorList) {
        dataList.clear();

        for (int i = 0; i < count; ++i) {
            List<Float> list = new ArrayList<>();

            for (int j = 0; j < dimen; ++j) {
                list.add(random.nextFloat());
            }

            RadarChartView.Data data = new RadarChartView.Data(list, colorList.get(i % colorList.size()));

            dataList.add(data);
        }

    }

    public void onRun(View view) {
        createData(dataList, dataCount, dimenCount, DATA_COLOR);
        createData(baseDataList, baseDataCount, dimenCount, BASE_COLOR);

        if (isShowText) {
            createTextData();
            radarChartView.setTextDataList(textDataList);
        } else {
            radarChartView.setTextDataList(new ArrayList<String>());
        }
        radarChartView.setBaseDataList(baseDataList);
        radarChartView.setDataList(dataList);
        radarChartView.start();
    }

    public void onStop(View view) {
        radarChartView.stop();
    }

    public void onReset(View view) {
        radarChartView.reset();
    }

    private void createTextData() {
        textDataList.clear();
        for (int i = 0; i < dimenCount; ++i) {
            textDataList.add("第" + i + "维");
        }
    }
}
