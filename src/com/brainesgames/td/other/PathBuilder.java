package com.brainesgames.td.other;

import com.brainesgames.geom.Circle;
import com.brainesgames.geom.Line;
import com.brainesgames.linalg.V2f;
import com.brainesgames.linalg.V2i;
import com.brainesgames.td.game.Map;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static com.brainesgames.linalg.V2f.sub;

public class PathBuilder extends Application{
    ArrayList<Circle> points;
    GraphicsContext g;
    Image image;
    int activePoint;

    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage stage) {
        try {
            image = new Image(new FileInputStream("imgs/map1.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        double width = image.getWidth();
        double height = image.getHeight();
        Canvas canvas = new Canvas(width, height);
        g = canvas.getGraphicsContext2D();

        points = new ArrayList<>();
        points.add(new Circle(0,(float) height/2, 8));
        points.add(new Circle((float) width,(float) height/2, 8));
        activePoint = 0;

        Button button1 = new Button("Add point");
        button1.setOnAction((event)->{
            if(activePoint != points.size() - 1) {
                Circle currentPoint = points.get(activePoint);
                points.add(++activePoint,new Circle(currentPoint.getX() + 20, currentPoint.getY(), currentPoint.getR()));
                draw(g);
            }
        });

        Button button2 = new Button("Create Path");
        button2.setOnAction(event -> {
            V2i[] path = createPath();
            try {
                PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream("data/maps.txt",true)));
                ps.print("\r\npath:");
                Map.printPath(ps, path);
                ps.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        canvas.setOnMousePressed(event -> {
            for(int i = 0; i < points.size(); i++){
                Circle point = points.get(i);
                if(point.pointInside(new V2f((float)event.getX(),(float)event.getY()))){
                    activePoint = i;
                    draw(g);
                    break;
                }
            }
        });

        canvas.setOnMouseDragged(event ->{
            if(activePoint != 0 && activePoint != points.size() - 1) points.get(activePoint).setX((float)event.getX());
            points.get(activePoint).setY((float)event.getY());
            draw(g);
        });

        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setBottom(new HBox(button1,button2));
        Scene scene = new Scene(root);
        stage.setTitle("Tower Defense");
        stage.setScene(scene);
        draw(g);
        stage.show();
    }

    void draw(GraphicsContext g){
        g.drawImage(image, 0, 0);
        g.setStroke(Color.RED);
        g.setLineWidth(5);
        for(int i = 0; i<points.size(); i++){
            if(activePoint == i)g.setFill(Color.YELLOW);
            else g.setFill(Color.BLACK);
            Circle point = points.get(i);
            g.fillOval(point.getX() - point.getR(), point.getY() - point.getR(), point.getR() * 2, point.getR() * 2);
        }
        //connect the dot
        for(int i = 0; i < points.size() - 1; i++){
            Circle p1 = points.get(i);
            Circle p2 = points.get(i + 1);
            g.strokeLine(p1.getX(),p1.getY(),p2.getX(),p2.getY());
        }
    }

    V2i[] createPath(){
        ArrayList<V2f> pathList = new ArrayList<>();
        for(int i = 0; i < points.size() - 1; i++){
            Circle p1 = points.get(i);
            Circle p2 = points.get(i + 1);
            V2f dif = sub(p2.getC(),p1.getC());
            int numPieces = (int)(dif.mag() / 4);
            for(int j = 0; j < numPieces;j++){
                pathList.add(V2f.add(p1.getC(),V2f.scale((float)j/numPieces,dif)));
            }
        }
        pathList.add(points.get(points.size() - 1).getC());

        V2i[] path = new V2i[pathList.size()];
        for(int i = 0; i < path.length; i++){
            V2f p = pathList.get(i);
            path[i] = new V2i(Math.round(p.getX()),Math.round(p.getY()));
        }
        return path;
    }
}
