//package widget;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.content.ContextCompat;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//
//import com.zinc.ui2018.R;
//
///**
// * @author Jiang zinc
// * @date 创建时间：2018/11/22
// * @description
// */
//public class SliceActivity extends Activity {
//
//    private static final String TAG = SliceActivity.class.getSimpleName();
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_slice);
//
////        SliceView slice = findViewById(R.id.slice);
////        slice.setListener(new SliceView.ClickListener() {
////            @Override
////            public void onClick(int index) {
////                Log.i(TAG, "onClick: " + index);
////            }
////        });
//
//        LeanView leanView = findViewById(R.id.lean_view);
//        leanView.setText("已成交");
//        leanView.setBgColor(ContextCompat.getColor(this, R.color.color_trade_done));
//
//    }
//}
