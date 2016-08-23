package com.brainesgames.td.game;

import com.brainesgames.linalg.V2i;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static com.brainesgames.td.util.ImageMap.shade;

public class Map {
    //image stuff
    String imageFile;
    WritableImage image;
    PixelReader reader;
    PixelWriter writer;
    //sub objects
    V2i[] path;
    ArrayList<Tower> towers;
    ArrayList<Unit> units;
    //state stuff
    int hp;
    V2i size;
    //animation stuff
    int loseColourIdx;
    static Color[] loseColours;
    int shadeCount;

    static{
        loseColours = new Color[51];
        for(int i = 0; i < 26; i++)loseColours[i] = Color.rgb(i*10, 0, 0);
        for(int i = 24; i >= 0; i--)loseColours[50 - i] = Color.rgb(i*10, 0, 0);
    }

    public Map(String imageFile, V2i[] path){
        this.imageFile = imageFile;
        try {
            Image img = new Image(new FileInputStream(imageFile));
            reader = img.getPixelReader();
            image = new WritableImage(reader, (int)img.getWidth(), (int)img.getHeight());
            reader = image.getPixelReader();
            writer = image.getPixelWriter();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        size = new V2i(640,480);
        this.path = path;
        hp = 100;
        towers = new ArrayList<>();
        units = new ArrayList<>();
        for(int i=0; i < 11; i++)units.add(new Unit(100, new V2i(64,64), -50 * i));

        loseColourIdx = 0;
        shadeCount = 0;
    }

    public static Map load(String file){
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null)lines.add(line);
            br.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        System.out.println(lines);
        String loadedImageFile = null;
        V2i[] loadedPath = null;
        for(String l : lines){
            String[] split = l.split(":");
            if(split.length == 2){
                String key = split[0];
                String value = split[1];
                switch(key){
                    case "image":
                        loadedImageFile = value;
                        break;
                    case "path":
                        loadedPath = parsePath(value);
                        break;
                }
            }
        }
        return loadedImageFile != null && loadedPath != null ? new Map(loadedImageFile, loadedPath) : null;
    }

    static V2i[] parsePath(String pathStr){
        try{
            Scanner sc = new Scanner(pathStr);
            int n = sc.nextInt();
            V2i[] parsed = new V2i[n];
            for(int i = 0; i < n; i++){
                int x = sc.nextInt();
                int y = sc.nextInt();
                parsed[i] = new V2i(x,y);
            }
            return parsed;
        } catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public void write(String file) {
        try {
            PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)));
            out.println("image:"+imageFile);
            out.print("path:");
            printPath(out, path);
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
    }

    public void move(){
        for(int i =0; i<units.size(); i++){
            Unit unit = units.get(i);
            if(unit.position == path.length - 1){
                hp -= unit.value;
                units.remove(i);
                i--;
            }
            else unit.incPosition();
        }
    }

    public Image getImage() {
        return image;
    }

    public void draw(GraphicsContext g){
        g.drawImage(image, 0, 0);
        if(hp > 0) {
            for (Tower tower : towers) tower.draw(g);
            for (Unit unit : units) {
                if (unit.position >= 0) {
                    V2i loc = path[unit.position];
                    int width = unit.size.getX();
                    int height = unit.size.getY();
                    unit.draw(g, loc.getX() - width / 2, loc.getY() - height / 2);
                }
            }
        }
        else{
            g.setFill(loseColours[loseColourIdx++]);
            g.setFont(Font.font("monospace", FontWeight.BLACK, 120));
            g.fillText("YOU LOSE",20,250);
            if(loseColourIdx == loseColours.length) loseColourIdx = 0;
            if(shadeCount++ < 256)shade(reader, writer, (int)image.getWidth(),(int)image.getHeight(), 0.99);
        }
    }

    public static void printPath(PrintStream out, V2i[] path){
        out.print(path.length + " ");
        for(int i = 0; i < path.length; i++){
            out.print(path[i].getX() + " " + path[i].getY() + " ");
        }
    }

    public int getHp() {
        return hp;
    }
}
