package com.brainesgames.td.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * Created by obrai on 2016-08-23.
 */
public class Fade {
    public static Image fade(Image image, double fraction){
        if(fraction < 0 || fraction > 1)throw new IllegalArgumentException("Fraction must be between 1 and 0");
        PixelReader reader = image.getPixelReader();
        WritableImage faded = new WritableImage((int)(image.getWidth()),(int)(image.getHeight()));
        PixelWriter writer = faded.getPixelWriter();
        fade(reader,writer,(int)(image.getWidth()),(int)(image.getHeight()),fraction);
        return faded;
    }

    public static void fade(PixelReader reader, PixelWriter writer, int width, int height, double fraction){
        if(fraction < 0 || fraction > 1)throw new IllegalArgumentException("Fraction must be between 1 and 0");
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                //0xAARRGGBB
                int pixel = reader.getArgb(x,y);
                int alpha = pixel >>> 24;
                pixel &= 0xffffff;
                alpha = (int)(fraction * alpha);
                pixel += alpha << 24;
                writer.setArgb(x,y,pixel);
            }
        }
    }

    public static Image shade(Image image, double fraction){
        PixelReader reader = image.getPixelReader();
        WritableImage shaded = new WritableImage((int)(image.getWidth()),(int)(image.getHeight()));
        PixelWriter writer = shaded.getPixelWriter();
        shade(reader,writer,(int)(image.getWidth()),(int)(image.getHeight()),fraction);
        return shaded;
    }

    public static void shade(PixelReader reader, PixelWriter writer, int width, int height, double fraction){
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                //0xAARRGGBB
                int pixel = reader.getArgb(x,y);
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
                writer.setArgb(x,y,pixel);
            }
        }
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
