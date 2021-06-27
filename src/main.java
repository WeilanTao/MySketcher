import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class main {
    public static final String SOURCE_FILE = "./resource/xjj.PNG";
    public static final String DESTINATION_FILE = "./resource/out_xjj.PNG";
    private static int imageWidth;
    private static int imageHeight;

    public static void main(String[] args) {
        BufferedImage originalImage = null;
        try {

            originalImage = ImageIO.read(new File(SOURCE_FILE));
            imageWidth=originalImage.getWidth();
            imageHeight=originalImage.getHeight();
            BufferedImage resultImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
//            BufferedImage resultImage =originalImage;
            grayScaleImg(originalImage, resultImage);

            File outputFile = new File(DESTINATION_FILE);
            ImageIO.write(resultImage, "PNG", outputFile);
        } catch (IOException e) {
        }
    }

    private static void grayScaleImg(BufferedImage originalImage,BufferedImage resultImage){
        for(int i =0; i<imageWidth;i++){
            for(int j =0; j<imageHeight;  j++){
                int oRGB=originalImage.getRGB(i,j);
                resultImage.setRGB(i,j,grayScalePixle(oRGB));
//                grayScalePixle(resultImage.getRGB(i,j));
            }
        }

    }

    private static int grayScalePixle(int rgbVal){

        int red=(rgbVal & 0x00FF0000) >> 16;;
        int blue=rgbVal & 0x000000FF;
        int green=(rgbVal & 0x0000FF00) >> 8;
        int nRGB= grayScaling(red, green, blue);

        return nRGB;
    }

    private static int grayScaling(int r, int g, int b){


        return 0;
    }
}


