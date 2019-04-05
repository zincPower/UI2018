package com.zinc.class21_svg.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.zinc.class21_svg.PathParser;
import com.zinc.class21_svg.R;
import com.zinc.lib_base.UIUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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

        mSvgRes = R.raw.juejin;

        mHandle = new InnerHandler(context, this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        ParserSvgThread thread = new ParserSvgThread(context, mSvgRes);
        thread.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mSvgTranX, mSvgTranY);

        for (PathData pathData : mPathDataList) {
            mPaint.setColor(pathData.color);
            canvas.drawPath(pathData.path, mPaint);
        }

    }

    /**
     * 解析地图的线程
     */
    private class ParserSvgThread extends Thread {

        private static final String WIDTH = "android:width";
        private static final String HEIGHT = "android:height";
        private static final String VIEWPORT_WIDTH = "viewportWidth";
        private static final String VIEWPORT_HEIGHT = "viewportHeight";

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

                PathData item = new PathData();
                item.path = path;
                item.color = color;

                pathDataList.add(item);
            }

            mPathDataList.clear();
            mPathDataList.addAll(pathDataList);

            mHandle.sendEmptyMessage(InnerHandler.SUCCESS);
        }

        /**
         * 关闭流
         *
         * @param inputStream 输入流
         */
        private void close(InputStream inputStream) {
            if (inputStream == null) {
                return;
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private float getRealSize(String sizeString) {

            if (sizeString.endsWith("px")) {
                return Float.parseFloat(
                        sizeString.substring(0, sizeString.length() - 2));
            } else if (sizeString.endsWith("dp")) {
                float dpSize = Float.parseFloat(sizeString.substring(0, sizeString.length() - 2));
                return UIUtils.dip2px(mContext.get(), dpSize);
            }

            throw new RuntimeException("Error Unit");

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
