package com.zinc.svg.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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
 * author       : zinc
 * time         : 2019/4/5 下午6:01
 * desc         :
 * version      :
 */
public class JueJinLogoView extends View {

    private InnerHandler mHandle;

    private float mSvgWidth;
    private float mSvgHeight;

    private float mSvgTranX;
    private float mSvgTranY;

    private Paint mPaint;

    private final List<PathData> mPathDataList = new ArrayList<>();

    private int mSvgRes;

    private PathMeasure mPathMeasure;

    /**
     * 画布的矩阵
     */
    private final Matrix mCanvasMatrix = new Matrix();

    /**
     * svg 的 rect
     */
    private final RectF mSvgRect = new RectF();

    private final Path mAnimPath = new Path();

    private ValueAnimator mAnim = null;
    private float mAnimValue = 0;

    private float mLineWidth;

    public JueJinLogoView(Context context) {
        this(context, null, 0);
    }

    public JueJinLogoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JueJinLogoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        // 关闭硬件加速，否则会模糊
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mSvgRes = R.raw.juejin;

        mHandle = new InnerHandler(context, this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPathMeasure = new PathMeasure();

        mLineWidth = UIUtils.dip2px(context, 0.5f);

        ParserSvgThread thread = new ParserSvgThread(context, mSvgRes);
        thread.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCanvasMatrix.reset();
        mAnimPath.reset();
        mAnimPath.lineTo(0, 0);

        float mScale = calculateScale(mSvgRect.width(), mSvgRect.height(), getWidth(), getHeight());

        // 移至中心
        mCanvasMatrix.preTranslate(getWidth() / 2, getHeight() / 2);
        mCanvasMatrix.preTranslate(-mSvgRect.width() / 2, -mSvgRect.height() / 2);

        mCanvasMatrix.preScale(
                mScale,
                mScale,
                mSvgRect.width() / 2,
                mSvgRect.height() / 2);

        canvas.setMatrix(mCanvasMatrix);

        int index = (int) mAnimValue;
        float process = mAnimValue - index;

        for (int i = 0; i < index; ++i) {
            PathData pathData = mPathDataList.get(i);
            mPaint.setColor(pathData.color);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawPath(pathData.path, mPaint);
        }

        if (mAnimValue >= mPathDataList.size()) {
            return;
        }


        Log.i("onDraw", "mPathDataList: " + mPathDataList.size() +
                "; mAnimValue: " + mAnimValue +
                "; index: " + index +
                "; process: " + process);

        PathData pathData = mPathDataList.get(index);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(pathData.color);
        mPaint.setStrokeWidth(mLineWidth / mScale);

        mPathMeasure.setPath(pathData.path, false);
        mPathMeasure.getSegment(0,
                mPathMeasure.getLength() * process,
                mAnimPath,
                true);
        canvas.drawPath(mAnimPath, mPaint);


    }

    public void start() {
        if (mAnim != null) {
            mAnim.cancel();
        }

        if (mPathDataList.size() <= 0) {
            return;
        }

        mAnim = ValueAnimator.ofFloat(0, mPathDataList.size());
        mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mAnim.setDuration(10000);
        mAnim.setInterpolator(new LinearInterpolator());
        mAnim.start();

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
    private class ParserSvgThread extends Thread {

        private static final String WIDTH = "android:width";
        private static final String HEIGHT = "android:height";
        private static final String VIEWPORT_WIDTH = "android:viewportWidth";
        private static final String VIEWPORT_HEIGHT = "android:viewportHeight";

        private static final String GROUP = "group";
        private static final String TRAN_X = "android:translateX";
        private static final String TRAN_Y = "android:translateY";

        private static final String PATH = "path";
        private static final String PATH_DATA = "android:pathData";
        private static final String FILL_COLOR = "android:fillColor";

        /**
         * 地图资源id
         */
        private final int mSvgResourceId;
        private final WeakReference<Context> mContext;

        ParserSvgThread(Context context, int svgResourceId) {
            this.mContext = new WeakReference<>(context);
            this.mSvgResourceId = svgResourceId;
        }

        @Override
        public void run() {
            // 打开 svg 的输入流
            InputStream inputStream = null;
            try {
                inputStream = getContext().getResources().openRawResource(mSvgResourceId);
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
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 出错则关闭流，发送提示信息
            if (document == null) {
                mHandle.sendEmptyMessage(InnerHandler.ERROR);
                return;
            }

            // 获取根结点
            Element root = document.getDocumentElement();

            // 装载我们解析的数据
            List<PathData> pathDataList = new ArrayList<>();

            float viewportWidth = 0;
            float viewportHeight = 0;

            try {
                mSvgWidth = getRealSize(root.getAttribute(WIDTH));
                mSvgHeight = getRealSize(root.getAttribute(HEIGHT));
                viewportWidth = getRealSize(root.getAttribute(VIEWPORT_WIDTH));
                viewportHeight = getRealSize(root.getAttribute(VIEWPORT_HEIGHT));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (viewportWidth == 0 || viewportHeight == 0) {
                mHandle.sendEmptyMessage(InnerHandler.ERROR);
                return;
            }

            // 获取 <group> 节点
            NodeList groupNodeList = root.getElementsByTagName(GROUP);

            Element group = (Element) groupNodeList.item(0);
            String tranX = group.getAttribute(TRAN_X);
            if (TextUtils.isEmpty(tranX)) {
                mSvgTranX = 0;
            } else {
                float rate = mSvgWidth / viewportWidth;
                try {
                    float tx = Float.parseFloat(tranX);
                    mSvgTranX = rate * tx;
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandle.sendEmptyMessage(InnerHandler.ERROR);
                    return;
                }
            }

            String tranY = group.getAttribute(TRAN_Y);
            if (TextUtils.isEmpty(tranY)) {
                mSvgTranY = 0;
            } else {
                float rate = mSvgHeight / viewportHeight;
                try {
                    float ty = Float.parseFloat(tranY);
                    mSvgTranY = rate * ty;
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandle.sendEmptyMessage(InnerHandler.ERROR);
                    return;
                }
            }

            NodeList pathNodeList = group.getElementsByTagName(PATH);

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
                String pathData = pathNode.getAttribute(PATH_DATA);
                // path 的 颜色
                String colorData = pathNode.getAttribute(FILL_COLOR);

                // 解析 path
                Path path = null;
                try {
                    path = PathParser.createPathFromPathData(pathData);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // path 解析出错，退出
                if (path == null) {
                    mHandle.sendEmptyMessage(InnerHandler.ERROR);
                    return;
                }

                int color = Color.parseColor(colorData);

                path.computeBounds(rect, true);

                left = left == -1 ? rect.left : Math.min(left, rect.left);
                right = right == -1 ? rect.right : Math.max(right, rect.right);
                top = top == -1 ? rect.top : Math.min(top, rect.top);
                bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);

                PathData item = new PathData();
                item.path = path;
                item.color = color;

                pathDataList.add(item);
            }

            mSvgRect.left = left;
            mSvgRect.top = top;
            mSvgRect.right = right;
            mSvgRect.bottom = bottom;

            mPathDataList.clear();
            mPathDataList.addAll(pathDataList);

            mHandle.sendEmptyMessage(InnerHandler.SUCCESS);
        }

        private float getRealSize(String sizeString) {

            if (sizeString.endsWith("px")) {
                return Float.parseFloat(
                        sizeString.substring(0, sizeString.length() - 2));
            } else if (sizeString.endsWith("dp")) {
                float dpSize = Float.parseFloat(sizeString.substring(0, sizeString.length() - 2));
                return UIUtils.dip2px(mContext.get(), dpSize);
            }

            try {
                return Float.parseFloat(sizeString);
            } catch (Exception e) {
                throw e;
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

    private static class PathData {
        Path path;
        int color;
    }

}
