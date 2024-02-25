package com.sketcher;

/**
 * @File
 * @Author Emily Weilan Tao
 * @Date July 6, 2021
 * @Description
 * @Since version-1.0
 * @Copyright Copyright (c) 2020
 */
public class KernelTable {
    private static final float NINTH = 1.0f / 9.0f;
    private static final float[] blurKernel = {
            NINTH, NINTH, NINTH,
            NINTH, NINTH, NINTH,
            NINTH, NINTH, NINTH
    };
    private static final float[] KERNEL =
            {
                    1.0f, 0.0f, -1.0f,
                    1.0f, 0.0f, -1.0f,
                    1.0f, 0.0f, -1.0f,

            };
    private static final float[] KERNEL_1 =
            {
                    1.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 0.0f,
                    -1.0f, -1.0f, -1.0f

            };
    private static final float[] KERNEL_2 =
            {
                    1.0f, 1.0f, 0.0f,
                    1.0f, 0.0f, -1.0f,
                    0.0f, -1.0f, -1.0f,
            };
    private static final float[] KERNEL_3 =
            {
                    0.0f, -1.0f, -1.0f,
                    1.0f, 0.0f, -1.0f,
                    1.0f, 1.0f, 0.0f,
            };
    private static final float[] sharp_kernel =
            {
                    0.0f, -1.0f, 0.0f,
                    -1.0f, 5.0f, -1.0f,
                    0.0f, -1.0f, 0.0f
            };


    public static float[] getBlurKernel() {
        return blurKernel;
    }

    public static float[] getKERNEL() {
        return KERNEL;
    }

    public static float[] getKernel1() {
        return KERNEL_1;
    }

    public static float[] getKernel2() {
        return KERNEL_2;
    }

    public static float[] getKernel3() {
        return KERNEL_3;
    }

    public static float[] getSharpKernel() {
        return sharp_kernel;
    }
}
