      /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package katkilla;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import static katkilla.Config.*;

/**
 *
 * @author Kuba
 */
public class KatKilla extends Application {

    private static Pane root = new Pane();
    public static Scene scene;
    private Frames frames;

   
    @Override
    public void start(Stage stage) {
        frames = new Frames();
        scene = new Scene(root, APP_WIDTH, APP_HEIGHT);
        root.getStylesheets().add(getClass().getResource("/styles/stylesheet.css").toExternalForm());
        stage.setTitle("Kat killa!");  
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        root.getChildren().add(frames.introScreen());        
    }
    
    public static Node getRoot() {
        return root;
    }
    
    public static void clearAndDisplay(Node... args) {
        root.getChildren().clear();
        root.getChildren().addAll(args);
    }
    
    public static void remove(Node node) {
        root.getChildren().remove(node);
    }
    
    public static void display(Node node) {
        root.getChildren().add(node);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
