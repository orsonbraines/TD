package com.brainesgames.td.game;

import com.brainesgames.linalg.V2i;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static java.lang.Math.PI;
import static java.lang.Math.cos;

public class Map {
    Image image;
    V2i size;
    V2i[] path;
    ArrayList<Tower> towers;
    ArrayList<Unit> units;

    public Map(){
        try {
            image = new Image(new FileInputStream("imgs/map1.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        size = new V2i(640,480);
        path = new V2i[640];
        for(int i = 0; i < 640; i++){
            path[i] = new V2i(i,(int)(-180*cos(2 * PI * i / 320)) + 240);
        }
        towers = new ArrayList<>();
        units = new ArrayList<>();
        units.add(new Unit(100,new V2i(64,64)));
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
            unit.draw(g, loc.getX(), loc.getY());
        }
    }
}
