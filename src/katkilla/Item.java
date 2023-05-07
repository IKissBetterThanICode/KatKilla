/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package katkilla;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import static katkilla.Animations.setRotation;
import static katkilla.Resources.*;

/**
 *
 * @author Kuba
 */
public class Item extends Constructs {
    
    private boolean isCollected = false;
    
    private int boost;
    
    private boolean isMarkedForRemoval;
    
    private BooleanProperty isMarkedForRemovalProperty = new SimpleBooleanProperty(isMarkedForRemoval);     

    private LevelLoader loader = new LevelLoader();
    private Entities player = loader.getPlayer();
    
    public Item() { }                                                           //empty default constructor
    
    private Item(Image spriteSheet, Rectangle2D spriteView, String name, int rotationAngle, 
            int boost, boolean isMarkedForRemoval) {
        super(spriteSheet, spriteView, name, rotationAngle);
        this.boost = boost;
    }
    
    public Item hpIncrease(Pane layer) {
        Item i = new Item(SPRITE_SHEET, ITEM_HP, "Heart", 0, 20, false);
        loader.getItems().add(i);
        layer.getChildren().add(i);
        setRotation(i, Duration.millis(1000));
        return i;
    }
    
    /*
    GETTERS AND SETTERS
     */
    public int getBoost() {
        return boost;
    }

    public void setBoost(int boost) {
        this.boost = boost;
    }

    public boolean isIsCollected() {
        return isCollected;
    }

    public void setIsCollected(boolean isCollected) {
        this.isCollected = isCollected;
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
