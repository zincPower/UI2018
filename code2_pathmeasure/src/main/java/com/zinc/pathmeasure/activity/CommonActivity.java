package com.zinc.pathmeasure.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zinc.pathmeasure.GetLengthView;
import com.zinc.pathmeasure.NextCounterView;
import com.zinc.pathmeasure.LoadingView;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/4
 * @description
 */
public class CommonActivity extends AppCompatActivity {

    public static final String TYPE = "type";

    public static final String ABC = "ABC";
    public static final String LOADING = "LOADING";
    public static final String GET_LENGTH = "GET_LENGTH";

    private String mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mType = getIntent().getStringExtra(TYPE);

        switch (mType) {
            case ABC:
                setContentView(new NextCounterView(this));
                break;
            case LOADING:
                setContentView(new LoadingView(this));
                break;
            case GET_LENGTH:
                setContentView(new GetLengthView(this));
                break;
        }

    }
}
