package com.brainesgames.td.game;

import com.brainesgames.linalg.V2i;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Unit {
    Image image;
    int hp;
    V2i size;
    int position;

    public Unit(int hp, V2i size) {
        try {
            image = new Image(new FileInputStream("imgs/unit1.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.hp = hp;
        this.size = size;
        position = 0;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void incPosition() { position++; }

    public void draw(GraphicsContext g, int x, int y){
        g.drawImage(image, x, y, size.getX(), size.getY());
    }
}
