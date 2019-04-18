package com.zinc.animation.bean;

import android.animation.TimeInterpolator;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/24
 * @description
 */
public class TimeInterpolatorBean {

    private boolean isSelect;
    private String name;
    private TimeInterpolator timeInterpolator;

    public TimeInterpolatorBean(boolean isSelect,
                                String name,
                                TimeInterpolator timeInterpolator) {
        this.isSelect = isSelect;
        this.name = name;
        this.timeInterpolator = timeInterpolator;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getName() {
        return name;
    }

    public TimeInterpolator getTimeInterpolator() {
        return timeInterpolator;
    }
}
