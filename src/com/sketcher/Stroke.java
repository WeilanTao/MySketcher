package com.sketcher;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * @File
 * @Author Emily Weilan Tao
 * @Date July 14, 2021
 * @Description produce outline/shape of items in an image
 * @Since version-1.1
 * @Copyright Copyright (c) 2021
 */
public class Stroke {
    private static float[] kernel;
    private static float[] kernel1;
    private static float[] kernel2;
    private static float[] kernel3;
    private static float[] blurKernel;
    private static float[] getSharpKernel;
    private static int width;
    private static int height;
    private static BufferedImage result;
    private static BufferedImage inputImage;
    private static BufferedImage strokeBlended;

    public static BufferedImage getResult() {
        return result;
    }

    public Stroke(BufferedImage input) {
        width = input.getWidth();
        height = input.getHeight();
        KernelTable kernelgenerater = new KernelTable();
        kernel = kernelgenerater.getKERNEL();
        kernel1 = kernelgenerater.getKernel1();
        kernel2 = kernelgenerater.getKernel2();
        kernel3 = kernelgenerater.getKernel3();
        blurKernel = kernelgenerater.getBlurKernel();
        getSharpKernel = kernelgenerater.getSharpKernel();
        inputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        strokeBlended = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = inputImage.getGraphics();
        g.drawImage(input, 0, 0, null);
        g.dispose();

        generateStroke();
    }

    private static void generateStroke() {
        BufferedImageOp blur_0 = new ConvolveOp(new Kernel(3, 3, kernel));
        BufferedImage outputImg_0 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        BufferedImageOp blur_1 = new ConvolveOp(new Kernel(3, 3, kernel1));
        BufferedImage outputImg_1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        BufferedImageOp blur_2 = new ConvolveOp(new Kernel(3, 3, kernel2));
        BufferedImage outputImg_2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        BufferedImageOp blur_3 = new ConvolveOp(new Kernel(3, 3, kernel3));
        BufferedImage outputImg_3 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        blur_0.filter(inputImage, outputImg_0);
        blur_1.filter(inputImage, outputImg_1);
        blur_2.filter(inputImage, outputImg_2);
        blur_3.filter(inputImage, outputImg_3);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = outputImg_0.getRGB(i, j);
                Color c = fixRGB(rgb);
                outputImg_0.setRGB(i, j, c.getRGB());
                int rgb1 = outputImg_1.getRGB(i, j);
                Color c1 = fixRGB(rgb1);
                outputImg_1.setRGB(i, j, c1.getRGB());
                int rgb2 = outputImg_2.getRGB(i, j);
                Color c2 = fixRGB(rgb2);
                outputImg_2.setRGB(i, j, c2.getRGB());
                int rgb3 = outputImg_3.getRGB(i, j);
                Color c3 = fixRGB(rgb3);
                outputImg_3.setRGB(i, j, c3.getRGB());
            }
        }

        //combine the four directions
        int[][] rgbMap = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rgbMap[i][j] = getMin(outputImg_0.getRGB(i, j), outputImg_1.getRGB(i, j), outputImg_2.getRGB(i, j), outputImg_3.getRGB(i, j));
            }
        }

        BufferedImage outputNosharp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);


        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                outputNosharp.setRGB(i, j, rgbMap[i][j]);
            }
        }

        //sharp the line
        BufferedImage outputSharped = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        BufferedImageOp sharpline = new ConvolveOp(new Kernel(3, 3, getSharpKernel));
        sharpline.filter(outputNosharp, outputSharped);

        //gussian blur
        BufferedImageOp blur = new ConvolveOp(new Kernel(3, 3, blurKernel));
        blur.filter(outputSharped, result);


    }

    private static Color fixRGB(int rgbint) {
        int red;

        red = (rgbint & 0x00FF0000) >> 16;
        if (red > 255) {
            red = 255;
        } else if (red < 20) {
            red = 0;
        }
        red = 255 - red;

        Color c = new Color(red, red, red);
        return c;
    }

    private static int getMin(int rgb0, int rgb1, int rgb2, int rgb3) {
        int min = rgb0;
        if (rgb1 < min) {
            min = rgb1;
        }
        if (rgb2 < min) {
            min = rgb2;
        }
        if (rgb3 < min) {
            min = rgb3;
        }
        return min;
    }

    public static BufferedImage blendStroke(BufferedImage textured) {
        //get a copy of the stroke img as the cover img
        BufferedImage cover = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = cover.getGraphics();
        g.drawImage(result, 0, 0, null);
        g.dispose();

        int alpha = 255;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = cover.getRGB(i, j);
                int r = (rgb & 0x00FF0000) >> 16;
                if (r > 150) {
                    alpha = 0;
                } else {
                    alpha = 255;
                }
                rgb = (alpha << 24 | (rgb & 0x00ffffff));
                cover.setRGB(i, j, rgb);

            }
        }


        //blend the two pictures
        Graphics g1 = strokeBlended.getGraphics();
        g1.drawImage(textured, 0, 0, null);
        g1.drawImage(cover, 0, 0, null);

        g1.dispose();

//        BufferedImage blured = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//        BufferedImageOp blur = new ConvolveOp(new Kernel(3, 3, blurKernel));
//        blur.filter(strokeBlended, blured);

        return strokeBlended;
    }
}
