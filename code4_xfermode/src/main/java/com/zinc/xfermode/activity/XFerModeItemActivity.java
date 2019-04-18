package com.zinc.xfermode.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zinc.xfermode.R;
import com.zinc.xfermode.fragment.XFerModeFragment;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/26
 * @description
 */
public class XFerModeItemActivity extends AppCompatActivity {

    public static final String INDEX = "xfermode";
    public static final String DST = "dst";
    public static final String SRC = "src";
    public static final String SHOW_INDEX = "showIndex";

    private int dst;
    private int src;

    private ViewPager viewPager;

    private int showIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfermode_item);

        dst = getIntent().getIntExtra(DST, 0);
        src = getIntent().getIntExtra(SRC, 0);
        showIndex = getIntent().getIntExtra(SHOW_INDEX, 0);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new XFerModeViewPagerAdapter(getSupportFragmentManager(), dst, src));
        viewPager.setCurrentItem(showIndex);
        viewPager.setOffscreenPageLimit(3);
    }

    public static class XFerModeViewPagerAdapter extends FragmentPagerAdapter {

        private int dst;
        private int src;

        public XFerModeViewPagerAdapter(FragmentManager fm, int dst, int src) {
            super(fm);
            this.dst = dst;
            this.src = src;
        }

        @Override
        public Fragment getItem(int position) {
            return XFerModeFragment.getInstance(position, dst, src);
        }

        @Override
        public int getCount() {
            return 16;
        }
    }

}