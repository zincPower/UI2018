package com.zinc.class4_xfermode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zinc.class4_xfermode.activity.GoogleXFerModeActivity;
import com.zinc.class4_xfermode.activity.HeartActivity;
import com.zinc.class4_xfermode.activity.ScratchCardActivity;
import com.zinc.class4_xfermode.activity.ZincXFerModeActivity;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/22
 * @description
 */
public class ClientActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lsn4_activity_client);

        findViewById(R.id.btn_google).setOnClickListener(this);
        findViewById(R.id.btn_zinc).setOnClickListener(this);
        findViewById(R.id.btn_gua_gua).setOnClickListener(this);
        findViewById(R.id.btn_heart).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_google) {
            startActivity(new Intent(this, GoogleXFerModeActivity.class));
        } else if (i == R.id.btn_zinc) {
            startActivity(new Intent(this, ZincXFerModeActivity.class));
        } else if (i == R.id.btn_gua_gua) {
            startActivity(new Intent(this, ScratchCardActivity.class));
        } else if (i == R.id.btn_heart) {
            startActivity(new Intent(this, HeartActivity.class));
        }
    }
}
