package com.zinc.pathmeasure.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zinc.pathmeasure.GetSegmentView;
import com.zinc.pathmeasure.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/4
 * @description
 */
public class GetSegmentActivity extends AppCompatActivity {

    private GetSegmentView mGetSegmentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_segment);
        mGetSegmentView = findViewById(R.id.get_segment_view);
    }

    public void onSetTrue(View view) {
        mGetSegmentView.setStartWithMoveTo(true);
    }

    public void onSetFalse(View view) {
        mGetSegmentView.setStartWithMoveTo(false);
    }
}
