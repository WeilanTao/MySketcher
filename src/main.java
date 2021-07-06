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
    public static final String SOURCE_FILE = "./resource/cubs.png";
    public static final String DESTINATION_FILE = "./resource/cubs_stroke.PNG";
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

            Stroke stroke=new Stroke(greyScalerResultImage);
            BufferedImage stokeLine = stroke.getResult();

            Tone tone=new Tone(greyScalerResultImage);
            BufferedImage toned = tone.getResult();

            File outputFile = new File(DESTINATION_FILE);
            ImageIO.write(stokeLine, "PNG", outputFile);

        } catch (IOException e) {
        }
    }


}
