package com.zinc.xfermode.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zinc.xfermode.R;
import com.zinc.xfermode.widget.ScratchCardImageView;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/2/14
 * @description 刮刮卡
 */
public class ScratchCardActivity extends AppCompatActivity {

    ScratchCardImageView iv_scratch_card;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_card);

        iv_scratch_card = findViewById(R.id.iv_scratch_card);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iv_scratch_card.recycle();
    }

}
