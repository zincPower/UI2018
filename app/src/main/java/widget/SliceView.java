//package widget;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.Point;
//import android.graphics.PointF;
//import android.support.annotation.Nullable;
//import android.support.v4.content.ContextCompat;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//
//import com.zinc.ui2018.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Jiang zinc
// * @date 创建时间：2018/11/22
// * @description 切换按钮，互嵌
// */
//public class SliceView extends View {
//
//    private final static String TAG = SliceView.class.getSimpleName();
//    private final static int LEFT = 1;
//    private final static int RIGHT = 2;
//    private final static int NONE = 0;
//
//    // 斜面高度
//    private final static double ANGLE = 2.67;
//    // 线宽
//    private float mLineWidth;
//    // 字体大小
//    private float mTextSize;
//    // 间隔
//    private int mSpace;
//    // 是否已经初始化了
//    private boolean mIsInit = false;
//
//    // 可绘制部分的 高、宽
//    private float mHeight;
//    private float mWidth;
//
//    // 突出部分的长度
//    private float mSharpWidth;
//
//    private float mShortLine;
//    private float mLongLine;
//
//    // 右边的正常颜色
//    private int mRightColor;
//    // 左边的正常颜色
//    private int mLeftColor;
//    // 未被选中颜色
//    private int mUnSelectColor;
//
//    private Paint mRightPaint;
//    private Paint mLeftPaint;
//    private Paint mRightTextPaint;
//    private Paint mLeftTextPaint;
//
//    private Path mRightPath;
//    private Path mLeftPath;
//
//    // 右边文字
//    private String mRightText;
//    // 左边文字
//    private String mLeftText;
//
//    private List<PointF> mLeftPoint = new ArrayList<>();
//    private List<PointF> mRightPoint = new ArrayList<>();
//
//    // 默认选择左边
//    private boolean isSelectLeft = true;
//
//    // 触碰到的是哪一边，用于抬起时判读是否为选中
//    private int mTouchIndex = 0;
//
//    private ClickListener mListener;
//
//    public SliceView(Context context) {
//        this(context, null, 0);
//    }
//
//    public SliceView(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public SliceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context);
//    }
//
//    private void init(Context context) {
//
//        mSpace = dip2px(context, 15);
//        mLineWidth = dip2px(context, 1);
//        mTextSize = dip2px(context, 14);
//
//        mLeftColor = ContextCompat.getColor(context, R.color.color_slice_right);
//        mRightColor = ContextCompat.getColor(context, R.color.color_slice_left);
//        mUnSelectColor = ContextCompat.getColor(context, R.color.color_slice_unselect);
//
//        mLeftPaint = new Paint();
//        mLeftPaint.setAntiAlias(true);
//        mLeftPaint.setColor(mLeftColor);
//        mLeftPaint.setStrokeWidth(mLineWidth);
//
//        mRightPaint = new Paint();
//        mRightPaint.setAntiAlias(true);
//        mRightPaint.setColor(mRightColor);
//        mRightPaint.setStrokeWidth(mLineWidth);
//
//        mLeftTextPaint = new Paint();
//        mLeftTextPaint.setAntiAlias(true);
//        mLeftTextPaint.setTextAlign(Paint.Align.CENTER);
//        mLeftTextPaint.setTextSize(mTextSize);
//
//        mRightTextPaint = new Paint();
//        mRightTextPaint.setAntiAlias(true);
//        mRightTextPaint.setTextAlign(Paint.Align.CENTER);
//        mRightTextPaint.setTextSize(mTextSize);
//
//        mLeftPath = new Path();
//        mRightPath = new Path();
//
//        mLeftText = "增加保证金";
//        mRightText = "减少保证金";
//
//    }
//
//    public void setListener(ClickListener listener) {
//        this.mListener = listener;
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        if (!mIsInit) {
//
//            mIsInit = true;
//            int height = getMeasuredHeight();
//            int width = getMeasuredWidth();
//
//            mHeight = height - getPaddingTop() - getPaddingBottom();
//            mWidth = width - getPaddingLeft() - getPaddingRight();
//
//            mSharpWidth = (float) (mHeight / ANGLE);
//
//            mShortLine = (mWidth - mSharpWidth - mSpace) / 2;
//            mLongLine = mShortLine + mSharpWidth;
//
//            buildBtnPath();
//        }
//
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        if (isSelectLeft) {
//            mLeftPaint.setStyle(Paint.Style.FILL);
//            mRightPaint.setStyle(Paint.Style.STROKE);
//
//            mLeftPaint.setColor(mLeftColor);
//            mRightPaint.setColor(mUnSelectColor);
//
//            mLeftTextPaint.setColor(Color.WHITE);
//            mRightTextPaint.setColor(mUnSelectColor);
//
//        } else {
//            mLeftPaint.setStyle(Paint.Style.STROKE);
//            mRightPaint.setStyle(Paint.Style.FILL);
//
//            mLeftPaint.setColor(mUnSelectColor);
//            mRightPaint.setColor(mRightColor);
//
//            mLeftTextPaint.setColor(mUnSelectColor);
//            mRightTextPaint.setColor(Color.WHITE);
//        }
//
//        canvas.drawPath(mLeftPath, mLeftPaint);
//        canvas.drawPath(mRightPath, mRightPaint);
//        canvas.drawText(mLeftText,
//                mShortLine / 2 + mSharpWidth / 2,
//                mHeight / 2 + mTextSize / 4,
//                mLeftTextPaint);
//        canvas.drawText(mRightText,
//                mWidth - mShortLine / 2 - mSharpWidth / 2,
//                mHeight / 2 + mTextSize / 4,
//                mRightTextPaint);
//
//    }
//
//    /**
//     * 构建按钮路径
//     */
//    private void buildBtnPath() {
//
//        mLeftPoint.add(new PointF(mLineWidth / 2f, mLineWidth / 2));
//        mLeftPoint.add(new PointF(mShortLine, mLineWidth / 2));
//        mLeftPoint.add(new PointF(mLongLine, mHeight - mLineWidth / 2));
//        mLeftPoint.add(new PointF(mLineWidth / 2, mHeight - mLineWidth / 2));
//        mLeftPoint.add(new PointF(mLineWidth / 2f, mLineWidth / 2));
//        buildPathFromList(mLeftPoint, mLeftPath);
//
//        mRightPoint.add(new PointF(mShortLine + mSpace, mLineWidth / 2));
//        mRightPoint.add(new PointF(mWidth - mLineWidth / 2, mLineWidth / 2));
//        mRightPoint.add(new PointF(mWidth - mLineWidth / 2, mHeight - mLineWidth / 2));
//        mRightPoint.add(new PointF(mLongLine + mSpace + mLineWidth / 2, mHeight - mLineWidth / 2));
//        mRightPoint.add(new PointF(mShortLine + mSpace, mLineWidth / 2));
//        buildPathFromList(mRightPoint, mRightPath);
//
//    }
//
//    private void buildPathFromList(List<PointF> pointList, Path path) {
//        for (int i = 0; i < pointList.size(); ++i) {
//            if (i == 0) {
//                path.moveTo(pointList.get(i).x, pointList.get(i).y);
//            } else {
//                path.lineTo(pointList.get(i).x, pointList.get(i).y);
//            }
//        }
//    }
//
//    private int dip2px(Context context, float dipValue) {
//        float density = context.getResources().getDisplayMetrics().density;
//        return (int) (dipValue * density + 0.5f);
//    }
//
//    private PointF mTouchPoint = new PointF();
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
////        Log.i(TAG, "onTouchEvent: [x:" + event.getX() + "; y:" + event.getY() + "]");
////        Log.i(TAG, "onTouchEvent: " + event.getAction());
//
//        mTouchPoint.x = event.getX();
//        mTouchPoint.y = event.getY();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
////                Log.i(TAG, "onTouchEvent: ACTION_DOWN");
//                if (contains(mLeftPoint, mTouchPoint)) {
//                    mTouchIndex = LEFT;
//                } else if (contains(mRightPoint, mTouchPoint)) {
//                    mTouchIndex = RIGHT;
//                } else {
//                    mTouchIndex = NONE;
//                }
//
//                break;
//            case MotionEvent.ACTION_MOVE:
////                Log.i(TAG, "onTouchEvent: ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                int upIndex = NONE;
//                if (contains(mLeftPoint, mTouchPoint)) {
//                    upIndex = LEFT;
//                } else if (contains(mRightPoint, mTouchPoint)) {
//                    upIndex = RIGHT;
//                }
//
//                // 抬起点不再范围内
//                if(upIndex == NONE){
//                    break;
//                }
//
//                // 按下和抬起要在同一个控件，才可以
//                if (upIndex != mTouchIndex) {
//                    break;
//                }
//
//                boolean result = upIndex == LEFT;
//
//                // 如果和之前一样的话，不回调
//                if(result == isSelectLeft){
//                    break;
//                }
//
//                isSelectLeft = result;
//
//                if (mListener != null) {
//                    mListener.onClick(upIndex);
//                }
//
////                Log.i(TAG, "onTouchEvent: ACTION_UP");
//                invalidate();
//                mTouchIndex = NONE;
//                break;
//        }
//
//        return true;
//    }
//
//    /**
//     * Return true if the given point is contained inside the boundary.
//     * See: http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
//     *
//     * @param touchPoint The point to check
//     * @return true if the point is inside the boundary, false otherwise
//     */
//    public boolean contains(List<PointF> pointList, PointF touchPoint) {
//        int i;
//        int j;
//        boolean result = false;
//        for (i = 0, j = pointList.size() - 1; i < pointList.size(); j = i++) {
//            if ((pointList.get(i).y > touchPoint.y) != (pointList.get(j).y > touchPoint.y) &&
//                    (touchPoint.x < (pointList.get(j).x - pointList.get(i).x) * (touchPoint.y - pointList.get(i).y) / (pointList.get(j).y - pointList.get(i).y) + pointList.get(i).x)) {
//                result = !result;
//            }
//        }
//        return result;
//    }
//
//    interface ClickListener {
//        void onClick(int index);
//    }
//
//}
