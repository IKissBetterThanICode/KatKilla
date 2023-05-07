 package katkilla;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;
import static katkilla.Config.*;
import static katkilla.Resources.*;

/**
 *
 * @author Kuba
 */
public class Weapon extends Constructs {

    private int 
            delay,
            velocity;
    
    private String soundFile;
    private boolean 
            isNeutralized, 
            isFired, 
            isDead;
    private Weapon weapon;
    private Object source;
    private LevelLoader loader = new LevelLoader();
    
    private boolean isMarkedForRemoval;
    
    private BooleanProperty isMarkedForRemovalProperty = new SimpleBooleanProperty(isMarkedForRemoval);    
    private static StringProperty currentWeaponName = new SimpleStringProperty();
    private BooleanProperty isDeadProperty = new SimpleBooleanProperty(isDead);    

    public Weapon() {

    }

    private Weapon(Image spriteSheet, Rectangle2D spriteView, String name, String soundFile, 
            int rotationAngle, int hp, int delay, int velocity, boolean isNeutralized, boolean isMarkedForRemoval) {
        super(spriteSheet, spriteView, name, rotationAngle, hp);
        this.soundFile = soundFile;
        this.delay = delay;
        this.velocity = velocity;
        this.isNeutralized = isNeutralized;
        
        setIsFired(false);
        setIsMarkedForRemoval(false);
    }

    /*
    *   PLAYER WEAPONS
     */
    public ArrayList<Weapon> getLightLaser(Constructs constructs, Pane layer) {
        ArrayList<Weapon> projectiles = new ArrayList();
        for (int i = 0; i < 2; ++i) {
            Weapon w = new Weapon(SPRITE_SHEET, L_LASER_IMAGE, LIGHTLASER, L_LASER_SOUND,
                    0, 2, 125, 200, false, false);
            projectiles.add(w);
            layer.getChildren().add(w);
            Timeline timeline = new Timeline();
            KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                    new KeyValue(w.translateYProperty(), constructs.getTranslateY()));
            KeyFrame kf2 = new KeyFrame(Duration.millis(w.getVelocity()),
                    new KeyValue(w.translateYProperty(), -FIELD_HEIGHT));
            timeline.getKeyFrames().addAll(kf1, kf2);
            loader.getTimelines().add(timeline);
            timeline.play();
            timeline.setOnFinished((event) -> {
                w.setIsMarkedForRemoval(true);
                loader.getTimelines().remove(timeline);
                layer.getChildren().remove(w);
            });
        }
        return projectiles;
    }

    public ArrayList<Weapon> getDFMissile(Constructs constructs, Pane layer) {
        ArrayList<Weapon> missiles = new ArrayList();
        for (int i = 0; i < 2; ++i) {
            Weapon w = new Weapon(SPRITE_SHEET, DFMISSILE_IMAGE, DFMISSILE, DFMISSILE_SOUND, 
                    0, 25, 1000, 2000, false, false);
            missiles.add(w);
            layer.getChildren().add(w);
            
            Timeline timeline = new Timeline();
            KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                    new KeyValue(w.translateXProperty(), constructs.getTranslateX()),
                    new KeyValue(w.translateYProperty(), constructs.getTranslateY()));
            KeyFrame kf2 = new KeyFrame(Duration.millis(500),
                    new KeyValue(w.translateXProperty(), constructs.getTranslateX() - 20.0 + (40.0 * i)),
                    new KeyValue(w.translateYProperty(), 10.0));
            KeyFrame kf3 = new KeyFrame(Duration.millis(w.getVelocity()),
                    new KeyValue(w.translateYProperty(), -FIELD_HEIGHT));
            timeline.getKeyFrames().addAll(kf1, kf2, kf3);
            loader.getTimelines().add(timeline);
            timeline.play();
            timeline.setOnFinished((event) -> {
                w.setIsMarkedForRemoval(true);
                loader.getTimelines().remove(timeline);
                layer.getChildren().remove(w);
            });            
        }
        return missiles;
    }

    public Weapon getSonicCannon(Constructs constructs, Pane layer) {
        Weapon w = new Weapon(SPRITE_SHEET, SONIC_IMAGE, SONICCANNON, SONICCANNON_SOUND, 
                0, 1, 500, 500, false, false);
        layer.getChildren().add(w);
        Timeline timeline = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(w.translateYProperty(), constructs.getTranslateY()));
        KeyFrame kf2 = new KeyFrame(Duration.millis(w.getVelocity()),
                new KeyValue(w.translateYProperty(), -FIELD_HEIGHT));
        timeline.getKeyFrames().addAll(kf1, kf2);
        loader.getTimelines().add(timeline);
        timeline.play();
        timeline.setOnFinished((event) -> {
            w.setIsMarkedForRemoval(true);
            loader.getTimelines().remove(timeline);
            layer.getChildren().remove(w);
        });
        return w;
    }

    /*
    GETTERS AND SETTERS
     */
    public void setSource(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }

    public void setObservableWeaponName(String s) {
        this.currentWeaponName.set(s);
    }

    public String getObservableWeaponName() {
        return currentWeaponName.get();
    }

    public StringProperty getObservableWeaponNameProperty() {
        return currentWeaponName;
    }

    public void setSoundFile(String soundFile) {
        this.soundFile = soundFile;
    }

    public String getSoundFile() {
        return soundFile;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }
        
    
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setIsDead(boolean isDead) {
        isDeadProperty.set(isDead);
    }

    public boolean getIsDead() {
        return isDeadProperty.get();
    }

    public BooleanProperty getIsDeadProperty() {
        return isDeadProperty;
    }  

    public void setIsNeutralized(boolean isNeutralized) {
        this.isNeutralized = isNeutralized;
    }

    public boolean getIsNeutralized() {
        return isNeutralized;
    }

    public void setIsFired(boolean isFired) {
        this.isFired = isFired;
    }

    public boolean getIsFired() {
        return isFired;
    }    
    
    public void setIsMarkedForRemoval(boolean isMarkedForRemoval) {
        isMarkedForRemovalProperty.set(isMarkedForRemoval);
    }

    public boolean getIsMarkedForRemoval() {
        return isMarkedForRemovalProperty.get();
    }
    public BooleanProperty getIsMarkedForRemovalProperty() {
        return isMarkedForRemovalProperty;
    }      
   
}
