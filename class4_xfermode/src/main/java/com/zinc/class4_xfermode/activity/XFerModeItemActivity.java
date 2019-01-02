package com.zinc.class4_xfermode.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zinc.class4_xfermode.R;
import com.zinc.class4_xfermode.widget.ItemXFerModeView;
import com.zinc.class4_xfermode.widget.ZincXFerModeView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/26
 * @description
 */
public class XFerModeItemActivity extends AppCompatActivity {

    public static final String INDEX = "xfermode";

    private int index;

    private TextView tvLabel;
    private ItemXFerModeView xFerModeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lsn4_activity_item);

        tvLabel = findViewById(R.id.tv_label);
        xFerModeView = findViewById(R.id.xfermode_view);

        index = getIntent().getIntExtra(INDEX, 0);

        tvLabel.setText(ZincXFerModeView.sLabels[index]);
        xFerModeView.setCurXfermode(ZincXFerModeView.sModes[index]);

    }
}