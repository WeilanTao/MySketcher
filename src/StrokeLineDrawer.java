import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @File
 * @Author Emily Weilan Tao
 * @Date Jun 27, 2021
 */

public class StrokeLineDrawer {
    private static double[][] gradientmap;
    private static double[][] G;
    private static int imgWeight;
    private static int imgHight;
    private static int[][] kernel;
    private static int[][] inputgsmap;
    private BufferedImage StrokeLineImg;
    private static int[][] resultGrayScale;

    public StrokeLineDrawer(int[][] grayscalemap, int imgweight, int imghight) {


//       inputgdmap = new double[imgweight][imgHight];
//       inputgdmap = grayscalemap;

        imgHight = imghight;
        imgWeight = imgweight;
///////////////////////////////////////////////////////////
        kernel = new int[466][291];
        resultGrayScale=new int[imgWeight][imgHight];
        for (int i = 0; i < 466; i++) {
            for (int j = 0; j < 145; j++) {
                kernel[i][j] = 1;
            }
        }
        for (int i = 0; i < 466; i++) {
            for (int j = 146; j < 291; j++) {
                kernel[i][j] = -1;
            }
        }

        for (int i = 0; i < 466; i++) {
            kernel[i][145] = 0;
        }
///////////////////////////////////////////////////////////////
        inputgsmap = grayscalemap.clone();

        convolutionThroughTheInputImg();

        StrokeLineImg=new BufferedImage(imgWeight,imgHight,BufferedImage.TYPE_INT_RGB);

        for(int i =0; i<imgWeight; i++){
            for(int j =0; j<imgHight; j++){
//                System.out.println(createRGB(resultGrayScale[i][j]).getRGB());
                StrokeLineImg.setRGB(i, j, createRGB(resultGrayScale[i][j]).getRGB());
            }
        }

    }

    public BufferedImage getStrokeLineImg() {
        return StrokeLineImg;
    }

    private void GradientMap(int[][] gsmap) {
        for (int i = 1; i < imgWeight - 1; i++) {
            for (int j = 0; j < imgHight; j++) {
                double gradX = Math.abs(gsmap[i + 1][j] - gsmap[i - 1][j]);
                double gradY = Math.abs(gsmap[i][j + 1] - gsmap[i][j - 1]);
                gradientmap[i][j] = Math.pow(Math.pow(gradX, 2) + Math.pow(gradY, 2), 0.5);
            }
        }
    }


    private static void  OnePixelConvolution(int[][] sample, int x, int y) {
        int res = 0;
        for (int i = 0; i < 466; i++) {
            for (int j = 0; j < 291; j++) {
                res = res + sample[i][j] * kernel[i][j];
            }
        }
        resultGrayScale[x][y] = res;
    }

    public static void convolutionThroughTheInputImg() {
        int horizontalWalk = imgWeight - 466;
        int verticalWalk = imgHight - 291;

        for (int i = 0; i < horizontalWalk; i++) {
            for (int j = 0; j < verticalWalk; j++) {
                int sample[][] =new int[466][291];
                for(int k=i; k<466+i; k++){
                    for(int m=j; m<291+j;m++){
                        System.out.println("I am convoluting"+k+" "+m);

                        sample[k-i][m-j]=inputgsmap[k][m];
                        OnePixelConvolution(sample,i,j);
//                        inputgsmap[k][m]=sample[k][m];
                    }
                }
            }
        }
    }

    private static Color createRGB(int rgbint) {
        int red;
        int blue;
        int green;

        if (rgbint <= 255) {
            red = green = blue = rgbint;
        } else {
            red = (rgbint & 0x00FF0000) >> 16;
            blue = rgbint & 0x000000FF;
            green = (rgbint & 0x0000FF00) >> 8;
        }
        System.out.println(red+"   "+blue+ "   "+green);

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
