package com.brainesgames.td.game;

import com.brainesgames.linalg.V2i;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Math.PI;
import static java.lang.Math.cos;

public class Map {
    String imageFile;
    Image image;
    V2i size;
    V2i[] path;
    ArrayList<Tower> towers;
    ArrayList<Unit> units;

    public Map(String imageFile, V2i[] path){
        this.imageFile = imageFile;
        try {
            image = new Image(new FileInputStream(imageFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        size = new V2i(640,480);
        this.path = path;

        towers = new ArrayList<>();
        units = new ArrayList<>();
        units.add(new Unit(100,new V2i(64,64)));
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
            System.out.println(pathStr);
            int n = sc.nextInt();
            V2i[] parsed = new V2i[n];
            for(int i = 0; i < n; i++){
                System.out.println(i);
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
        for(Unit unit : units){
            if(unit.position == path.length - 1)unit.setPosition(0);
            else unit.incPosition();
        }
    }

    public Image getImage() {
        return image;
    }

    public void draw(GraphicsContext g){
        g.drawImage(image, 0, 0);
        for(Tower tower : towers) tower.draw(g);
        for(Unit unit : units){
            V2i loc = path[unit.position];
            //int width = unit.size.getX();
            int height = unit.size.getY();
            unit.draw(g, loc.getX(), loc.getY() - height / 2);
        }
    }

    public static void printPath(PrintStream out, V2i[] path){
        out.print(path.length + " ");
        for(int i = 0; i < path.length; i++){
            out.print(path[i].getX() + " " + path[i].getY() + " ");
        }
    }
}
