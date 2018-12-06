package com.zinc.class8_pathmeasure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/5
 * @description
 */
public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PlaneLoadingView(this));
    }

    public static void main(String[] args) {

        System.out.println(Math.atan2(45, 30));

    }
}
