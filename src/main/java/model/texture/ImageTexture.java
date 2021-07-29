package model.texture;

import model.Color;
import model.Point3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import static util.MathUtil.clamp;

public class ImageTexture extends Texture {
    private final int width;
    private final int height;
    private final int[][] img;

    public ImageTexture() {
        width = 0;
        height = 0;
        img = null;
    }

    public ImageTexture(String filename) {
        try {
            BufferedImage libImg = ImageIO.read(new File(filename));
            img = imageTo2D(libImg);
            width = libImg.getWidth();
            height = libImg.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Color value(double u, double v, Point3 p) {
        if (img == null)
            return new Color(0,1,1);

        // Clamp input texture coordinates to [0,1] x [1,0]
        u = clamp(u, 0.0, 1.0);
        v = 1.0 - clamp(v, 0.0, 1.0);  // Flip V to image coordinates

        int i = (int) (u * width);
        int j = (int) (v * height);

        // Clamp integer mapping, since actual coordinates should be less than 1.0
        if (i >= width)  i = width-1;
        if (j >= height) j = height-1;

        double color_scale = 1.0 / 255.0;
        Color pixel = getRGB(i, j);

        return new Color(color_scale*pixel.getX(), color_scale*pixel.getY(), color_scale*pixel.getZ());
    }

    private Color getRGB(int x, int y) {
        int rgb = img[y][x];
        int r = ((rgb >> 16) & 0xff);
        int g = ((rgb >> 8) & 0xff);
        int b = (rgb & 0xff);
        return new Color(r, g, b);
    }

    private static int[][] imageTo2D(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }


}
