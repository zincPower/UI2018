package com.zinc.code_x2_recycleview.recycler;

import java.util.ArrayList;

public class ArrayListWrapper<T> extends ArrayList<T> {
    public int maxSize = 0;
    public boolean canReset = true;
    private int lastSize = 0;

    @Override
    public boolean remove(Object o) {
        if (size() > maxSize) {
            maxSize = size();
            canReset = false;
        }
        if (size() == 0) {
            canReset = true;
        }
        return super.remove(o);
    }

    @Override
    public boolean add(T t) {
        if (canReset) {
            if (size() + 1 > lastSize) {
                maxSize = size() + 1;
            }
        }
        return super.add(t);
    }
}
