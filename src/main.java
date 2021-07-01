import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @File
 * @Author Emily Weilan Tao
 * @Date
 * @Description
 * @Since version-1.0
 * @Copyright Copyright (c) 2020
 */
public class main {
    public static final String SOURCE_FILE = "./resource/cubs.PNG";
    public static final String DESTINATION_FILE = "./resource/out_xjj.PNG";
    private static int imageWidth;
    private static int imageHeight;


    public static void main(String[] args) {
        BufferedImage originalImage = null;
        try {

            originalImage = ImageIO.read(new File(SOURCE_FILE));
            imageWidth = originalImage.getWidth();
            imageHeight = originalImage.getHeight();

            GreyScaler greyScaler = new GreyScaler(originalImage);
            BufferedImage greyScalerResultImage = greyScaler.getResultImage();
            int[][] greyScaleMap = greyScaler.getGrayScale();
//            StrokeLineDrawerOneDirection strokeLineDrawer = new StrokeLineDrawerOneDirection(greyScaleMap, imageWidth, imageHeight);
//            BufferedImage stroklines= strokeLineDrawer.getStrokeLineImg();

            StrokeLine skpackage=new StrokeLine(greyScaleMap,imageWidth,imageHeight);
            BufferedImage strokeimage = skpackage.getResult();


            File outputFile = new File(DESTINATION_FILE);
            ImageIO.write(strokeimage, "PNG", outputFile);
        } catch (IOException e) {
        }
    }


}
