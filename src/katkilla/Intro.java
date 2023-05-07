 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package katkilla;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import static katkilla.Config.*;
import static katkilla.Resources.*;

/**
 *
 * @author Kuba
 */
public class Intro extends Parent {

    private KatKilla katKilla;
    private LevelLoader loader = new LevelLoader();
    private Resources resources = new Resources();

    
    
    public Group introScreen() {
        Group group = new Group();
        Pane bgPane = new Pane();
        bgPane.setMinSize(APP_WIDTH + BACKGROUND_PADDING, APP_HEIGHT + BACKGROUND_PADDING);
        bgPane.setBackground(new Background(new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY, Insets.EMPTY)));

        Text title = new Text("KAT KILLA");
        title.setFont(new Font(80));
        title.setFill(Color.RED);
        int titleW = (int) title.getBoundsInParent().getWidth();
        int titleH = (int) title.getBoundsInParent().getHeight();
        title.setX(APP_WIDTH / 2 - (titleW / 2));
        title.setY(APP_HEIGHT / 3);

        ImageView iv = new ImageView(resources.SPRITE_SHEET);
        iv.setViewport(INTRO_CAT);
        iv.setX(APP_WIDTH / 2 - iv.getViewport().getWidth() / 2);
        iv.setY(APP_WIDTH / 2);

        Timeline timeline = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.millis(1000),
                new KeyValue(iv.scaleXProperty(), 0));
        KeyFrame kf2 = new KeyFrame(Duration.millis(5000),
                new KeyValue(iv.scaleXProperty(), 1));
        timeline.getKeyFrames().addAll(kf1, kf2);
        timeline.play();
        bgPane.getChildren().addAll(title, iv);
        group.getChildren().addAll(bgPane);
        keyControl();
        
        return group;
    }

    public Group levelSummary() {

        Group group = new Group();
        ImageView iv = new ImageView(resources.LEVEL_A1);
        iv.setFocusTraversable(true);
        iv.setViewport(INTRO_CAT);
        iv.setX(APP_WIDTH / 2 - iv.getViewport().getWidth() / 2);
        iv.setY(APP_WIDTH / 2);
        Text text = new Text("BLABLABLA");
        Pane bgPane = new Pane();
        bgPane.setMinSize(APP_WIDTH + BACKGROUND_PADDING, APP_HEIGHT + BACKGROUND_PADDING);
        bgPane.setBackground(new Background(new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY, Insets.EMPTY)));
        group.getChildren().addAll(bgPane, iv, text);
        group.setTranslateX(APP_WIDTH);
        General.playSound(resources.PANE_SLIDE);
        Timeline timeline = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(group.translateXProperty(), APP_WIDTH));
        KeyFrame kf2 = new KeyFrame(Duration.millis(2000),
                new KeyValue(group.translateXProperty(), 0));
        timeline.getKeyFrames().addAll(kf1, kf2);
        timeline.play();
        keyControl();
        
        return group;
    }

    private void keyControl() {
        katKilla = new KatKilla();
        KatKilla.scene.setOnKeyPressed((KeyEvent event) -> {
            KeyCode key = event.getCode();
            switch (key) {
                case SPACE:
                    System.out.println(loader.getLevelSelector());
                    loader.setLevelSelector(loader.getLevelSelector() + 1);
                    break;
            }
        });
    }
}
