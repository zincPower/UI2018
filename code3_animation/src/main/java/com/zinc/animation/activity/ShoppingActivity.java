package com.zinc.animation.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zinc.animation.R;
import com.zinc.animation.widget.ShoppingView;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/26
 * @description
 */
public class ShoppingActivity extends AppCompatActivity implements ShoppingView.AnimatorListener {

    private static final String TAG = "ShoppingActivity";

    private ImageView ivIcon;
    private TextView tvAdd;
    private ImageView ivShoppingCart;
    private TextView tvDot;

    private ObjectAnimator scaleXAnim;
    private ObjectAnimator scaleYAnim;

    private AnimatorSet animSet;

    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        ivIcon = findViewById(R.id.iv_icon);
        tvAdd = findViewById(R.id.tv_add);
        ivShoppingCart = findViewById(R.id.iv_shopping_cart);
        tvDot = findViewById(R.id.tv_dot);

        scaleXAnim = ObjectAnimator.ofFloat(tvDot, "scaleX", 1f, 1.2f, 1f, 1.1f, 1f);
        scaleYAnim = ObjectAnimator.ofFloat(tvDot, "scaleY", 1f, 1.2f, 1f, 1.1f, 1f);
        animSet = new AnimatorSet();
        animSet.play(scaleXAnim).with(scaleYAnim);

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PointF iconPos = getScreenPoint(ivIcon);
                PointF cartPos = getScreenPoint(ivShoppingCart);

                ShoppingView view = new ShoppingView(ShoppingActivity.this);
                view.setListener(ShoppingActivity.this);
                view.setImageBitmap(((BitmapDrawable) ivIcon.getDrawable()).getBitmap());

                ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ivIcon.getWidth(), ivIcon.getHeight());
                view.setLayoutParams(layoutParams);

                ViewGroup decorView = (ViewGroup) ShoppingActivity.this.getWindow().getDecorView();
                decorView.addView(view);

                view.start(iconPos, cartPos);

            }
        });

    }

    /**
     * 获取在整个屏幕内的绝对坐标
     */
    private PointF getScreenPoint(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        return new PointF(location[0], location[1]);
    }

    @Override
    public void animEnd() {
        Log.i(TAG, "animEnd");

        addCount();
        ivShoppingCart.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.shopping_cart_full));
    }

    public void addCount() {
        ++count;
        if (count <= 0) {
            return;
        }

        if(animSet.isRunning()){
            animSet.cancel();
        }

        animSet.start();

        tvDot.setVisibility(View.VISIBLE);
        tvDot.setText(count + "");
    }

}
