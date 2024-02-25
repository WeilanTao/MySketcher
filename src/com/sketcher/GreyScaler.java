package com.sketcher;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @File
 * @Author Emily Weilan Tao
 * @Date July 14, 2021
 * @Description Add multithreading
 * @Since version-1.0
 * @Copyright Copyright (c) 2020
 */
public class GreyScaler {
    private static final double RGB_LIMIT = 255.00;
    private static int imageWidth;
    private static int imageHeight;
    private BufferedImage resultImage;
    private static final int THREAD_NUMBER = 2;
    private static int height;
    private static ExecutorService executor;


    public GreyScaler(BufferedImage originalImage) throws InterruptedException {
        imageWidth = originalImage.getWidth();
        imageHeight = originalImage.getHeight();
        height = imageHeight / THREAD_NUMBER;
        resultImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
        executor = Executors.newCachedThreadPool();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            final int threadMuliplier = i;

            threads.add(new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * threadMuliplier;

                grayScaleImg(originalImage, resultImage, leftCorner, topCorner, imageWidth, height);
            }));

        }

        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }


    }


    /**
     * Rendering the pic pixel by pixel
     *
     * @param originalImage
     * @param resultImage
     */
    private static void grayScaleImg(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner, int imageWidth, int height) {
        for (int i = leftCorner; i < leftCorner + imageWidth && i < imageWidth; i++) {
            for (int j = leftCorner; j < topCorner + height && j < imageHeight; j++) {
                int oRGB = originalImage.getRGB(i, j);
                int igs = inverseGammaExpansion(grayScalePixel(oRGB));
                resultImage.setRGB(i, j, createRGB(igs).getRGB());
            }
        }

    }

    /**
     * Extract red, green blue
     *
     * @param rgbVal
     * @return new RGB
     */
    private static double grayScalePixel(int rgbVal) {
        int red = (rgbVal & 0x00FF0000) >> 16;
        int blue = rgbVal & 0x000000FF;
        int green = (rgbVal & 0x0000FF00) >> 8;

        return grayScaling(red, green, blue);
    }

    /**
     * Compress the RGB color based on gamma expansion
     *
     * @param r
     * @param g
     * @param b
     * @return linear greyscale representation of its luminance
     */
    private static double grayScaling(int r, int g, int b) {
        double scaledr = r / RGB_LIMIT;
        double scaledg = g / RGB_LIMIT;
        double scaledb = b / RGB_LIMIT;

        double linearr = gammaCompress(scaledr);
        double linearg = gammaCompress(scaledg);
        double linearb = gammaCompress(scaledb);

        return (0.2126 * linearr + 0.7152 * linearg + 0.07722 * linearb);
    }

    /**
     * @param c
     * @return gammaCompress red; green; blue parameter
     */
    private static double gammaCompress(double c) {
        return c < 0.04045 ? c / 12.92 : Math.pow(((c + 0.055) / 1.055), 2.4);
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

        return new Color(red, blue, green);
    }

    public BufferedImage getResultImage() {
        return resultImage;
    }
}
