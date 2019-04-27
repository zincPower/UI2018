package com.zinc.code8_canvas_clip.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zinc.code8_canvas_clip.widget.ClipOpView;
import com.zinc.code8_canvas_clip.R;

/**
 * author       : zinc
 * time         : 2019/4/26 下午12:36
 * desc         :
 * version      :
 */
public class ClipOpListActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_list);
    }

    public void onDifference(View view) {
        goToClipView(ClipOpView.DIFFERENCE);
    }

    public void onIntersect(View view) {
        goToClipView(ClipOpView.INTERSECT);
    }

    public void onUnion(View view) {
        goToClipView(ClipOpView.UNION);
    }

    public void onXor(View view) {
        goToClipView(ClipOpView.XOR);
    }

    public void onReverseDifference(View view) {
        goToClipView(ClipOpView.REVERSE_DIFFERENCE);
    }

    public void onReplace(View view) {
        goToClipView(ClipOpView.REPLACE);
    }

    private void goToClipView(String type) {
        Intent intent = new Intent(this, ClipOpContentActivity.class);
        intent.putExtra(ClipOpContentActivity.CLIP, type);

        startActivity(intent);
    }

}
