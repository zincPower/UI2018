//package widget;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.View;
//
//import com.zinc.lib_base.UIUtils;
//import com.zinc.ui2018.R;
//
///**
// * @author Jiang zinc
// * @date 创建时间：2018/11/26
// * @description 角标
// */
//public class LeanView extends View {
//
//    private Paint cornerPaint;
//    private Paint textPaint;
//
//    private Path cornerPath;
//
//    // 角的颜色
//    private int cornerColor;
//    // 内容的颜色
//    private int textColor;
//    // 内容
//    private String text;
//    // 内容大小
//    private float textSize;
//
//    // 是否已经初始化
//    private boolean isInit = false;
//
//    public LeanView(Context context) {
//        this(context, null, 0);
//    }
//
//    public LeanView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public LeanView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context, attrs);
//    }
//
//    private void init(Context context, AttributeSet attrs) {
//        cornerPaint = new Paint();
//        cornerPaint.setAntiAlias(true);
//        cornerPaint.setStyle(Paint.Style.FILL);
////        cornerPaint.setStrokeWidth(1);
//
//        textPaint = new Paint();
//        textPaint.setAntiAlias(true);
//        textPaint.setStyle(Paint.Style.STROKE);
//        textPaint.setTextAlign(Paint.Align.CENTER);
//
//        cornerPath = new Path();
//
//        if (attrs == null) {
//            return;
//        }
//
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lean_view_styleable);
//        for (int i = 0; i < typedArray.getIndexCount(); i++) {
//            int attr = typedArray.getIndex(i);
//            switch (attr) {
//
//                case R.styleable.lean_view_styleable_text:  //内容
//                    text = typedArray.getString(R.styleable.lean_view_styleable_text);
//                    break;
//
//                case R.styleable.lean_view_styleable_textColor:          //内容颜色
//                    textColor = typedArray.getColor(R.styleable.lean_view_styleable_textColor,
//                            getResources().getColor(R.color.colorPrimary));
//                    break;
//
//                case R.styleable.lean_view_styleable_bgColor:           // 背景颜色
//                    cornerColor = typedArray.getColor(R.styleable.lean_view_styleable_bgColor,
//                            getResources().getColor(R.color.colorAccent));
//                    break;
//
//                case R.styleable.lean_view_styleable_textSizeSp:
//                    textSize = typedArray.getFloat(R.styleable.lean_view_styleable_textSizeSp, 1f);
//                    textSize = UIUtils.sp2px(context, textSize);
//                    break;
//
//            }
//        }
//        typedArray.recycle();
//
//        cornerPaint.setColor(cornerColor);
//        textPaint.setTextSize(textSize);
//        textPaint.setColor(textColor);
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        if (!isInit) {
//            isInit = true;
//            float left = getPaddingLeft();
//            float top = getPaddingTop();
//            float right = getMeasuredWidth() - getPaddingRight();
//            float bottom = getMeasuredHeight() - getPaddingBottom();
//            cornerPath.moveTo(left, top);
//            cornerPath.lineTo(right, top);
//            cornerPath.lineTo(right, bottom);
//            cornerPath.lineTo(left, top);
//        }
//
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//
//        canvas.drawPath(cornerPath, cornerPaint);
//
//        if (TextUtils.isEmpty(text)) {
//            return;
//        }
//
//        canvas.save();
//        canvas.rotate(45, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
//        canvas.translate(0, -textSize / 2);
//        canvas.drawText(text, getMeasuredWidth() / 2, getMeasuredHeight() / 2, textPaint);
//        canvas.restore();
//
//    }
//
//    public void setBgColor(int bgColor) {
//        this.cornerColor = bgColor;
//        this.cornerPaint.setColor(bgColor);
//        invalidate();
//    }
//
//    public void setText(String text) {
//        this.text = text;
//        invalidate();
//    }
//
//    public void setTextColor(int textColor) {
//        this.textColor = textColor;
//        this.textPaint.setColor(textColor);
//        invalidate();
//    }
//
//    public void setTextSize(float px) {
//        this.textSize = px;
//        this.textPaint.setTextSize(px);
//        invalidate();
//    }
//
//}
