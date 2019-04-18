package com.zinc.xfermode.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zinc.xfermode.R;
import com.zinc.xfermode.widget.ItemXFerModeView;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/2/19
 * @description
 */
public class AddAndOverlayActivity extends AppCompatActivity {

    public static final String ADD = "Add";
    public static final String OVERLAY = "Overlay";

    public static final String TYPE = "type";

    private Xfermode xfermode;
    private String title;

    private TextView tvLabel;
    private ItemXFerModeView xfermodeView;

    private Bitmap dstBitmap;
    private Bitmap srcBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_overlay);

        tvLabel = findViewById(R.id.tv_label);
        xfermodeView = findViewById(R.id.xfermode_view);

        title = getIntent().getStringExtra(TYPE);

        if (title.equals(ADD)) {
            xfermode = new PorterDuffXfermode(PorterDuff.Mode.ADD);
        } else if (title.equals(OVERLAY)) {
            xfermode = new PorterDuffXfermode(PorterDuff.Mode.OVERLAY);
        }

        dstBitmap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.dst);
        srcBitmap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.src);

        tvLabel.setText(title);
        xfermodeView.setCurXfermode(xfermode);
        xfermodeView.setBitmap(dstBitmap, srcBitmap);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dstBitmap.recycle();
        srcBitmap.recycle();
    }
}
