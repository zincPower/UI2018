//package other_test;
//
///**
// * @author Jiang zinc
// * @date 创建时间：2018/11/7
// * @description
// */
//public class SynchronizedTest {
//
//    public static void main(String[] args) {
//        System.out.println(A.getDefault().toString());
//    }
//
//    public static class A {
//
//        private static B instance;
//
//        /**
//         * 获取 UserModel（如果为第一次，会进行一次从SharePreference中获取，具体看{@link #refresh}）
//         *
//         * @return
//         */
//        public static B getDefault() {
//            if (instance == null) {
//                synchronized (B.class) {
//                    if (instance == null) {
//                        refresh();
//                    }
//                }
//            }
//            return instance;
//        }
//
//        /**
//         * 刷新内存中的数据，重新从 SharePreference 获取
//         */
//        public static void refresh() {
//            synchronized (B.class) {
//                instance = new B("zinc");
//            }
//        }
//    }
//
//    public static class B {
//        private String name;
//
//        public B(String name) {
//            this.name = name;
//        }
//
//        @Override
//        public String toString() {
//            return "B{" +
//                    "name='" + name + '\'' +
//                    '}';
//        }
//    }
//
//}
