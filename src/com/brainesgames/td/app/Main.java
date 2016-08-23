package com.brainesgames.td.app;

import com.brainesgames.linalg.V2i;
import com.brainesgames.td.game.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import static java.lang.Math.PI;
import static java.lang.Math.cos;

public class Main extends Application {
    Map map;
    GraphicsContext g;

    class AnimationLoop implements Runnable{
        @Override
        public void run() {
            try {
                while(true) {
                    if (map.getHp() > 0) {
                        map.move();
                        Platform.runLater(() -> map.draw(g));
                        Thread.sleep(10);
                    } else {
                        Platform.runLater(() -> map.draw(g));
                        Thread.sleep(25);
                    }
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage stage) {
        Canvas canvas = new Canvas(640,480);
        Group root = new Group(canvas);
        Scene scene = new Scene(root, 640, 480);

        System.out.println(System.getProperty("user.dir"));

        V2i[] path = new V2i[640];
        for(int i = 0; i < 640; i++){
            path[i] = new V2i(i,(int)(-180*cos(2 * PI * i / 320)) + 240);
        }

        map = Map.load("maps/map1.txt");
        g = canvas.getGraphicsContext2D();

        map.draw(g);

        stage.setTitle("Tower Defense");
        stage.setScene(scene);
        stage.show();

        Thread animationThread = new Thread(new AnimationLoop(),"Animation Thread");
        animationThread.setDaemon(true);
        animationThread.start();
    }
}
