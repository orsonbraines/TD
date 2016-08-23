package com.brainesgames.td.app;

import com.brainesgames.td.game.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {
    Map map;
    GraphicsContext g;

    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage stage) {
        Canvas canvas = new Canvas(640,480);
        Group root = new Group(canvas);
        Scene scene = new Scene(root, 640, 480);

        System.out.println(System.getProperty("user.dir"));

        map = new Map();
        g = canvas.getGraphicsContext2D();

        map.draw(g);

        stage.setTitle("Tower Defense");
        stage.setScene(scene);
        stage.show();

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                animate();
            }
        }, 25, 25);
    }

    void animate(){
        map.move();
        Platform.runLater(()->map.draw(g));
    }
}
