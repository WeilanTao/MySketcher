import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
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
    private int[][] sorcegsMap;


    public StrokeLine(int[][] greyScaleMap, int imagewidth, int imageheight) {
        kernels.add(KERNEL_0);
        kernels.add(KERNEL_90);
        kernels.add(KERNEL_45);
        kernels.add(KERNEL_135);
        sorcegsMap = greyScaleMap;

        imageWidth = imagewidth;
        imageHight = imageheight;

        doConvolution(sorcegsMap);

    }


    private static void doConvolution(int[][] sorcegsMap) {

        //get the kernel
        for (int i = 0; i < 4; i++) {

            int k[][] = kernels.get(i);

            for (int j = 0; j < K_R; j++) {
                for (int l = 0; l < K_C; l++) {
                    System.out.println(k[j][l]);
                }
            }

            //TODO StrokeLineDrawerOneDirection constructor involve kernel

            StrokeLineDrawerOneDirection strokeLineDrawer = new StrokeLineDrawerOneDirection(sorcegsMap, imageWidth, imageHight);
            BufferedImage stroklines = strokeLineDrawer.getStrokeLineImg();
            kernelImageMap.put(k, stroklines);
        }


        //discrete convolution


    }



}
