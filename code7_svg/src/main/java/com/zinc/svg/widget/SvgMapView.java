package com.zinc.svg.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.zinc.svg.PathParser;
import com.zinc.svg.R;
import com.zinc.lib_base.UIUtils;

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
            R.color.t1_color4
    };

    private static final int ANIM_DURATION = 500;

    private static final int DEFAULT_SEL_COLOR = R.color.t1_sel_color;
    private static final int DEFAULT_OUTLINE_COLOR = R.color.t1_outline;
    private static final int DEFAULT_MAP_RESOURCE = R.raw.china;

    private InnerHandler mHandle;

    /**
     * 填充地图的颜色
     */
    private int[] mMapColor = DEFAULT_COLOR;

    /**
     * 选中的区域颜色
     */
    private int mSelColor;
    private int mOutlineColor;

    private Paint mPaint;
    private Paint mTextPaint;

    /**
     * 缩放倍数
     */
    private float mScale;

    /**
     * 是否在播放
     */
    private boolean isPlaying;

    /**
     * 用于存储已经解析完的数据
     */
    private final List<ItemData> mMapItemDataList = new ArrayList<>();

    /**
     * 画布的矩阵
     */
    private final Matrix mCanvasMatrix = new Matrix();
    /**
     * 触碰点的矩阵
     */
    private final Matrix mTouchChangeMatrix = new Matrix();

    /**
     * 用于判断获取触碰区域的rect
     */
    private final RectF mTouchRectF = new RectF();

    /**
     * 描述的文字
     */
    private final RectF mDesRect = new RectF();

    /**
     * 用于记录触碰去的范围
     */
    private final Region mTouchRegion = new Region();

    /**
     * 触碰点，第一个为x轴，第二个为y轴
     */
    private final float[] mTouchPoints = new float[]{0, 0};

    /**
     * 选中的区域
     */
    private ItemData mSelItem;

    /**
     * svg 的 rect
     */
    private final RectF mSvgRect = new RectF();

    /**
     * 当前显示的主 rect
     */
    private final RectF mCurRect = new RectF();

    /**
     * 上次选中的 rect
     */
    private final RectF mLastRectF = new RectF();

    /**
     * 动画中的缩放比例
     */
    private float mAnimScale;

    private ValueAnimator mValueAnim;

    /**
     * 解析线程
     */
    private ParserMapThread mParserThread;

    /**
     * 地图资源
     */
    private int mMapResource;

    /**
     * 描述框的外边距
     */
    private int mDesMargin;

    /**
     * 描述文字的大小
     */
    private int mDesTextSize;

    /**
     * 描述文字的颜色
     */
    private int mDesTextColor;

    /**
     * 描述文字背景颜色
     */
    private int mDesBgColor;

    /**
     * 描述框的圆角半径
     */
    private int mDesRoundRadius;

    /**
     * 文字那边距
     */
    private int mDesPadding;

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

        // 关闭硬件加速，否则会模糊
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mHandle = new InnerHandler(context, this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);

        mSelColor = ContextCompat.getColor(context, DEFAULT_SEL_COLOR);
        mOutlineColor = ContextCompat.getColor(context, DEFAULT_OUTLINE_COLOR);
        mMapResource = DEFAULT_MAP_RESOURCE;

        mDesMargin = UIUtils.dip2px(context, 5);
        mDesPadding = UIUtils.dip2px(context, 5f);
        mDesRoundRadius = UIUtils.dip2px(context, 3.5f);

        mDesTextSize = UIUtils.dip2px(context, 12f);
        mDesTextColor = ContextCompat.getColor(context, R.color.des_color);
        mDesBgColor = Color.WHITE;
        mTextPaint.setTextSize(mDesTextSize);
        mTextPaint.setColor(mDesTextColor);

        // 初始化动画
        mValueAnim = ValueAnimator.ofFloat(0, 1);
        mValueAnim.setInterpolator(new LinearInterpolator());
        mValueAnim.setDuration(ANIM_DURATION);
        mValueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimScale = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mValueAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isPlaying = false;
            }
        });

        initData();

    }

    private void initData() {
        mSvgRect.setEmpty();
        mCurRect.setEmpty();
        mLastRectF.setEmpty();
        mScale = 1.0f;
        mAnimScale = 1.0f;
        isPlaying = false;

        mMapItemDataList.clear();

        mCanvasMatrix.reset();
        mTouchChangeMatrix.reset();

        mTouchRectF.setEmpty();

        mSelItem = null;

        mParserThread = new ParserMapThread(mMapResource);
        mParserThread.start();
    }

    /**
     * 设置地图资源
     *
     * @param mapResource 地图资源
     */
    public void setMapResource(int mapResource) {
        if (mParserThread != null && mParserThread.isAlive()) {
            mParserThread.mIsCancel = true;
        }
        mMapResource = mapResource;
        initData();
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
        this.mSelColor = ContextCompat.getColor(getContext(), selColor);
    }

    /**
     * 设置勾勒颜色
     *
     * @param outlineColor 勾勒颜色
     */
    public void setOutlineColor(int outlineColor) {
        this.mOutlineColor = ContextCompat.getColor(getContext(), outlineColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();
        // 重置矩阵
        mCanvasMatrix.reset();
        mTouchChangeMatrix.reset();

        handleLastState();
        handleCurState();

        // 将矩阵施加于画布
        canvas.setMatrix(mCanvasMatrix);

        // 画地图
        for (ItemData itemData : mMapItemDataList) {
            drawItem(canvas, itemData);
        }

        canvas.restore();

        drawDesTest(canvas);

    }

    /**
     * 描述文字
     */
    private void drawDesTest(Canvas canvas) {

        if (mSelItem == null) {
            return;
        }
        canvas.save();

        float textLength = mTextPaint.measureText(mSelItem.title);

        mDesRect.left = 0;
        mDesRect.top = 0;
        mDesRect.right = textLength + mDesPadding * 4;
        mDesRect.bottom = mDesTextSize + mDesPadding * 2;

        canvas.translate(mDesMargin, mDesMargin);

        mPaint.setColor(mDesBgColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(mDesRect, mDesRoundRadius, mDesRoundRadius, mPaint);

        canvas.translate(0, mDesTextSize / 2);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mDesTextSize);
        mPaint.setColor(mDesTextColor);
        canvas.drawText(mSelItem.title,
                mDesRect.width() / 2,
                mDesRect.height() / 2,
                mPaint);

        canvas.restore();
    }

    /**
     * 处理当前状态
     */
    private void handleCurState() {
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
     */
    private void handleLastState() {
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

            mLastRectF.set(mCurRect);

            // 如果当前已经显示该区域就不再执行
            if (mTouchRectF.equals(mCurRect)) {
                mSelItem = null;
                startAnim(mSvgRect);
                return true;
            }

            //  选中的为空
            if (mSelItem == null) {
                startAnim(mSvgRect);
                return true;
            }

            startAnim(mTouchRectF);
        }

        return true;
    }

    /**
     * 开始动画
     *
     * @param rectF 将要到达的 rect
     */
    private void startAnim(RectF rectF) {
        mCurRect.set(rectF);
        mValueAnim.start();
        isPlaying = true;
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
                        (int) mTouchRectF.top,
                        (int) mTouchRectF.right,
                        (int) mTouchRectF.bottom)
        );

        return mTouchRegion.contains((int) x, (int) y);
    }

    /**
     * 画每个区域
     */
    private void drawItem(Canvas canvas, ItemData itemData) {

        if (itemData == mSelItem) {
            mPaint.setColor(mSelColor);
        } else {
            mPaint.setColor(itemData.color);
        }

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(itemData.path, mPaint);

        mPaint.setColor(mOutlineColor);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(itemData.path, mPaint);

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

        /**
         * 是否要取消
         */
        private boolean mIsCancel;

        ParserMapThread(int mapResourceId) {
            this.mMapResourceId = mapResourceId;
            this.mIsCancel = false;
        }

        @Override
        public void run() {
            // 打开地图的输入流
            InputStream inputStream = null;
            try {
                inputStream = getContext().getResources().openRawResource(mMapResourceId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }

            // 地图输入流打开失败
            if (inputStream == null) {
                if (!mIsCancel) {
                    mHandle.sendEmptyMessage(InnerHandler.ERROR);
                }
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
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 出错则关闭流，发送提示信息
            if (document == null) {
                if (!mIsCancel) {
                    mHandle.sendEmptyMessage(InnerHandler.ERROR);
                }
                return;
            }

            // 获取根结点
            Element root = document.getDocumentElement();

            // 装载我们解析的数据
            List<ItemData> mapDataList = new ArrayList<>();

            // 获取 <path/> 节点
            NodeList pathNodeList = root.getElementsByTagName(PATH);

            int colorSize = mMapColor.length;

            // 用于记录整个 svg 的实际大小
            float left = -1;
            float top = -1;
            float right = -1;
            float bottom = -1;

            // 计算出 path 的 rect
            RectF rect = new RectF();

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
                    if (!mIsCancel) {
                        mHandle.sendEmptyMessage(InnerHandler.ERROR);
                    }
                    return;
                }

                path.computeBounds(rect, true);

                left = left == -1 ? rect.left : Math.min(left, rect.left);
                right = right == -1 ? rect.right : Math.max(right, rect.right);
                top = top == -1 ? rect.top : Math.min(top, rect.top);
                bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);

                ItemData itemData = new ItemData(path,
                        ContextCompat.getColor(getContext(), mMapColor[i % colorSize]),
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
            if (!mIsCancel) {
                mHandle.sendEmptyMessage(InnerHandler.SUCCESS);
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

        ItemData(Path path, int color, String title) {
            this.path = path;
            this.color = color;
            this.title = title;
        }

    }

}
