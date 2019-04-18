package com.zinc.xfermode.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/29
 * @description
 */
public class XFerModeBean {

    private String name;
    private boolean isSelect;
    private Bitmap dstBitmap;
    private Bitmap srcBitmap;
    private int dst;
    private int src;

    public XFerModeBean(Context context,
                        String name,
                        boolean isSelect,
                        int dst,
                        int src) {
        this.name = name;
        this.isSelect = isSelect;

        this.dst = dst;
        this.src = src;

        this.dstBitmap = BitmapFactory.decodeResource(context.getResources(), dst);
        this.srcBitmap = BitmapFactory.decodeResource(context.getResources(), src);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public Bitmap getDstBitmap() {
        return dstBitmap;
    }

    public void setDstBitmap(Bitmap dstBitmap) {
        this.dstBitmap = dstBitmap;
    }

    public Bitmap getSrcBitmap() {
        return srcBitmap;
    }

    public void setSrcBitmap(Bitmap srcBitmap) {
        this.srcBitmap = srcBitmap;
    }

    public int getDst() {
        return dst;
    }

    public void setDst(int dst) {
        this.dst = dst;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public void recycle() {
        this.dstBitmap.recycle();
        this.dstBitmap.recycle();
    }

}
