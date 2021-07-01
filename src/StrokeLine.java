import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class StrokeLine {
    private static final int K_R = 7;
    private static final int K_C = 7;
    private static final int[][] KERNEL_0 = {{1, 1, 1, 0, -1, -1, -1}, {1, 1, 1, 0, -1, -1, -1}, {1, 1, 1, 0, -1, -1, -1}, {1, 1, 1, 0, -1, -1, -1}, {1, 1, 1, 0, -1, -1, -1}, {1, 1, 1, 0, -1, -1, -1}, {1, 1, 1, 0, -1, -1, -1}};
    private static final int[][] KERNEL_90 = {{1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {0, 0, 0, 0, 0, 0, 0}, {-1, -1, -1, -1, -1, -1, -1}, {-1, -1, -1, -1, -1, -1, -1}, {-1, -1, -1, -1, -1, -1, -1}};
    private static final int[][] KERNEL_45 = {{1, 1, 1, 1, 1, 1, 0}, {1, 1, 1, 1, 1, 0, -1}, {1, 1, 1, 1, 0, -1, -1}, {1, 1, 1, 0, -1, -1, -1}, {1, 1, 0, -1, -1, -1, -1}, {1, 0, -1, -1, -1, -1, -1}, {0, -1, -1, -1, -1, -1, -1}};
    private static final int[][] KERNEL_135 = {{0, -1, -1, -1, -1, -1, -1}, {1, 0, -1, -1, -1, -1, -1}, {1, 1, 0, -1, -1, -1, -1}, {1, 1, 1, 0, -1, -1, -1}, {1, 1, 1, 1, 0, -1, -1}, {1, 1, 1, 1, 1, 0, -1}, {1, 1, 1, 1, 1, 1, 0}};
    private static List<int[][]> kernels = new ArrayList<>();
    private static Hashtable<int[][], BufferedImage> kernelImageMap = new Hashtable<>(); //for thread safety
    private static int imageWidth;
    private static int imageHight;
    private static int [][] sorcegsMap;

    private static int[][] CMap_0;
    private static int[][] CMap_90;
    private static int[][] CMap_45;
    private static int[][] CMap_135;
    private static int[][] SMap;

    private static BufferedImage result;



    public StrokeLine(int[][] greyScaleMap, int imagewidth, int imageheight) throws IOException {
        imageWidth = imagewidth;
        imageHight = imageheight;
        kernels.add(KERNEL_0);
        kernels.add(KERNEL_90);
        kernels.add(KERNEL_45);
        kernels.add(KERNEL_135);
        sorcegsMap = greyScaleMap;
        CMap_0=new int[imageWidth][imageHight];
        CMap_90=new int[imageWidth][imageHight];
        CMap_45=new int[imageWidth][imageHight];
        CMap_135=new int[imageWidth][imageHight];
        SMap=new int[imageWidth][imageHight];
        result=new BufferedImage(imageWidth, imageHight, BufferedImage.TYPE_INT_RGB);


        doConvolution(sorcegsMap);


    }

    private static void doConvolution(int[][] sorcegsMap) throws IOException {

        //put the kernels in a list
        for (int i = 0; i < 4; i++) {

            int k[][] = kernels.get(i);

            StrokeLineDrawerOneDirection strokeLineDrawer = new StrokeLineDrawerOneDirection(sorcegsMap, imageWidth, imageHight, k);
            BufferedImage stroklines = strokeLineDrawer.getStrokeLineImg();
            kernelImageMap.put(k, stroklines);


//            String DESTINATION_FILE="./resource/iamok_"+i+".PNG";
//            File outputFile = new File(DESTINATION_FILE);
//            ImageIO.write(stroklines, "PNG", outputFile);
        }

        //Generate the CMap
        filterResponse();

        //TODO sharp the line~the cross Correlation
        sharpTheLine();

        //convert to RGB
        for(int i =0 ; i<imageWidth; i++){
            for(int j=0; j<imageHight; j++){
                result.setRGB(i,j,convertToRGB(SMap[i][j]).getRGB());
            }
        }

    }

    public static BufferedImage getResult(){
        return result;
    }

    private static Color convertToRGB(int rgbint) {
//        System.out.println(rgbint);

        int red;
        int blue;
        int green;

        red= rgbint <= 255 ? rgbint :(rgbint & 0x00FF0000) >> 16;

        if (red > 255) {
            red = 255;
        } else if (red < 0) {
            red = 0;
        }

        green = red;
        blue = red;

        Color c = new Color(red, blue, green);
        return c;
    }

    private static void sharpTheLine(){
        int[][] cC_0= crossCorrelation(KERNEL_0,CMap_0);
        int[][] cC_45=crossCorrelation(KERNEL_45,CMap_45);
        int[][] cC_90=crossCorrelation(KERNEL_90,CMap_90);
        int[][] cC_135=crossCorrelation(KERNEL_135,CMap_135);

        //generate S
        for(int i=0; i<imageWidth; i++){
            for(int j =0; j<imageHight; j++) {
//                System.out.println("i "+ i +" j " + j);
//                System.out.println("cC_0[i][j] "+ cC_0[i][j]);
//                System.out.println("cC_45[i][j] "+ cC_45[i][j]);
//                System.out.println("cC_90[i][j] "+ cC_90[i][j]);
//                System.out.println("cC_135[i][j] "+ cC_135[i][j]);
//                System.out.println("S[i][j] "+ S[i][j]);
                SMap[i][j] = cC_0[i][j]+cC_45[i][j]+cC_90[i][j]+cC_135[i][j];
//                SMap[i][j]=0;
            }
        }


    }

    private static int[][] crossCorrelation(int [][] kernel,int[][] CMapVal){
        int horizontalWalk = imageWidth - K_R;
        int verticalWalk = imageHight - K_C;
        int res[][]=new int[imageWidth][imageHight];
        for (int i = 0; i < horizontalWalk; i++) {
            for (int j = 0; j < verticalWalk; j++) {
                int sample[][] = new int[K_R][K_C];
                for (int k = i; k < K_R + i; k++) {
                    for (int m = j; m < K_C + j; m++) {
                        sample[k - i][m - j] = CMapVal[k][m];
                        OnePixelConvolution(sample, i, j, kernel, res);
                    }
                }
            }
        }

        return res;
    }

    private static void OnePixelConvolution(int[][] sample, int x, int y, int [][]kernel, int [][] res) {
        int resrgb = 0;
        for (int i = 0; i < K_R; i++) {
            for (int j = 0; j < K_C; j++) {
                resrgb = resrgb + sample[i][j] * kernel[i][j];
            }
        }
        res[x][y] = resrgb;
    }

    private static void filterResponse(){
        for(int i=0;  i<imageWidth; i++){
            for (int j =0; j<imageHight; j++){
                pixelMagnitude_Ci(i,j);
            }
        }

    }

    private static void pixelMagnitude_Ci(int i, int j){
        int min =1000;
        int index=-1;

        Iterator<int [][]> iterator=kernels.iterator();
        while(iterator.hasNext()){
            int temp = kernelImageMap.get(iterator.next()).getRGB(i,j);
            if(temp<min) {
                min = temp;
                index++;
            }
        }

        if(index>=0)
            min= sorcegsMap[i][j];
        else
            min=0;

        switch (index){
            case 0:
                CMap_0[i][j]=min;
                break;
            case 1:
                CMap_90[i][j]=min;
                break;
            case 2:
                CMap_45[i][j]=min;
                break;
            case 3:
                CMap_135[i][j]=min;
                break;
                default:
                    break;

        }
    }
}