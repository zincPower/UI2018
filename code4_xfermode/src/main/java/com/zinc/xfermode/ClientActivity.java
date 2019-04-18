package com.zinc.xfermode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zinc.xfermode.activity.AddAndOverlayActivity;
import com.zinc.xfermode.activity.GoogleXFerModeActivity;
import com.zinc.xfermode.activity.HeartActivity;
import com.zinc.xfermode.activity.ScratchCardActivity;
import com.zinc.xfermode.activity.ZincXFerModeActivity;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/22
 * @description
 */
public class ClientActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfermode_client);

        findViewById(R.id.btn_google).setOnClickListener(this);
        findViewById(R.id.btn_zinc).setOnClickListener(this);
        findViewById(R.id.btn_gua_gua).setOnClickListener(this);
        findViewById(R.id.btn_heart).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_overlay).setOnClickListener(this);
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
        } else if (i == R.id.btn_add) {
            Intent intent = new Intent(this, AddAndOverlayActivity.class);
            intent.putExtra(AddAndOverlayActivity.TYPE, AddAndOverlayActivity.ADD);
            startActivity(intent);
        } else if (i == R.id.btn_overlay) {
            Intent intent = new Intent(this, AddAndOverlayActivity.class);
            intent.putExtra(AddAndOverlayActivity.TYPE, AddAndOverlayActivity.OVERLAY);
            startActivity(intent);
        }
    }
}
