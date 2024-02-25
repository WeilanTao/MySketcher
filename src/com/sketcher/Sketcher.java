import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * @File
 * @Author Emily Weilan Tao
 * @Date
 * @Description
 * @Since version-1.0
 * @Copyright Copyright (c) 2020
 */
public class Sketcher {
    public static final String SOURCE_FILE = "./resource/xjj.png";
    public static final String DESTINATION_FILE = "./resource/xjj_out.PNG";

    public static void main(String[] args) {
        BufferedImage originalImage = null;

        try {

            originalImage = ImageIO.read(new File(SOURCE_FILE));

            long startTime = System.currentTimeMillis();


            GreyScaler greyScaler = new GreyScaler(originalImage);
            BufferedImage greyScalerResultImage = greyScaler.getResultImage();

            Future<BufferedImage> futureblendedTxtre;
            Future<Stroke> futurestroke;
            ExecutorService executorService = Executors.newCachedThreadPool();

            Callable<BufferedImage> colorer = () -> {
                Tone tone = new Tone(greyScalerResultImage);
                BufferedImage toned = tone.getResult();
                Texture texture = new Texture(toned);
                return texture.getCombined();
            };

            Callable<Stroke> stroker = () -> {
                Stroke stroke = new Stroke(greyScalerResultImage);
                BufferedImage stokeLine = stroke.getResult();
                return stroke;
            };

            futureblendedTxtre = executorService.submit(colorer);
            futurestroke = executorService.submit(stroker);

            executorService.shutdown();

            BufferedImage resoultImg = (futurestroke.get()).blendStroke(futureblendedTxtre.get());

            long endTime = System.currentTimeMillis();

            File outputFile = new File(DESTINATION_FILE);
            ImageIO.write(resoultImg, "PNG", outputFile);

            long duration = endTime - startTime;
            System.out.println("duration is: " + duration);

        } catch (IOException | ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }


}
