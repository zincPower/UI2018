package com.zinc.bezier.utils;

import android.graphics.PointF;
import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/5
 * @description 贝塞尔曲线工具
 */
public class BezierUtils {

    // x轴坐标
    public static final int X_TYPE = 1;
    // y轴坐标
    public static final int Y_TYPE = 2;

    /**
     * 构建贝塞尔曲线，具体点数由 参数frame 决定
     *
     * @param controlPointList 控制点的坐标
     * @param frame            帧数
     * @return
     */
    public static List<PointF> buildBezierPoint(List<PointF> controlPointList,
                                                int frame) {
        List<PointF> pointList = new ArrayList<>();

        // 此处注意，要用1f，否则得出结果为0
        float delta = 1f / frame;

        // 阶数，阶数=绘制点数-1
        int order = controlPointList.size() - 1;

        // 循环递增
        for (float u = 0; u <= 1; u += delta) {
            pointList.add(new PointF(BezierUtils.calculatePointCoordinate(BezierUtils.X_TYPE, u, order, 0, controlPointList),
                    BezierUtils.calculatePointCoordinate(BezierUtils.Y_TYPE, u, order, 0, controlPointList)));
        }

        return pointList;

    }

    /**
     * 计算坐标 [贝塞尔曲线的核心关键]
     *
     * @param type             {@link #X_TYPE} 表示x轴的坐标， {@link #Y_TYPE} 表示y轴的坐标
     * @param u                当前的比例
     * @param k                阶数
     * @param p                当前坐标（具体为 x轴 或 y轴）
     * @param controlPointList 控制点的坐标
     * @return
     */
    public static float calculatePointCoordinate(@IntRange(from = X_TYPE, to = Y_TYPE) int type,
                                                 float u,
                                                 int k,
                                                 int p,
                                                 List<PointF> controlPointList) {

        /**
         * 公式解说：（p表示坐标点，后面的数字只是区分）
         * 场景：有一条线p1到p2，p0在中间，求p0的坐标
         *      p1◉--------○----------------◉p2
         *            u    p0
         *
         * 公式：p0 = p1+u*(p2-p1) 整理得出 p0 = (1-u)*p1+u*p2
         */

        // 一阶贝塞尔，直接返回
        if (k == 1) {

            float p1;
            float p2;

            // 根据是 x轴 还是 y轴 进行赋值
            if (type == X_TYPE) {
                p1 = controlPointList.get(p).x;
                p2 = controlPointList.get(p + 1).x;
            } else {
                p1 = controlPointList.get(p).y;
                p2 = controlPointList.get(p + 1).y;
            }

            return (1 - u) * p1 + u * p2;

        } else {

            /**
             * 这里应用了递归的思想：
             * 1阶贝塞尔曲线的端点 依赖于 2阶贝塞尔曲线
             * 2阶贝塞尔曲线的端点 依赖于 3阶贝塞尔曲线
             * ....
             * n-1阶贝塞尔曲线的端点 依赖于 n阶贝塞尔曲线
             *
             * 1阶贝塞尔曲线 则为 真正的贝塞尔曲线存在的点
             */
            return (1 - u) * calculatePointCoordinate(type, u, k - 1, p, controlPointList)
                    + u * calculatePointCoordinate(type, u, k - 1, p + 1, controlPointList);

        }

    }

    /**
     * 计算中间降阶过程的绘制点
     *
     * @param intermediateList 层级说明：
     *                         第1层list.存放每一阶的值
     *                         ------即：intermediateList.get(0) 即为第(n-1)阶的贝塞尔曲线的数值
     *                         ------intermediateList.get(1) 即为第(n-2)阶的贝塞尔曲线的数值
     *                         第2层list.存放该阶的每条边的数据
     *                         第3层list.存放这条边点的数据
     *                         <p>
     *                         举个例子：
     *                         一个三阶的贝塞尔曲线；
     *                         第一层：
     *                         --- mOrderPointList.get(0) 存放的是第2阶的数据
     *                         --- mOrderPointList.get(1) 存放的是第1阶的数据（即最终贝塞尔曲线的数据）
     *                         第二层：
     *                         --- mOrderPointList.get(0).get(0) 存放的是第2阶中第一条边的数据
     *                         第三层：
     *                         --- mOrderPointList.get(0).get(0).get(0) 存放的是第二阶中第一条边的第一个点的数据
     * @param controlPointList 控制点的坐标
     * @param frame            帧数
     */
    public static void calculateIntermediateLine(List<List<List<PointF>>> intermediateList,
                                                 List<PointF> controlPointList,
                                                 int frame) {
        intermediateList.clear();

        // 获取阶数
        int order = controlPointList.size() - 1;
        // 每次增加的偏量
        float delta = 1f / frame;

        // i 的取值范围必须为 [0, order-1]，
        // order-1是因为n阶的贝塞尔曲线只需要计算n-1次就可以，
        // 因为最高阶不需要计算，已经直接绘制
        for (int i = 0; i < order - 1; ++i) {

            List<List<PointF>> orderPointList = new ArrayList<>();

            // 终止条件为每一阶的边的条数，阶数与边数相等
            // 随着i的增大，即阶数的降低，相应的需要计算的边数对应减少
            for (int j = 0; j < order - i; ++j) {

                List<PointF> pointList = new ArrayList<>();

                // 计算每个偏移量的点
                for (float u = 0; u <= 1; u += delta) {

                    /**
                     *  p1(x,y)◉--------○----------------◉p2(x,y)
                     *            u    p0(x,y)
                     */
                    float p1x;
                    float p1y;
                    float p2x;
                    float p2y;

                    // 上一阶中，对应的当前帧的下标
                    int beforeOrderCurPointIndex = (int) (u * frame);

                    // 当 mIntermediateList==0 时，说明要依赖于 最高阶的控制基线绘制
                    if (intermediateList.size() == 0) {
                        p1x = controlPointList.get(j).x;
                        p1y = controlPointList.get(j).y;
                        p2x = controlPointList.get(j + 1).x;
                        p2y = controlPointList.get(j + 1).y;
                    } else { // 当 mIntermediateList!=0 时，说明要依赖于 上一阶的控制基线进行绘制
                        p1x = intermediateList.get(i - 1).get(j).get(beforeOrderCurPointIndex).x;
                        p1y = intermediateList.get(i - 1).get(j).get(beforeOrderCurPointIndex).y;
                        p2x = intermediateList.get(i - 1).get(j + 1).get(beforeOrderCurPointIndex).x;
                        p2y = intermediateList.get(i - 1).get(j + 1).get(beforeOrderCurPointIndex).y;
                    }

                    /**
                     * 这里的公式 和{@link BezierUtils#calculatePointCoordinate(int, float, int, int, List)} 的原理是一样的，
                     * 只是不用递归，直接计算当前阶数的点即可
                     */
                    float p0x = (1 - u) * p1x + u * p2x;
                    float p0y = (1 - u) * p1y + u * p2y;

                    pointList.add(new PointF(p0x, p0y));

                }

                orderPointList.add(pointList);

            }

            intermediateList.add(orderPointList);

        }
    }

}
