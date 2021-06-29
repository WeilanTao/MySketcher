import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Create the convoluted img in one direction
 *
 * @File
 * @Author Emily Weilan Tao
 * @Date Jun 27, 2021
 */

public class StrokeLineDrawerOneDirection {
    private static final int K_R = 7;
    private static final int K_C = 7;
    private static int imgWeight;
    private static int imgHight;
    private static int[][] kernel;
    private static int[][] inputgsmap;
    private static int[][] resultGrayScale;
    private BufferedImage StrokeLineImg;
    private int[][] gradientmap;

    public StrokeLineDrawerOneDirection(int[][] grayscalemap, int imgweight, int imghight, int[][] kernel_in_one_dir) {

        imgHight = imghight;
        imgWeight = imgweight;
        gradientmap = new int[imgWeight][imgHight];
        GradientMap(grayscalemap);

        kernel = kernel_in_one_dir;

        resultGrayScale = new int[imgWeight][imgHight];

        inputgsmap = gradientmap;

        convolutionThroughTheInputImg();
        StrokeLineImg = new BufferedImage(imgWeight, imgHight, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < imgWeight; i++) {
            for (int j = 0; j < imgHight; j++) {
                StrokeLineImg.setRGB(i, j, createRGB((resultGrayScale[i][j])).getRGB());
            }
        }

    }

    /**
     * Convolution on each pixel
     *
     * @param sample
     * @param x
     * @param y
     */
    private static void OnePixelConvolution(int[][] sample, int x, int y) {
        int res = 0;
        for (int i = 0; i < K_R; i++) {
            for (int j = 0; j < K_C; j++) {
                res = res + sample[i][j] * kernel[i][j];
            }
        }
        resultGrayScale[x][y] = res;
    }

    /**
     * split the image into several parts and apply convolution to each part
     */
    public static void convolutionThroughTheInputImg() {
        int horizontalWalk = imgWeight - K_R;
        int verticalWalk = imgHight - K_C;

        for (int i = 0; i < horizontalWalk; i++) {
            for (int j = 0; j < verticalWalk; j++) {
                int sample[][] = new int[K_R][K_C];
                for (int k = i; k < K_R + i; k++) {
                    for (int m = j; m < K_C + j; m++) {
                        sample[k - i][m - j] = inputgsmap[k][m];
                        OnePixelConvolution(sample, i, j);
                    }
                }
            }
        }
    }

    /**
     * Convert the convoluted inversed gamma expression into the RGB color
     *
     * @param rgbint
     * @return
     */
    private static Color createRGB(int rgbint) {
        System.out.println(rgbint);

        int red;
        int blue;
        int green;

        if (rgbint <= 255) {
            red = rgbint;
        } else {
            red = (rgbint & 0x00FF0000) >> 16;
        }

        red = correctColor(red);
        green = red;
        blue = red;

        Color c = new Color(red, blue, green);
        return c;
    }

    /**
     * Filter out too larger numbers and to negative numbers
     *
     * @param color
     * @return
     */
    private static int correctColor(int color) {
        int c;
        if (color > 255) {
            c = 255;
        } else if (color < 0) {
            c = 0;
        } else {
            c = color;
        }
        return c;
    }

    private void GradientMap(int[][] gsmap) {
        for (int i = 1; i < imgWeight - 1; i++) {
            for (int j = 1; j < imgHight - 1; j++) {
                double gradX = Math.abs(gsmap[i + 1][j] - gsmap[i - 1][j]);
                double gradY = Math.abs(gsmap[i][j + 1] - gsmap[i][j - 1]);
                System.out.println("i： "+ i+ " j: "+j);
                gradientmap[i][j] = (int) Math.pow(Math.pow(gradX, 2) + Math.pow(gradY, 2), 0.5);
            }
        }
    }

    /**
     * return Stroke Line Img(in one direction)
     *
     * @return
     */
    public BufferedImage getStrokeLineImg() {
        return StrokeLineImg;
    }

}
