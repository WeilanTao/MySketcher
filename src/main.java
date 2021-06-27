import javax.imageio.ImageIO;
import java.awt.*;
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
    public static final String SOURCE_FILE = "./resource/xjj.PNG";
    public static final String DESTINATION_FILE = "./resource/out_xjj.PNG";
    private static final double RGB_LIMIT = 255.00;
    private static int imageWidth;
    private static int imageHeight;

    public static void main(String[] args) {
        BufferedImage originalImage = null;
        try {

            originalImage = ImageIO.read(new File(SOURCE_FILE));
            imageWidth = originalImage.getWidth();
            imageHeight = originalImage.getHeight();
            BufferedImage resultImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);

            grayScaleImg(originalImage, resultImage);

            File outputFile = new File(DESTINATION_FILE);
            ImageIO.write(resultImage, "PNG", outputFile);
        } catch (IOException e) {
        }
    }

    /**
     * Rendering the pic pixel by pixel
     *
     * @param originalImage
     * @param resultImage
     */
    private static void grayScaleImg(BufferedImage originalImage, BufferedImage resultImage) {
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                int oRGB = originalImage.getRGB(i, j);
//                System.out.println(grayScalePixel(oRGB));
                resultImage.setRGB(i, j, grayScalePixel(oRGB).getRGB());
//                grayScalePixle(resultImage.getRGB(i,j));
            }
        }

    }

    /**
     * Extract red, green blue
     *
     * @param rgbVal
     * @return new RGB
     */
    private static Color grayScalePixel(int rgbVal) {

        int red = (rgbVal & 0x00FF0000) >> 16;
        int blue = rgbVal & 0x000000FF;
        int green = (rgbVal & 0x0000FF00) >> 8;
        int nRGBInt = grayScaling(red, green, blue);

        return createRGB(nRGBInt);
    }

    /**
     * Compress the RGB color based on gamma expansion
     *
     * @param r
     * @param g
     * @param b
     * @return linear greyscale representation of its luminance
     */
    private static int grayScaling(int r, int g, int b) {

        double scaledr = r / RGB_LIMIT;
        double scaledg = g / RGB_LIMIT;
        double scaledb = b / RGB_LIMIT;

        double linearr = gammaCompress(scaledr);
        double linearg = gammaCompress(scaledg);
        double linearb = gammaCompress(scaledb);


        double y = (0.2126 * linearr + 0.7152 * linearg + 0.07722 * linearb);

        int res = inverseGammaExpansion(y);

        return res;
    }


    /**
     * @param c
     * @return gammaCompress red; green; blue parameter
     */
    private static double gammaCompress(double c) {
        double linear = c < 0.04045 ? c / 12.92 : Math.pow(((c + 0.055) / 1.055), 2.4);
        return linear;
    }

    /**
     * Convert y back to sRGB
     *
     * @param y
     * @return
     */
    private static int inverseGammaExpansion(double y) {
        double srgby = (y <= 0.0031308) ? 12.92 * y : (1.055 * Math.pow(y, (1 / 2.4)) - 0.055);
        srgby *= RGB_LIMIT;
        return (int) srgby;
    }

    /**
     * Convert the new grayscaled value to RGB
     *
     * @param rgbint
     * @return
     */
    private static Color createRGB(int rgbint) {
//        System.out.println(rgbint);
        int red;
        int blue;
        int green;
        if(rgbint<=255){
             red=green=blue=rgbint;

        }else{
             red = (rgbint & 0x00FF0000) >> 16;
             blue = rgbint & 0x000000FF;
             green = (rgbint & 0x0000FF00) >> 8;

        }

        Color c=new Color(red,blue,green);
        return c;
    }
}
