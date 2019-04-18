package com.zinc.flowlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClientActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int DATA_INTERVAL = 2;

    private static final List<String> INFO = new ArrayList<>();

    static {
        INFO.add("Hello World!");
        INFO.add("猛猛的小盆友");
        INFO.add("掘");
        INFO.add("金金金");
        INFO.add("PHP是最好的语言");
        INFO.add("大Android");
        INFO.add("IOS");
        INFO.add("JAVA");
        INFO.add("Python");
        INFO.add("这是一个很长很长很长很长很长很长很长很长超级无敌长的句子");
    }

    private TagFlowLayout tagFlowLayout;
    private TextView tvDataCount;
    private SeekBar sbDataCount;
    private Switch swShowComplexLayout;

    private Random mRandom = new Random();

    private int mDataCount = 10;

    private int mTypeCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowlayout_client);

        tagFlowLayout = findViewById(R.id.tag_flow_layout);
        tvDataCount = findViewById(R.id.tv_data_count);
        sbDataCount = findViewById(R.id.sb_data_count);
        swShowComplexLayout = findViewById(R.id.sw_show_complex_layout);

        tvDataCount.setText(String.format(getString(R.string.data_count), mDataCount));

        swShowComplexLayout.setChecked(false);
        swShowComplexLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTypeCount = isChecked ? 2 : 1;
            }
        });

        sbDataCount.setProgress(mDataCount / DATA_INTERVAL);
        sbDataCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDataCount = progress * DATA_INTERVAL;
                tvDataCount.setText(String.format(getString(R.string.data_count), mDataCount));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.tv_play).setOnClickListener(this);

        createData();

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_play) {
            createData();
        }
    }

    /**
     * 创建view
     */
    private void createData() {

        // 先清除之前的view
        tagFlowLayout.removeAllViews();

        // 循环添加view
        for (int i = 0; i < mDataCount; ++i) {
            int type = mRandom.nextInt(mTypeCount);
            if (type == 0) {
                createSingleView();
            } else {
                createComplexView();
            }
        }


        // 刷新
        tagFlowLayout.invalidate();

    }

    private void createSingleView() {
        int index = mRandom.nextInt(INFO.size());
        final String itemContent = INFO.get(index);

        TextView item = (TextView) getLayoutInflater()
                .inflate(R.layout.item_single_tag, tagFlowLayout, false);
        item.setText(itemContent);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClientActivity.this, "text:" + itemContent,
                        Toast.LENGTH_SHORT).show();
            }
        });

        tagFlowLayout.addView(item);
    }

    private void createComplexView() {
        int index = mRandom.nextInt(INFO.size());
        final String itemContent = INFO.get(index);

        ViewGroup group = (ViewGroup) getLayoutInflater()
                .inflate(R.layout.item_complex_tag, tagFlowLayout, false);

        TextView tvInfo = group.findViewById(R.id.tv_info);
        tvInfo.setText(itemContent.length() > 0 ?
                itemContent.substring(0, 1) :
                "");

        TextView button = group.findViewById(R.id.tv_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClientActivity.this, "text:" + itemContent,
                        Toast.LENGTH_SHORT).show();
            }
        });

        tagFlowLayout.addView(group);
    }

}
