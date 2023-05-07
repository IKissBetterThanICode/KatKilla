/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package katkilla;

import javafx.beans.property.BooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Kuba
 */
public class Constructs extends ImageView {

    //default empty constructor
    public Constructs() {
    }

    private int 
            spriteW,                                                            //sprite's width
            spriteH,                                                            //sprite's height
            velocity,
            rotationAngle,                                                      //sprite rotation angle
            hp;                                                                 //sprite's hp
    
    private Image spriteSheet;      //sprite sheet used

    private Rectangle2D spriteView; //sprite viewport

    private String name;                                                        //entity's name
    

//Image spriteSheet, Rectangle2D spriteView, String name, int rotationAngle,
//            int hp, int maxHp, int velocity, boolean isDead    
    
    //base constructor for enemies
    public Constructs(Image spriteSheet, Rectangle2D spriteView, String name, int rotationAngle, int hp) {

        super(spriteSheet);
        this.spriteView = spriteView;
        this.rotationAngle = rotationAngle;
        this.name = name;
        this.hp = hp;

        setRotate(rotationAngle);
        setViewport(spriteView);
    }
    
    //base constructor for items and neutral entities  
    public Constructs(Image spriteSheet, Rectangle2D spriteView, String name, int rotationAngle) {

        super(spriteSheet);
        this.spriteView = spriteView;
        this.name = name;
        this.rotationAngle = rotationAngle;

        setRotate(rotationAngle);
        setViewport(spriteView);
    }

    /*
    HALF A TON OF GETTERS AND SETTERS
     */
    public Rectangle2D getSpriteView() {
        return spriteView;
    }

    public void setSpriteView(Rectangle2D spriteView) {
        this.spriteView = spriteView;
    }

    //sprite's width
    public int getSpriteW() {
        return (int) spriteView.getWidth();
    }

    public void setSpriteW(int spriteW) {
        this.spriteW = spriteW;
    }

    //sprite's height
    public int getSpriteH() {
        return (int) spriteView.getHeight();
    }

    public void setSpriteH(int spriteH) {
        this.spriteH = spriteH;
    }

    public Image getSpriteSheet() {
        return spriteSheet;
    }

    public void setSpriteSheet(Image spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    public int getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(int rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }
    
}
