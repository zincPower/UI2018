package com.zinc.code_x2_recycleview.recycler;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/2/27
 * @description 数据
 */
public class RecyclerBean<T> {

    public static final int TYPE_ONE = 0;
    public static final int TYPE_TWO = 1;
    public static final int TYPE_THREE = 2;
    public static final int TYPE_FOUR = 3;

    private int type;
    private T data;

    public RecyclerBean(int type, T data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
