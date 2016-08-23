package com.brainesgames.td.game;

import com.brainesgames.linalg.V2i;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.brainesgames.td.util.ImageMap.fade;

public class Unit {
    Image image;
    int hp;
    V2i size;
    int position;
    int value;

    public Unit(int hp, V2i size, int position) {
        try {
            image = fade(new Image(new FileInputStream("imgs/unit1.png")),0.7);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.hp = hp;
        this.size = size;
        this.position = position;
        value = 10;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void incPosition() { position++; }

    public void draw(GraphicsContext g, int x, int y){
        g.drawImage(image, x, y, size.getX(), size.getY());
    }
}
