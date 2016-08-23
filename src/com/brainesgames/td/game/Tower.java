package com.brainesgames.td.game;

import com.brainesgames.linalg.V2i;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tower {
    Image image;
    int range;
    V2i loc,size;

    public void draw(GraphicsContext g){
        g.drawImage(image, loc.getX() - size.getX() / 2, loc.getY() - size.getY() / 2, size.getX(), size.getY());
    }
}
