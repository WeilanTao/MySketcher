import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Texture {
    private BufferedImage inputImage;
    private BufferedImage txter;
    private static int imageWidth;
    private static int imageHeight;
    private static TexturePaint texturePaint;
    private static BufferedImage combined;
    private static BufferedImage blank;


    public Texture(BufferedImage tonedInput) throws IOException {
        imageWidth=tonedInput.getWidth();
        imageHeight=tonedInput.getHeight();

        inputImage=new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        blank= new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = inputImage.getGraphics();
        g.drawImage(tonedInput, 0, 0, null);
        g.dispose();
        txter =  ImageIO.read(new File("./resource/pattern.jpg"));

        combined=new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);


        texturePaint=new TexturePaint(txter, new Rectangle(0, 0, imageWidth, imageHeight));

        renderTexture();
    }

    private void renderTexture(){
        Graphics2D g2d = blank.createGraphics();
        g2d.setPaint(texturePaint);
        g2d.fill(new Rectangle(0, 0, imageWidth, imageHeight));
        int alpha=255;
        for(int i = 0; i<imageWidth; i++){
            for(int j =0; j<imageHeight; j++){
                int rgb = blank.getRGB(i,j);
                int red = (rgb & 0x00FF0000) >> 16;

                int tonedrgb = inputImage.getRGB(i,j);
                int tonedred = (tonedrgb & 0x00FF0000) >> 16;
                if(red>100 || tonedred >230 || tonedred< 30){
                    alpha=0;
                }else{
                    alpha=255;
                }

                rgb =(alpha<<24| (rgb & 0x00ffffff));
                blank.setRGB(i,j,rgb);
            }
        }

        Graphics g1 = combined.getGraphics();
        g1.drawImage(inputImage, 0, 0, null);
        g1.drawImage(blank, 0, 0, null);
        g1.dispose();
    }

    public static BufferedImage getCombined() {
        return combined;
    }
}
