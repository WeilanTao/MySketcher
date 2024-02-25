package com.sketcher;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @File
 * @Author Emily Weilan Tao
 * @Date
 * @Description This is the class that generates the sketch effect for a picture.
 * @Since version-1.0
 * @Copyright Copyright (c) 2020
 */
public class Sketcher {
    public static final String SOURCE_FILE = "./resource/xjj.png";
    public static final String DESTINATION_FILE = "./resource/xjj_out.PNG";
    private static Logger logger = Logger.getLogger(Sketcher.class.getName());

    public static void main(String[] args) {
        BufferedImage originalImage = null;

        try {
            /**
             * Read in the original image
             */
            originalImage = ImageIO.read(new File(SOURCE_FILE));

            long startTime = System.currentTimeMillis();

            /**
             * Grey version of the original image
             */
            GreyScaler greyScaler = new GreyScaler(originalImage);
            BufferedImage greyScalerResultImage = greyScaler.getResultImage();

            /**
             * Multi-thread the process of tonning and generating the stroke/shape of items in the picture
             *
             */
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
                stroke.getResult();
                return stroke;
            };

            futureblendedTxtre = executorService.submit(colorer);
            futurestroke = executorService.submit(stroker);

            executorService.shutdown();

            /**
             * Blend layers generated above
             */
            BufferedImage resoultImg = (futurestroke.get()).blendStroke(futureblendedTxtre.get());

            long endTime = System.currentTimeMillis();

            /**
             * Output image
             */
            File outputFile = new File(DESTINATION_FILE);
            ImageIO.write(resoultImg, "PNG", outputFile);

            long duration = endTime - startTime;
            logger.log(Level.INFO, "Duration is: {0}", duration);

        } catch (IOException | ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
