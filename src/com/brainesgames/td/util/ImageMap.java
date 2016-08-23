package com.brainesgames.td.util;

import javafx.beans.binding.ObjectBinding;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * Created by obrai on 2016-08-23.
 */
public class ImageMap {
    public interface Mapper{
        int map(int pixel);
    }

    public static class Fader implements Mapper{
        double fraction;

        public Fader(double fraction) {
            this.fraction = fraction;
        }

        public void setFraction(double fraction) {
            this.fraction = fraction;
        }

        @Override
        public int map(int pixel) {
            if(fraction < 0 || fraction > 1)throw new IllegalArgumentException("Fraction must be between 1 and 0");
            int alpha = pixel >>> 24;
            pixel &= 0xffffff;
            alpha = (int)(fraction * alpha);
            pixel += alpha << 24;
            return pixel;
        }
    }
    private static Fader fader;

    public static class Shader implements Mapper{
        double fraction;

        public Shader(double fraction) {
            this.fraction = fraction;
        }

        public void setFraction(double fraction) {
            this.fraction = fraction;
        }

        @Override
        public int map(int pixel) {
            int r = (pixel & 0xff0000) >>> 16;
            int g = (pixel & 0xff00) >>> 8;
            int b = pixel & 0xff;
            r = (int)(r*fraction);
            g = (int)(g*fraction);
            b = (int)(b*fraction);
            if(r > 255) r = 255;
            if(g > 255) g = 255;
            if(b > 255) b = 255;
            pixel &= 0xff000000;
            pixel += (r << 16) + (g << 8) + b;
            return pixel;
        }
    }
    private static Shader shader;

    static {
        shader = new Shader(0);
        fader = new Fader(0);
    }

    public static Image map(Image image, Mapper mapper){
        PixelReader reader = image.getPixelReader();
        WritableImage mapped = new WritableImage((int)(image.getWidth()),(int)(image.getHeight()));
        PixelWriter writer = mapped.getPixelWriter();
        map(reader,writer,(int)(image.getWidth()),(int)(image.getHeight()),mapper);
        return mapped;
    }

    public static void map(PixelReader reader, PixelWriter writer, int width, int height, Mapper mapper){
        for(int x = 0; x < width; x++) for(int y = 0; y < height; y++) writer.setArgb(x, y, mapper.map(reader.getArgb(x,y)));
    }

    public static Image fade(Image image, double fraction){
        fader.setFraction(fraction);
        return map(image, fader);
    }

    public static void fade(PixelReader reader, PixelWriter writer, int width, int height, double fraction){
        fader.setFraction(fraction);
        map(reader,writer,width,height,fader);
    }

    public static Image shade(Image image, double fraction){
        shader.setFraction(fraction);
        return map(image, shader);
    }

    public static void shade(PixelReader reader, PixelWriter writer, int width, int height, double fraction){
        shader.setFraction(fraction);
        map(reader,writer,width,height,shader);
    }

    public static void printPixel(int pixel){
        System.out.print("b: "+(pixel & 0xff)+" ");
        pixel >>>= 8;
        System.out.print("g: "+(pixel & 0xff)+" ");
        pixel >>>= 8;
        System.out.print("r: "+(pixel & 0xff)+" ");
        pixel >>>= 8;
        System.out.println("a: "+(pixel & 0xff));
    }
}
