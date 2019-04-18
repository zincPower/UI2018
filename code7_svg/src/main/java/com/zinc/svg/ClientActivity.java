package com.zinc.svg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * author       : zinc
 * time         : 2019/4/5 下午4:28
 * desc         :
 * version      :
 */
public class ClientActivity extends Activity implements View.OnClickListener {

    private TextView tvSvgUse;
    private TextView tvSvgJueJin;
    private TextView tvSvgMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_client);

        tvSvgUse = findViewById(R.id.tv_svg_use);
        tvSvgJueJin = findViewById(R.id.tv_svg_jue_jin);
        tvSvgMap = findViewById(R.id.tv_svg_map);

        tvSvgUse.setOnClickListener(this);
        tvSvgJueJin.setOnClickListener(this);
        tvSvgMap.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        int i = v.getId();
        if (i == R.id.tv_svg_use) {
            intent = new Intent(this, SvgUseActivity.class);
        } else if (i == R.id.tv_svg_jue_jin) {
            intent = new Intent(this, JueJinAnimActivity.class);
        } else if (i == R.id.tv_svg_map) {
            intent = new Intent(this, SvgMapActivity.class);
        }

        if (intent == null) {
            return;
        }

        startActivity(intent);
    }
}
