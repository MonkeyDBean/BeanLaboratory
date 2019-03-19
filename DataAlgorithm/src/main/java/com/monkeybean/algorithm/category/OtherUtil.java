package com.monkeybean.algorithm.category;

/**
 * Created by MonkeyBean on 2018/8/6.
 */
public final class OtherUtil {

    private OtherUtil() {
    }

    /**
     * 字符打印，输出心形
     *
     * @param isFull 是否输出完整心形，true为整个，false为半个
     */
    public static void printSimpleHeart(boolean isFull) {
        for (double y = 1.5; y > -1.5; y -= 0.1) {
            for (double x = -1.5; x < 1.5; x += 0.05) {
                double a = x * x + y * y - 1;
                if (a * a * a - x * x * y * y * y <= 0) {
                    System.out.print("*");
                } else {
                    if (isFull) {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
    }

    /**
     * 字符打印，输出一箭穿心图案
     */
    public static void printTwoHeart() {
        int i, j, e, a, t1;
        final int I = 8, R = 150;
        for (i = 1, a = I; i <= I / 2; i++, a--) {
            t1 = 6;
            while (t1-- != 0) System.out.print(" ");
            for (j = (int) (I - Math.sqrt(I * I - (a - i) * (a - i))); j > 0; j--)
                System.out.print(" ");
            for (e = 1; e <= 2 * Math.sqrt(I * I - (a - i) * (a - i)); e++)
                System.out.print("*");
            for (j = (int) (2 * (I - Math.sqrt(I * I - (a - i) * (a - i)))); j > 0; j--)
                System.out.print(" ");
            for (e = 1; e <= 2 * Math.sqrt(I * I - (a - i) * (a - i)); e++)
                System.out.print("*");
            for (j = (int) (I - Math.sqrt(I * I - (a - i) * (a - i))); j > 0; j--)
                System.out.print(" ");
            t1 = (int) (I - Math.sqrt(I * I - (a - i) * (a - i)));
            t1 = 2 * t1;
            t1 += (int) (2 * Math.sqrt(I * I - (a - i) * (a - i)));
            t1 = 20 - t1;
            while (t1-- != 0) System.out.print(" ");
            for (j = (int) (I - Math.sqrt(I * I - (a - i) * (a - i))); j > 0; j--)
                System.out.print(" ");
            for (e = 1; e <= 2 * Math.sqrt(I * I - (a - i) * (a - i)); e++)
                System.out.print("*");
            for (j = (int) (2 * (I - Math.sqrt(I * I - (a - i) * (a - i)))); j > 0; j--)
                System.out.print(" ");
            for (e = 1; e <= 2 * Math.sqrt(I * I - (a - i) * (a - i)); e++)
                System.out.print("*");
            for (j = (int) (I - Math.sqrt(I * I - (a - i) * (a - i))); j > 0; j--)
                System.out.print(" ");
            System.out.print("\n");
        }
        for (i = 1; i <= R / 2; i++) {
            if (i % 2 != 0 || i % 3 != 0) continue;
            t1 = 6;
            if (i == 6) System.out.print(">>----");
            else while (t1-- != 0) System.out.print(" ");
            for (j = (int) (R - Math.sqrt(R * R - i * i)); j > 0; j--)
                System.out.print(" ");
            for (e = 1; e <= 2 * (Math.sqrt(R * R - i * i) - (R - 2 * I)); e++)
                System.out.print("*");
            for (j = (int) (R - Math.sqrt(R * R - i * i)); j > 0; j--)
                System.out.print(" ");
            t1 = (int) (R - Math.sqrt(R * R - i * i));
            t1 = 2 * t1;
            t1 += (int) (2 * (Math.sqrt(R * R - i * i) - (R - 2 * I)));
            t1 = 35 - t1;
            if (i == 6)
                System.out.print("LOVE");
            else if (i == 48)
                System.out.print("\t ");
            else
                while (t1-- != 0) System.out.print(" ");
            for (j = (int) (R - Math.sqrt(R * R - i * i)); j > 0; j--)
                System.out.print(" ");
            for (e = 1; e <= 2 * (Math.sqrt(R * R - i * i) - (R - 2 * I)); e++)
                System.out.print("*");
            for (j = (int) (R - Math.sqrt(R * R - i * i)); j > 0; j--)
                System.out.print(" ");
            if (i == 6) System.out.print("----->");
            System.out.print("\n");
        }
    }

}
