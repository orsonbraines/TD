package com.brainesgames.td.game;

import com.brainesgames.linalg.V2i;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tower {
    Image image;
    int hp;
    V2i loc,size;

    public void draw(GraphicsContext g){
        g.drawImage(image, loc.getX(), loc.getY(), size.getX(), size.getY());
    }
}
