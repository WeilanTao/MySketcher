public class KernelTable  {
    private static final float ninth = 1.0f / 9.0f;
    private static final float[] blurKernel = {
            ninth, ninth, ninth,
            ninth, ninth, ninth,
            ninth, ninth, ninth
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

    public static float[] getSharp_kernel() {
        return sharp_kernel;
    }
}