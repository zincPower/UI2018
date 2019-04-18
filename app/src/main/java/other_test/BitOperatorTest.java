//package other_test;
//
///**
// * @author Jiang zinc
// * @date 创建时间：2018/11/6
// * @description
// */
//public class BitOperatorTest {
//
//    public static void main(String[] args) {
//
//        System.out.println("0 & 0 = " + (0 & 0));
//        System.out.println("0 & 1 = " + (0 & 1));
//        System.out.println("1 & 0 = " + (1 & 0));
//        System.out.println("1 & 1 = " + (1 & 1));
//        System.out.println();
//
//        System.out.println("0 | 0 = " + (0 | 0));
//        System.out.println("0 | 1 = " + (0 | 1));
//        System.out.println("1 | 0 = " + (1 | 0));
//        System.out.println("1 | 1 = " + (1 | 1));
//        System.out.println();
//
//        System.out.println("～1 = " + ~(1));
//        System.out.println("～0 = " + ~(0));
//        System.out.println();
//
//        int right = 0x001;
//        int bottom = 0x002;
//        System.out.println("right | bottom = " + (right | bottom));
//        System.out.println();
//
//        int top = 0x008;
//        int state = right | bottom;
//        System.out.println("是否存在 right = " + ((state & right) == right));
//        System.out.println("是否存在 top = " + ((state & top) == top));
//        System.out.println();
//
//        System.out.println("剔除 right 状态前 " + state);
//        state &= ~right;
//        System.out.println("剔除 right 状态后 " + state);
//        state &= ~top;
//        System.out.println("剔除不存在的 top 状态后 " + state);
//        System.out.println();
//
//    }
//
//}
