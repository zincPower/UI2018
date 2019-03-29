package com.zinc.class21_svg;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
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
import android.view.View;
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
 * author       : zinc
 * time         : 2019/3/28 上午11:23
 * desc         : svg 的地图 View
 * version      :
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

    private Context mContext;

    private InnerHandler mHandle;

    private int[] mMapColor = DEFAULT_COLOR;

    private Paint mPaint;

    private RectF mSvgRect;

    private float mScale = 1.0f;

    /**
     * 用于存储已经解析完的数据
     */
    private final List<ItemData> mMapItemDataList = new ArrayList<>();

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
        mContext = context;
        mHandle = new InnerHandler(context, this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mSvgRect = new RectF(-1, -1, -1, -1);

        new ParserMapThread(R.raw.world).start();
    }

    /**
     * 设置地图颜色
     *
     * @param mapColor 地图颜色数组
     */
    public void setMapColor(int[] mapColor) {
        this.mMapColor = mapColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 不为空，则进行计算缩放
        if (!mSvgRect.isEmpty()) {
            float widthScale = getWidth() / mSvgRect.width();
            float heightScale = getHeight() / mSvgRect.height();

            mScale = Math.min(widthScale, heightScale);
        }

        // 移至中间
        canvas.translate((getWidth() - mSvgRect.width() * mScale) / 2,
                (getHeight() - mSvgRect.height() * mScale) / 2);

        canvas.scale(mScale, mScale);
        Log.i("SvgMapView", "onDraw: " + mScale);

        for (ItemData itemData : mMapItemDataList) {
            drawItem(canvas, itemData);
        }

    }

    /**
     * 画每个区域
     *
     * @param canvas
     * @param itemData
     */
    private void drawItem(Canvas canvas, ItemData itemData) {

        // 画区域
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(itemData.color);
        canvas.drawPath(itemData.path, mPaint);

        // 画边界

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

            Log.i("SvgMapView", "run: " + mSvgRect.width() + ";" + mSvgRect.height());

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
        // 是否被选中
        boolean isSelect;
        // 名称
        String title;

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

}
