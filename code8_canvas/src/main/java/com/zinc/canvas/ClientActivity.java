package com.zinc.canvas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zinc.canvas.widget.draw.ArcView;
import com.zinc.canvas.widget.draw.LineView;
import com.zinc.canvas.widget.draw.OvalView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/29
 * @description
 */
public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new OvalView(this));
    }

}
