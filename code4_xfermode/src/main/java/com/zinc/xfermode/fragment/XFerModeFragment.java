package com.zinc.xfermode.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zinc.xfermode.R;
import com.zinc.xfermode.activity.XFerModeItemActivity;
import com.zinc.xfermode.widget.ItemXFerModeView;
import com.zinc.xfermode.widget.ZincXFerModeView;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/29
 * @description
 */
public class XFerModeFragment extends Fragment {

    private int index;
    private Bitmap dstBitmap;
    private Bitmap srcBitmap;

    private TextView tvLabel;
    private ItemXFerModeView xFerModeView;

    public static XFerModeFragment getInstance(int index,
                                               int dst,
                                               int src) {
        XFerModeFragment fragment = new XFerModeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(XFerModeItemActivity.INDEX, index);
        bundle.putInt(XFerModeItemActivity.DST, dst);
        bundle.putInt(XFerModeItemActivity.SRC, src);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_xfermode_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvLabel = view.findViewById(R.id.tv_label);
        xFerModeView = view.findViewById(R.id.xfermode_view);

        index = getArguments().getInt(XFerModeItemActivity.INDEX);
        dstBitmap = BitmapFactory.decodeResource(this.getResources(),
                getArguments().getInt(XFerModeItemActivity.DST));
        srcBitmap = BitmapFactory.decodeResource(this.getResources(),
                getArguments().getInt(XFerModeItemActivity.SRC));

        tvLabel.setText(ZincXFerModeView.sLabels[index]);
        xFerModeView.setCurXfermode(ZincXFerModeView.sModes[index]);
        xFerModeView.setBitmap(dstBitmap, srcBitmap);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dstBitmap.recycle();
        srcBitmap.recycle();
    }

}
