package com.zinc.xfermode.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.zinc.xfermode.R;
import com.zinc.xfermode.XFerModeInfo;
import com.zinc.xfermode.adapter.XFerModeAdapter;
import com.zinc.xfermode.bean.XFerModeBean;
import com.zinc.xfermode.widget.ZincXFerModeView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/22
 * @description
 */
public class ZincXFerModeActivity extends AppCompatActivity implements XFerModeInfo, XFerModeAdapter.XFerModeListener {

    private ImageView ivDst;
    private ImageView ivSrc;
    private RecyclerView recycleView;
    private ZincXFerModeView xfermodeView;

    private XFerModeAdapter mAdapter;

    private final List<XFerModeBean> xfermodeList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zinc_xfermode);

        createXFerModeData();

        ivDst = findViewById(R.id.iv_dst);
        ivSrc = findViewById(R.id.iv_src);
        recycleView = findViewById(R.id.recycle_view);
        xfermodeView = findViewById(R.id.xfermode_view);

        initView();

    }

    private void initView() {
        mAdapter = new XFerModeAdapter(this, xfermodeList);
        mAdapter.setListener(this);
        recycleView.setAdapter(mAdapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        XFerModeBean selXFerModeBean = getSelXFerModeBean();
        ivDst.setImageBitmap(selXFerModeBean.getDstBitmap());
        ivSrc.setImageBitmap(selXFerModeBean.getSrcBitmap());

        xfermodeView.setBitmap(
                selXFerModeBean.getDst(),
                selXFerModeBean.getSrc(),
                selXFerModeBean.getDstBitmap(),
                selXFerModeBean.getSrcBitmap());

    }

    private void createXFerModeData() {
        xfermodeList.add(new XFerModeBean(this,
                "zinc",
                true,
                R.drawable.dst,
                R.drawable.src));
        xfermodeList.add(new XFerModeBean(this,
                "monkey",
                false,
                R.drawable.monkey,
                R.drawable.white_shadow_to_alpha
        ));

        xfermodeList.add(new XFerModeBean(this,
                "spider",
                false,
                R.drawable.spider_black,
                R.drawable.spider
        ));

        xfermodeList.add(new XFerModeBean(this,
                "spider_circle",
                false,
                R.drawable.spider_black,
                R.drawable.circle
        ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (XFerModeBean xFerModeBean : xfermodeList) {
            xFerModeBean.recycle();
        }

        xfermodeList.clear();
    }

    @Override
    public XFerModeBean getSelXFerModeBean() {

        for (XFerModeBean bean : xfermodeList) {
            if (bean.isSelect()) {
                return bean;
            }
        }

        return null;
    }

    @Override
    public void onClickItem(int position) {
        for (int pos = 0; pos < xfermodeList.size(); ++pos) {
            XFerModeBean item = xfermodeList.get(pos);
            if (pos == position) {
                item.setSelect(true);
                ivDst.setImageBitmap(item.getDstBitmap());
                ivSrc.setImageBitmap(item.getSrcBitmap());
                xfermodeView.setBitmap(item.getDst(),
                        item.getSrc(),
                        item.getDstBitmap(),
                        item.getSrcBitmap());
            } else {
                item.setSelect(false);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
