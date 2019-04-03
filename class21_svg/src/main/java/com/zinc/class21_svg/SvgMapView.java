package com.zinc.class21_svg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PointFEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * author       : Jiang zinc
 * time         : 2019/3/28 上午11:23
 * email        : 56002982@qq.com
 * desc         : svg 的地图 View
 * version      : 1.0.0
 */
public class SvgMapView extends View {

    /**
     * 默认颜色
     */
    private static final int[] DEFAULT_COLOR = new int[]{
            R.color.t1_color1,
            R.color.t1_color2,
            R.color.t1_color3,
            R.color.t1_color4,
    };

    private static final int ANIM_DURATION = 500;

    private static final int DEFAULT_SEL_COLOR = R.color.sel_color;

    private Context mContext;

    private InnerHandler mHandle;

    /**
     * 填充地图的颜色
     */
    private int[] mMapColor = DEFAULT_COLOR;

    /**
     * 选中的区域颜色
     */
    private int mSelColor;

    private Paint mPaint;

    /**
     * svg 的 rect
     */
    private RectF mSvgRect;

    /**
     * 缩放倍数
     */
    private float mScale = 1.0f;

    /**
     * 是否在播放
     */
    private boolean isPlaying = false;

    /**
     * 用于存储已经解析完的数据
     */
    private final List<ItemData> mMapItemDataList = new ArrayList<>();

    /**
     * 画布的矩阵
     */
    private Matrix mCanvasMatrix = new Matrix();
    /**
     * 触碰点的矩阵
     */
    private Matrix mTouchChangeMatrix = new Matrix();

    /**
     * 用于判断获取触碰区域的rect
     */
    private RectF mTouchRectF = new RectF();
    /**
     * 用于记录触碰去的范围
     */
    private Region mTouchRegion = new Region();

    /**
     * 触碰点，第一个为x轴，第二个为y轴
     */
    private float[] mTouchPoints = new float[]{0, 0};
    /**
     * 当前地图的中心
     */
    private float[] mCurCenterPoints = new float[]{0, 0};

    /**
     * 选中的区域
     */
    private ItemData mSelItem = null;

    /**
     * 当前显示的主 rect
     */
    private RectF mCurRect;

    private boolean mIsFirst = true;

    /**
     * 动画中的缩放比例
     */
    private float mAnimScale = 1.0f;

    private ValueAnimator mValueAnim;

    private SvgAnimatorListener mSvgAnimListener;

    private RectF mLastRectF;

    public SvgMapView(Context context) {
        this(context, null, 0);
    }

    public SvgMapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SvgMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        // 关闭硬件加速，
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mContext = context;
        mHandle = new InnerHandler(context, this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mSvgRect = new RectF(-1, -1, -1, -1);
        mCurRect = new RectF(-1, -1, -1, -1);
        mLastRectF = new RectF(-1, -1, -1, -1);

        mSelColor = ContextCompat.getColor(context, DEFAULT_SEL_COLOR);

        new ParserMapThread(R.raw.china).start();

        mSvgAnimListener = new SvgAnimatorListener();
        mValueAnim = ValueAnimator.ofFloat(0, 1);
        mValueAnim.setInterpolator(new LinearInterpolator());
        mValueAnim.setDuration(ANIM_DURATION);
        mValueAnim.addUpdateListener(mSvgAnimListener);
        mValueAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isPlaying = false;
            }
        });

    }

    /**
     * 设置地图颜色
     *
     * @param mapColor 地图颜色数组
     */
    public void setMapColor(int[] mapColor) {
        this.mMapColor = mapColor;
    }

    /**
     * 设置选中颜色
     *
     * @param selColor 选中的颜色资源
     */
    public void setSelColor(int selColor) {
        this.mSelColor = ContextCompat.getColor(mContext, selColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mCanvasMatrix.reset();
        mTouchChangeMatrix.reset();

        handleLastState(canvas);
        handleCurState(canvas);

        // 将矩阵施加于画布
        canvas.setMatrix(mCanvasMatrix);

        // 画地图
        for (ItemData itemData : mMapItemDataList) {
            drawItem(canvas, itemData);
        }

    }

    private void handleCurState(Canvas canvas) {
        if (mCurRect.equals(mLastRectF)) {
            return;
        }

        float curCenterX = mCurRect.left + mCurRect.width() / 2;
        float curCenterY = mCurRect.top + mCurRect.height() / 2;

        float lastCenterX = mLastRectF.left + mLastRectF.width() / 2;
        float lastCenterY = mLastRectF.top + mLastRectF.height() / 2;

        float dx = curCenterX - lastCenterX;
        float dy = curCenterY - lastCenterY;

        // 进行缩放
        if (!mCurRect.isEmpty()) {

            mScale = calculateScale(mCurRect.width(), mCurRect.height(),
                    getWidth(), getHeight()) / mScale;

            if (mScale > 1) {
                mScale = (mScale - 1) * mAnimScale + 1;
            } else if (mScale < 1) {
                mScale = 1 - (1 - mScale) * mAnimScale;
            }
        }

        // 需要 多偏移区域 与 整地图 的外区域
        mCanvasMatrix.preTranslate(-dx * mAnimScale, -dy * mAnimScale);
        mCanvasMatrix.preScale(
                mScale,
                mScale,
                curCenterX,
                curCenterY);


        mTouchChangeMatrix.postTranslate(dx, dy);
        mTouchChangeMatrix.postScale(1 / mScale,
                1 / mScale,
                curCenterX,
                curCenterY);
    }

    /**
     * 处理上一次的状态
     *
     * @param canvas
     */
    private void handleLastState(Canvas canvas) {
        // 移至画布中心
        mCanvasMatrix.preTranslate(getWidth() / 2, getHeight() / 2);

        // 移外边
        float lastLeftMargin = mLastRectF.left - mSvgRect.left;
        float lastTopMargin = mLastRectF.top - mSvgRect.top;
        mCanvasMatrix.preTranslate(-lastLeftMargin, -lastTopMargin);

        // 移至中心
        mCanvasMatrix.preTranslate(-mLastRectF.width() / 2, -mLastRectF.height() / 2);

        // 进行缩放
        if (!mLastRectF.isEmpty()) {
            mScale = calculateScale(
                    mLastRectF.width(),
                    mLastRectF.height(),
                    getWidth(),
                    getHeight());
        }
        mCanvasMatrix.preScale(
                mScale,
                mScale,
                lastLeftMargin + mLastRectF.width() / 2,
                lastTopMargin + mLastRectF.height() / 2);

        mTouchChangeMatrix.postTranslate(-getWidth() / 2, -getHeight() / 2);
        mTouchChangeMatrix.postTranslate(lastLeftMargin, lastTopMargin);
        mTouchChangeMatrix.postTranslate(mLastRectF.width() / 2, mLastRectF.height() / 2);
        mTouchChangeMatrix.postScale(1 / mScale,
                1 / mScale,
                lastLeftMargin + mLastRectF.width() / 2,
                lastTopMargin + mLastRectF.height() / 2);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 正在播放则不处理
        if (isPlaying) {
            return true;
        }

        // 处理拦截事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            mTouchPoints[0] = event.getX();
            mTouchPoints[1] = event.getY();

            // 将点坐标进行转换
            mTouchChangeMatrix.mapPoints(mTouchPoints);

            // 重置选项
            mSelItem = null;

            // 置入地图选中状态
            for (ItemData item : mMapItemDataList) {
                if (isTouch(item, mTouchPoints[0], mTouchPoints[1])) {
                    mSelItem = item;
                    break;
                }
            }

            // 如果当前已经显示该区域就不再执行
            if (mTouchRectF.equals(mCurRect)) {
                return true;
            }

            mLastRectF.set(mCurRect);

            if (mSelItem != null) {
                mCurRect.set(mTouchRectF);
                mValueAnim.start();
                isPlaying = true;
            } else {
                mCurRect.set(mSvgRect);
            }

            postInvalidate();

        }

        return true;
    }

    /**
     * 是否在触碰的范围内
     *
     * @param item 地图的每个数据项
     * @param x    触碰点的x轴
     * @param y    触碰点的y轴
     * @return true：在范围内；false：在范围外
     */
    private boolean isTouch(ItemData item, float x, float y) {

        item.path.computeBounds(mTouchRectF, true);

        mTouchRegion.setPath(
                item.path,
                new Region((int) mTouchRectF.left,
                        (int) mTouchRectF.top, (int)
                        mTouchRectF.right,
                        (int) mTouchRectF.bottom)
        );

        return mTouchRegion.contains((int) x, (int) y);
    }

    /**
     * 画每个区域
     *
     * @param canvas
     * @param itemData
     */
    private void drawItem(Canvas canvas, ItemData itemData) {

        if (itemData == mSelItem) {
            mPaint.setColor(mSelColor);
        } else {
            mPaint.setColor(itemData.color);
        }

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(itemData.path, mPaint);

//        mPaint.setColor(mSelColor);
//        canvas.drawRect(mCurRect, mPaint);

    }

    private void drawPoint(Canvas canvas, float x, float y) {
        drawPoint(canvas, x, y, R.color.t4_color4);
    }

    private void drawPoint(Canvas canvas, float x, float y, int color) {

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(ContextCompat.getColor(mContext, color));
        canvas.drawCircle(x, y, 10f, mPaint);

    }

    /**
     * 用于计算缩放的大小
     */
    private float calculateScale(float fromWidth,
                                 float fromHeight,
                                 float toWidth,
                                 float toHeight) {
        float widthScale = toWidth / fromWidth;
        float heightScale = toHeight / fromHeight;

        return Math.min(widthScale, heightScale);
    }

    /**
     * 用于计算缩放的大小
     */
    private float calculateScaleWithMax(float fromWidth,
                                        float fromHeight,
                                        float toWidth,
                                        float toHeight) {
        float widthScale = toWidth / fromWidth;
        float heightScale = toHeight / fromHeight;

        return Math.max(widthScale, heightScale);
    }

    /**
     * 解析地图的线程
     */
    private class ParserMapThread extends Thread {

        private static final String PATH = "path";
        private static final String DATA = "d";
        private static final String TITLE = "title";

        /**
         * 地图资源id
         */
        private final int mMapResourceId;

        ParserMapThread(int mapResourceId) {
            this.mMapResourceId = mapResourceId;
        }

        @Override
        public void run() {
            // 打开地图的输入流
            InputStream inputStream = null;
            try {
                inputStream = mContext.getResources().openRawResource(mMapResourceId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }

            // 地图输入流打开失败
            if (inputStream == null) {
                mHandle.sendEmptyMessage(InnerHandler.ERROR);
                return;
            }

            // 从 XML文档 生成 DOM对象树
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document document = null;
            try {
                document = factory.newDocumentBuilder().parse(inputStream);
            } catch (SAXException |
                    IOException |
                    ParserConfigurationException e) {
                e.printStackTrace();
            }

            // 出错则关闭流，发送提示信息
            if (document == null) {
                close(inputStream);
                mHandle.sendEmptyMessage(InnerHandler.ERROR);
                return;
            }

            // 获取根结点
            Element root = document.getDocumentElement();

            // 装载我们解析的数据
            List<ItemData> mapDataList = new ArrayList<>();

            // 获取 <path/> 节点
            NodeList pathNodeList = root.getElementsByTagName(PATH);

            int colorSize = mMapColor.length;

            float left = -1;
            float top = -1;
            float right = -1;
            float bottom = -1;

            // 遍历所有的 Path 节点
            for (int i = 0; i < pathNodeList.getLength(); ++i) {
                Element pathNode = (Element) pathNodeList.item(i);
                // path 的 svg 路径
                String pathData = pathNode.getAttribute(DATA);
                // path 的 title
                String title = pathNode.getAttribute(TITLE);

                // 解析 path
                Path path = null;
                try {
                    path = PathParser.createPathFromPathData(pathData);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // path 解析出错，退出
                if (path == null) {
                    close(inputStream);
                    mHandle.sendEmptyMessage(InnerHandler.ERROR);
                    return;
                }

                // 计算出 path 的 rect
                RectF rect = new RectF();
                path.computeBounds(rect, true);

                left = left == -1 ? rect.left : Math.min(left, rect.left);
                right = right == -1 ? rect.right : Math.max(right, rect.right);
                top = top == -1 ? rect.top : Math.min(top, rect.top);
                bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);

                ItemData itemData = new ItemData(path,
                        ContextCompat.getColor(mContext, mMapColor[i % colorSize]),
                        title);

                mapDataList.add(itemData);
            }

            mSvgRect.left = left;
            mSvgRect.top = top;
            mSvgRect.right = right;
            mSvgRect.bottom = bottom;

            // 解析完，赋值给当前显示的主 Rect
            mCurRect.set(mSvgRect);
            mLastRectF.set(mSvgRect);

            mMapItemDataList.clear();
            mMapItemDataList.addAll(mapDataList);
            mHandle.sendEmptyMessage(InnerHandler.SUCCESS);
        }

        /**
         * 关闭流
         *
         * @param inputStream 输入流
         */
        private void close(InputStream inputStream) {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 内部handler
     */
    private static class InnerHandler extends Handler {

        /**
         * 解析成功
         */
        static final int SUCCESS = 1;
        /**
         * 解析失败
         */
        static final int ERROR = 2;

        static final String ERROR_MSG = "地图资源有误，载入失败";

        private WeakReference<Context> mContext;
        private WeakReference<View> mView;

        InnerHandler(Context context, View view) {
            super(Looper.getMainLooper());
            mContext = new WeakReference<>(context);
            mView = new WeakReference<>(view);
        }

        @Override
        public void dispatchMessage(Message msg) {
            if (msg.what == SUCCESS) {

                if (mView.get() != null) {
                    mView.get().postInvalidate();
                }

            } else if (msg.what == ERROR) {

                if (mContext.get() != null) {
                    Toast.makeText(mContext.get(), ERROR_MSG, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    /**
     * 地图数据项
     */
    private static class ItemData {

        // 路径数据
        Path path;
        // 颜色
        int color;
        // 名称
        String title;
        // 是否被选中
        boolean isSelect;

        // 区域
        Region region;

        ItemData(Path path, int color, String title) {
            this.path = path;
            this.color = color;
            this.isSelect = false;
            this.title = title;

            createRegion();
        }

        /**
         * 创建 region
         */
        private void createRegion() {
            RectF rectF = new RectF();
            path.computeBounds(rectF, true);

            Region region = new Region();
            region.setPath(path,
                    new Region((int) rectF.left,
                            (int) rectF.top,
                            (int) rectF.right,
                            (int) rectF.bottom));
        }

        private boolean isContains(float x, float y) {
            return region.contains((int) x, (int) y);
        }
    }

    private class SvgAnimatorListener implements ValueAnimator.AnimatorUpdateListener {

        /**
         * 开始的点
         */
        private float mStartPoints[] = new float[2];
        /**
         * 终止的点
         */
        private float mEndPoints[] = new float[2];
        /**
         * 开始的rect
         */
        private RectF mStartRect = new RectF();
        /**
         * 终止的rect
         */
        private RectF mEndRect = new RectF();

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float fraction = (float) animation.getAnimatedValue();

            mAnimScale = fraction;

            postInvalidate();

        }

    }

}
