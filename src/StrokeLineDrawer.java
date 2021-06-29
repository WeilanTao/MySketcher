import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @File
 * @Author Emily Weilan Tao
 * @Date Jun 27, 2021
 */

public class StrokeLineDrawer {
    private static int imgWeight;
    private static int imgHight;
    private static int[][] kernel;
    private static final int K_R=7;
    private static final int K_C=7;
    private static int[][] inputgsmap;
    private BufferedImage StrokeLineImg;
    private static int[][] resultGrayScale;

    public StrokeLineDrawer(int[][] grayscalemap, int imgweight, int imghight) {

        imgHight = imghight;
        imgWeight = imgweight;
///////////////////////////////////////////////////////////
        int [][] kernels={{1,1,1,1,1,1,1},{1,1,1,1,1,1,1},{1,1,1,1,1,1,1},{0,0,0,0,0,0,0},{-1,-1,-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1,-1,-1}};
        kernel = kernels;

        resultGrayScale=new int[imgWeight][imgHight];

///////////////////////////////////////////////////////////////
        inputgsmap = grayscalemap;

        convolutionThroughTheInputImg();

        StrokeLineImg=new BufferedImage(imgWeight,imgHight,BufferedImage.TYPE_INT_RGB);

        for(int i =0; i<imgWeight; i++){
            for(int j =0; j<imgHight; j++){
                StrokeLineImg.setRGB(i, j, createRGB((resultGrayScale[i][j])).getRGB());
            }
        }

    }

    public BufferedImage getStrokeLineImg() {
        return StrokeLineImg;
    }

//    private void GradientMap(int[][] gsmap) {
//        for (int i = 1; i < imgWeight - 1; i++) {
//            for (int j = 0; j < imgHight; j++) {
//                double gradX = Math.abs(gsmap[i + 1][j] - gsmap[i - 1][j]);
//                double gradY = Math.abs(gsmap[i][j + 1] - gsmap[i][j - 1]);
//                gradientmap[i][j] = Math.pow(Math.pow(gradX, 2) + Math.pow(gradY, 2), 0.5);
//            }
//        }
//    }
    private static void  OnePixelConvolution(int[][] sample, int x, int y) {
        int res = 0;
        for (int i = 0; i < K_R; i++) {
            for (int j = 0; j < K_C; j++) {
                res = res + sample[i][j] * kernel[i][j];
            }
        }
        resultGrayScale[x][y] = res;
    }

    public static void convolutionThroughTheInputImg() {
        int horizontalWalk = imgWeight - K_R;
        int verticalWalk = imgHight - K_C;

        for (int i = 0; i < horizontalWalk; i++) {
            for (int j = 0; j < verticalWalk; j++) {
                int sample[][] =new int[K_R][K_C];
                for(int k=i; k<K_R+i; k++){
                    for(int m=j; m<K_C+j;m++){
                        sample[k-i][m-j]=inputgsmap[k][m];
                        OnePixelConvolution(sample,i,j);
                    }
                }
            }
        }
    }

    private static int inverseGammaExpansion(double y) {
        double srgby = (y <= 0.0031308) ? 12.92 * y : (1.055 * Math.pow(y, (1 / 2.4)) - 0.055);
        srgby *= 255;
        return (int) srgby;
    }

    private static Color createRGB(int rgbint) {
        System.out.println(rgbint);

        int red;
        int blue;
        int green;
        System.out.println(rgbint);

        if (rgbint <= 255) {
            red = green = blue = rgbint;
        } else {
            red = (rgbint & 0x00FF0000) >> 16;
            blue = rgbint & 0x000000FF;
            green = (rgbint & 0x0000FF00) >> 8;
        }
        red=correctColor(red);
        green=correctColor(green);
        blue=correctColor(blue);

        Color c = new Color(red, blue, green);
        return c;
    }


    private static int correctColor( int color){
        int c;
        if(color>255){
            c=255;
        }else if(color<0){
            c=0;
        }else{
            c=color;
        }
        return c;
    }

}
