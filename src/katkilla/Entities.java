/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package katkilla;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import static katkilla.Animations.setRotation;
import static katkilla.Config.*;
import static katkilla.Resources.*;
import static katkilla.General.*;

/**
 *
 * @author Kuba
 */
public class Entities extends Constructs {

    private Entities entities;
    private Frames frames;

    //default empty constructor
    public Entities() {
    }

    private int spriteX, //sprite's X coord
            spriteY, //sprite's Y coord
            spriteW, //sprite's width
            spriteH, //sprite's height
            rotationAngle, //sprite rotation angle
            hp, //hit points (enemy) or damage (projectile)
            velocity, //velocity (PathTransition duration in ms)    
            dfMissileAmmo = 10,
            sonicAmmo = 10;

    private static int maxHp = PLAYER_MAX_HP,
            initialHp = 100;

    private Pane layer;             //Layer to which the game enitty is to be added

    private Image spriteSheet;      //sprite sheet used

    private Rectangle2D spriteView; //sprite viewport

    private Duration duration;      //duration of animation

    private double itemDropChance, //chance of dropping an item
            dx, //delta (movement rate) along X axis   
            dy; //delta (movement rate) along Y axis

    private boolean isDead, //true if entity is dead    
            isBoss, //true if entity is neutral (cannot damage the player and cannot be destroyed)
            isMarkedForRemoval, //if true, it gets removed from collection
            isNeutral, //if true, doesn't hurt the player
            isWeapon, //if true, cannot be destroyed by player's projectiles
            isExplosionTriggered; //if true, the game entity explodes

    private String name;            //entity's name, mainly for testing
    

    private BooleanProperty isDeadProperty = new SimpleBooleanProperty(isDead);
    private BooleanProperty isMarkedForRemovalProperty = new SimpleBooleanProperty(isMarkedForRemoval);
    private IntegerProperty hpProperty = new SimpleIntegerProperty();
    private static IntegerProperty scoreProperty = new SimpleIntegerProperty();
    private DoubleProperty xPositionProperty = new SimpleDoubleProperty();
    private DoubleProperty yPositionProperty = new SimpleDoubleProperty();
    private DoubleProperty rotationAngleProperty = new SimpleDoubleProperty();

    //class initialization
    LevelLoader loader = new LevelLoader();
    Resources resources = new Resources();
    Level level;
    Item item;

    //constructor for enemy game entities
    public Entities(Image spriteSheet, Rectangle2D spriteView, String name, int rotationAngle,
            int hp, int velocity, double itemDropChance, boolean isNeutral, boolean isWeapon, boolean isBoss,
            boolean isDead, boolean isMarkedForRemoval) {

        super(spriteSheet, spriteView, name, rotationAngle, hp);
        this.velocity = velocity;
        this.itemDropChance = itemDropChance;
        this.isNeutral = isNeutral;
        this.isWeapon = isWeapon;
        this.isBoss = isBoss;

        setRotate(rotationAngle);
        setViewport(spriteView);
    }
    
    //constructor for neutral game entities
    public Entities(Image spriteSheet, Rectangle2D spriteView, String name, int rotationAngle, boolean isMarkedForRemoval) {

        super(spriteSheet, spriteView, name, rotationAngle);

        setRotate(rotationAngle);
        setViewport(spriteView);
    }

    //constructor for playable game entities
    private Entities(Image spriteSheet, Rectangle2D spriteView, String name, int rotationAngle,
            int hp, int maxHp, int velocity, boolean isDead) {
        super(spriteSheet, spriteView, name, rotationAngle, hp);
        this.velocity = velocity;
        this.maxHp = maxHp;
        this.isDead = isDead;
    }

    public static Entities createPlayer() {
        Entities player = new Entities(SPRITE_SHEET, PLAYER_STRAIGHT, "Player", 0, initialHp, 100, PLAYER_STRAFE, false);
        player.setHp(initialHp);
        
        return player;
    }

    public Entities spawnRandomEnemy(Pane layer) {
        int randomize = ThreadLocalRandom.current().nextInt(MAX_RANDOM_ENEMIES);
        switch (randomize) {
            case 0:
                entities = chiuaua(layer);
                break;
            case 1:
                entities = pomeranian(layer);
                break;
        }
        return entities;
    }

    /*
    *    ENEMIES
    */
    public Entities introPuppy(Pane layer) {
        int startingHp = 15;
        Entities ge = new Entities(SPRITE_SHEET, PUPPY_ONE, "Bright Puppy",
                0, startingHp, 5000, 0, false, false, false, false, false);
        ge.setHp(startingHp);
        layer.getChildren().add(ge);
        loader.getEnemies().add(ge);
        return ge;
    }
    
    public Entities puppyOne(Pane layer) {
        int startingHp = 15;
        Entities ge = new Entities(SPRITE_SHEET, PUPPY_ONE, "Bright Puppy",
                0, startingHp, 5000, 0, false, false, false, false, false);
        ge.setHp(startingHp);
        layer.getChildren().add(ge);
        loader.getEnemies().add(ge);
        loader.setTotalEnemies(loader.getTotalEnemies() + 1);
        return ge;
    }
    
    public Entities shooterDiagonal(Pane layer) {
        int startingHp = 20;
        Entities ge = new Entities(SPRITE_SHEET, PUPPY_ONE, "Bright Puppy",
                0, startingHp, 5000, 0, false, false, false, false, false);
        ge.setHp(startingHp);
        layer.getChildren().add(ge);
        loader.getEnemies().add(ge);
        loader.setTotalEnemies(loader.getTotalEnemies() + 1);        
        return ge;
    }

    public Entities puppyTwo(Pane layer) {
        int startingHp = 20;
        Entities ge = new Entities(SPRITE_SHEET, PUPPY_TWO, "Dark Puppy",
                0, startingHp, 8000, 0, false, false, false, false, false);
        ge.setHp(startingHp);
        layer.getChildren().add(ge);
        loader.getEnemies().add(ge);
        loader.setTotalEnemies(loader.getTotalEnemies() + 1);
        return ge;
    }    

    public Entities chiuaua(Pane layer) {
        int startingHp = 10;
        Entities ge = new Entities(SPRITE_SHEET, CHIUAUA, "Chiuaua",
                0, startingHp, 5000, 0, false, false, false, false, false);
        ge.setHp(startingHp);
        double startX = getRandom(20, (FIELD_WIDTH - OFFSET - ge.getSpriteW()));
        ge.setTranslateX(startX);
        setRotation(ge, Duration.millis(3000));
        Animations.path1(ge, startX, false, Duration.ZERO, Duration.millis(ge.getVelocity()));
        layer.getChildren().add(ge);
        loader.getEnemies().add(ge);
        loader.setTotalEnemies(loader.getTotalEnemies() + 1);
        return ge;
    }

    public Entities pomeranian(Pane layer) {
        int startingHp = 15;
        Entities ge = new Entities(SPRITE_SHEET, POMERANIAN, "Pomeranian",
                0, startingHp, 8000, 0, false, false, false, false, false);
        ge.setHp(startingHp);
        double startX = getRandom(20, (FIELD_WIDTH - OFFSET - ge.getSpriteW()));
        ge.setTranslateX(startX);
        setRotation(ge, Duration.millis(3000));
        Animations.path1(ge, startX, false, Duration.ZERO, Duration.millis(ge.getVelocity()));        
        layer.getChildren().add(ge);
        loader.getEnemies().add(ge);
        loader.setTotalEnemies(loader.getTotalEnemies() + 1);
        return ge;
    } 

    public Entities bomber(Pane layer) {
        int startingHp = 40;
        Entities ge = new Entities(SPRITE_SHEET, BOMBER, "Bomber",
                0, startingHp, 10000, 0, false, false, false, false, false);
        ge.setHp(startingHp);
        layer.getChildren().add(ge);
        loader.getEnemies().add(ge);
        loader.setTotalEnemies(loader.getTotalEnemies() + 1);
//        calculateDistance(ge, layer);
        return ge;
    }

    public Entities mechaDog(Pane layer) {
        int startingHp = 50;
        Entities ge = new Entities(SPRITE_SHEET, MECHA_DOG, "Mecha Dog",
                0, startingHp, 10000, 50, false, false, false, false, false);
        ge.setHp(startingHp);
        enemyShot(ge, TWIN_FIREBALL, layer, 0, 0, true, 1500, 1600, 1700);
        layer.getChildren().add(ge);
        loader.getEnemies().add(ge);
        loader.setTotalEnemies(loader.getTotalEnemies() + 1);
        return ge;
    }

    public Entities puppyShooter(Pane layer) {
        item = new Item();
        int startingHp = 15;
        Entities ge = new Entities(SPRITE_SHEET, PUPPY_ONE, "Bright Puppy",
                0, startingHp, 5000, 0.2, false, false, false, false, false);
        ge.setHp(startingHp);
        if (!ge.getIsDeadProperty().get()) {
            enemyShot(ge, SHT_DIR, layer, ge.getSpriteW() / 2, ge.getSpriteH(), true, 1500);
        }
        layer.getChildren().add(ge);
        loader.getEnemies().add(ge);
        loader.setTotalEnemies(loader.getTotalEnemies() + 1);
        ge.getIsDeadProperty().addListener((Observable obs) -> {
            double randomize = Math.random();
            if (randomize < ge.getItemDropChance()) {
                dropItem(ge, item.hpIncrease(layer));
            }
        });        
        
        return ge;
    }
    
    public Entities rocketeer(Pane layer) {
        int startingHp = 50;
        Entities ge = new Entities(SPRITE_SHEET, ROCKETEER, "Rocketeer",
                0, startingHp, 10000, 30, false, false, false, false, false);
        ge.setHp(startingHp);
        enemyShot(ge, ROCKET, layer, ge.getSpriteW() - 13, ge.getSpriteH(), true, 2000);
        layer.getChildren().add(ge);
        loader.getEnemies().add(ge);
        loader.setTotalEnemies(loader.getTotalEnemies() + 1);
      
        return ge;
    }
    
    public Entities lesserHulk(Pane layer) {
        int startingHp = 50;
        Entities ge = new Entities(SPRITE_SHEET, LESSER_HULK, "Lesser Hulk",
                0, startingHp, 10000, 20, false, false, false, false, false);
        ge.setHp(startingHp);
        enemyShot(ge, HEAVYLASER, layer, ge.getSpriteW() / 2, ge.getSpriteH(), true, 2000);
        layer.getChildren().add(ge);
        loader.getEnemies().add(ge);
        loader.setTotalEnemies(loader.getTotalEnemies() + 1);
        return ge;
    }
    
//    public ArrayList<Entities> dachshund(Pane layer, Entities... args) {
//        int startingHp = 50;
//
//        Entities head = new Entities(SPRITE_SHEET, BOSS_ONE, "",
//                0, 50, 0, 0, false, false, true, false, false);
//        Entities tail  = new Entities(SPRITE_SHEET, BOSS_ONE, "",
//                0, 50, 0, 0, false, false, true, false, false);
//        for ()
//        Entities center = new Entities(SPRITE_SHEET, BOSS_ONE_RIGHT_PAW, "", //rightPaw is the right one from the viewer's perspective
//                0, 20, 0, 0, false, false, false, false, false);
//        
//        ArrayList<Entities> parts = new ArrayList();
//        parts.add(head);
//        parts.add(tail);
//        parts.add(rightPaw);
//        
//        Entities shadow = addShadow(ge);
//        ge.setHp(startingHp);
//        enemyShot(ge, HEAVYLASER, layer, ge.getSpriteW() / 2, ge.getSpriteH(), true, 2000);
//        layer.getChildren().add(ge);
//        loader.getEnemies().add(ge);
//        loader.setTotalEnemies(loader.getTotalEnemies() + 1);
//        return ge;
//    }

    public ArrayList<Entities> boss1(Pane layer) {
        level = new Level();
        Entities leftPaw = new Entities(SPRITE_SHEET, BOSS_ONE_LEFT_PAW, "", //leftPaw is the left one from the viewer's perspective
                0, 20, 0, 0, false, false, false, false, false);
        leftPaw.setHp(100);
        Entities head = new Entities(SPRITE_SHEET, BOSS_ONE, "",
                0, 50, 0, 0, false, false, true, false, false);
        head.setHp(250);
        Entities rightPaw = new Entities(SPRITE_SHEET, BOSS_ONE_RIGHT_PAW, "", //rightPaw is the right one from the viewer's perspective
                0, 20, 0, 0, false, false, false, false, false);
        rightPaw.setHp(100);

        ArrayList<Entities> bossParts = new ArrayList();
        bossParts.add(head);
        bossParts.add(leftPaw);
        bossParts.add(rightPaw);

        leftPaw.setLayoutX(-13);
        leftPaw.setLayoutY(72);
        rightPaw.setLayoutX(head.getSpriteW() - 57);
        rightPaw.setLayoutY(leftPaw.getLayoutY());

        Group group = new Group(head, leftPaw, rightPaw);

        getHealthBar(head, 10, 50, 50, layer);

        int groupWidth = (int) ((leftPaw.getSpriteW() - leftPaw.getLayoutX())
                + //sum af all groups' children widths minus placement offsets
                head.getSpriteW()
                + (rightPaw.getSpriteW() - rightPaw.getLayoutX()));

        int groupHeight = (int) leftPaw.getLayoutY() + leftPaw.getSpriteH();    //height from the lowest to the highest point of the group

        loader.getEnemies().addAll(bossParts);
//        loader.getEnemies().add(leftPaw);
//        loader.getEnemies().add(rightPaw);
//        loader.getEnemies().add(head);
//        loader.setTotalEnemies(loader.getTotalEnemies() + 1);
        layer.getChildren().addAll(head, leftPaw, rightPaw);

        int startX = OFFSET, startY = 0, endX = (int) (FIELD_WIDTH - groupWidth - OFFSET * 2), endY;
        endY = startY;

        //X and Y offsets for children nodes placement
        int leftPawXOffset = -13;
        int rightPawXOffset = head.getSpriteW() - 57;
        int pawYOffset = 72;

        enemyShot(rightPaw, SPREADFIRE, layer, 0, 0, true, 3000, 4000);
        enemyShot(leftPaw, SPREADFIRE, layer, 0, 0, true, 3000, 4000);

        leftPaw.getIsDeadProperty().addListener((Observable obs) -> {
        });
        rightPaw.getIsDeadProperty().addListener((Observable obs) -> {
        });
        head.getIsDeadProperty().addListener((Observable obs) -> {
            rightPaw.setIsDead(true);
            System.out.println("Right: " +rightPaw.getIsDead());
            leftPaw.setIsDead(true);
            System.out.println("Left: " +rightPaw.getIsDead());
            for (AnimationTimer at : loader.getTimers()) {
                at.stop();
            }
            for (Entities ge : loader.getEnemies()) {
                ge.setIsMarkedForRemoval(true);
            }
            loader.endLevel();
        });

        Timeline tl1 = new Timeline();
        Timeline tl2 = new Timeline();

        KeyFrame tl1kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(leftPaw.layoutXProperty(), -groupWidth + leftPawXOffset),
                new KeyValue(head.layoutXProperty(), -groupWidth),
                new KeyValue(rightPaw.layoutXProperty(), -groupWidth + rightPawXOffset),
                new KeyValue(leftPaw.layoutYProperty(), startY + pawYOffset),
                new KeyValue(head.layoutYProperty(), startY),
                new KeyValue(rightPaw.layoutYProperty(), startY + pawYOffset));
        KeyFrame tl1kf2 = new KeyFrame(Duration.millis(2500),
                new KeyValue(leftPaw.layoutXProperty(), 0 + leftPawXOffset),
                new KeyValue(head.layoutXProperty(), 0),
                new KeyValue(rightPaw.layoutXProperty(), 0 + rightPawXOffset),
                new KeyValue(leftPaw.layoutYProperty(), endY + pawYOffset),
                new KeyValue(head.layoutYProperty(), endY),
                new KeyValue(rightPaw.layoutYProperty(), endY + pawYOffset));
        tl1.getKeyFrames().addAll(tl1kf1, tl1kf2);

        KeyFrame tl2kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(leftPaw.layoutXProperty(), startX + leftPawXOffset),
                new KeyValue(head.layoutXProperty(), startX),
                new KeyValue(rightPaw.layoutXProperty(), startX + rightPawXOffset),
                new KeyValue(leftPaw.layoutYProperty(), startY + pawYOffset),
                new KeyValue(head.layoutYProperty(), startY),
                new KeyValue(rightPaw.layoutYProperty(), startY + pawYOffset));
        KeyFrame tl2kf2 = new KeyFrame(Duration.millis(5000),
                new KeyValue(leftPaw.layoutXProperty(), endX + leftPawXOffset),
                new KeyValue(head.layoutXProperty(), endX),
                new KeyValue(rightPaw.layoutXProperty(), endX + rightPawXOffset),
                new KeyValue(leftPaw.layoutYProperty(), endY + pawYOffset),
                new KeyValue(head.layoutYProperty(), endY),
                new KeyValue(rightPaw.layoutYProperty(), endY + pawYOffset));
        tl2.getKeyFrames().addAll(tl2kf1, tl2kf2);

        loader.getTimelines().add(tl1);
        loader.getTimelines().add(tl2);
        tl1.play();
        tl1.setOnFinished((event) -> {
            tl2.play();
            loader.getTimelines().remove(tl1);
        });
        tl2.setCycleCount(Timeline.INDEFINITE);
        tl2.setAutoReverse(true);
        tl2.setOnFinished((event) -> {
            loader.getTimelines().remove(tl2);
        });

        return bossParts;
    }
    
    public ArrayList<Entities> boss2(Pane layer) {
        level = new Level();
        Entities leftPaw = new Entities(SPRITE_SHEET, BOSS_ONE_LEFT_PAW, "", //leftPaw is the left one from the viewer's perspective
                0, 20, 0, 0, false, false, false, false, false);
        leftPaw.setHp(100);
        Entities head = new Entities(SPRITE_SHEET, BOSS_ONE, "",
                0, 50, 0, 0, false, false, true, false, false);
        head.setHp(250);
        Entities rightPaw = new Entities(SPRITE_SHEET, BOSS_ONE_RIGHT_PAW, "", //rightPaw is the right one from the viewer's perspective
                0, 20, 0, 0, false, false, false, false, false);
        rightPaw.setHp(100);

        ArrayList<Entities> bossParts = new ArrayList();
        bossParts.add(head);
        bossParts.add(leftPaw);
        bossParts.add(rightPaw);

        leftPaw.setLayoutX(-13);
        leftPaw.setLayoutY(72);
        rightPaw.setLayoutX(head.getSpriteW() - 57);
        rightPaw.setLayoutY(leftPaw.getLayoutY());

        Group group = new Group(head, leftPaw, rightPaw);

        Rectangle r = getHealthBar(head, 15, (APP_WIDTH - OFFSET), (APP_HEIGHT/3), layer);
        r.setRotate(90);

        int groupWidth = (int) ((leftPaw.getSpriteW() - leftPaw.getLayoutX())
                + //sum af all groups' children widths minus placement offsets
                head.getSpriteW()
                + (rightPaw.getSpriteW() - rightPaw.getLayoutX()));

        int groupHeight = (int) leftPaw.getLayoutY() + leftPaw.getSpriteH();    //height from the lowest to the highest point of the group

//        loader.getEnemies().addAll(Arrays.asList(bossParts));
        loader.getEnemies().add(leftPaw);
        loader.getEnemies().add(rightPaw);
        loader.getEnemies().add(head);
//        loader.setTotalEnemies(loader.getTotalEnemies() + 1);
        layer.getChildren().addAll(head, leftPaw, rightPaw);

        int startX = OFFSET, startY = 0, endX = (int) (FIELD_WIDTH - groupWidth - OFFSET * 2), endY;
        endY = startY;

        //X and Y offsets for children nodes placement
        int leftPawXOffset = -13;
        int rightPawXOffset = head.getSpriteW() - 57;
        int pawYOffset = 72;

        enemyShot(rightPaw, SPREADFIRE, layer, 0, 0, true, 3000, 4000);
        enemyShot(leftPaw, SPREADFIRE, layer, 0, 0, true, 3000, 4000);

        leftPaw.getIsDeadProperty().addListener((Observable obs) -> {
        });
        rightPaw.getIsDeadProperty().addListener((Observable obs) -> {
        });
        head.getIsDeadProperty().addListener((Observable obs) -> {
            rightPaw.setIsDead(true);
            leftPaw.setIsDead(true);
            for (AnimationTimer at : loader.getTimers()) {
                at.stop();
            }
            for (Entities ge : loader.getEnemies()) {
                ge.setIsMarkedForRemoval(true);
            }
            loader.endLevel();
        });

        Timeline tl1 = new Timeline();
        Timeline tl2 = new Timeline();

        KeyFrame tl1kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(leftPaw.layoutXProperty(), -groupWidth + leftPawXOffset),
                new KeyValue(head.layoutXProperty(), -groupWidth),
                new KeyValue(rightPaw.layoutXProperty(), -groupWidth + rightPawXOffset),
                new KeyValue(leftPaw.layoutYProperty(), startY + pawYOffset),
                new KeyValue(head.layoutYProperty(), startY),
                new KeyValue(rightPaw.layoutYProperty(), startY + pawYOffset));
        KeyFrame tl1kf2 = new KeyFrame(Duration.millis(2500),
                new KeyValue(leftPaw.layoutXProperty(), 0 + leftPawXOffset),
                new KeyValue(head.layoutXProperty(), 0),
                new KeyValue(rightPaw.layoutXProperty(), 0 + rightPawXOffset),
                new KeyValue(leftPaw.layoutYProperty(), endY + pawYOffset),
                new KeyValue(head.layoutYProperty(), endY),
                new KeyValue(rightPaw.layoutYProperty(), endY + pawYOffset));
        tl1.getKeyFrames().addAll(tl1kf1, tl1kf2);

        KeyFrame tl2kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(leftPaw.layoutXProperty(), startX + leftPawXOffset),
                new KeyValue(head.layoutXProperty(), startX),
                new KeyValue(rightPaw.layoutXProperty(), startX + rightPawXOffset),
                new KeyValue(leftPaw.layoutYProperty(), startY + pawYOffset),
                new KeyValue(head.layoutYProperty(), startY),
                new KeyValue(rightPaw.layoutYProperty(), startY + pawYOffset));
        KeyFrame tl2kf2 = new KeyFrame(Duration.millis(5000),
                new KeyValue(leftPaw.layoutXProperty(), endX + leftPawXOffset),
                new KeyValue(head.layoutXProperty(), endX),
                new KeyValue(rightPaw.layoutXProperty(), endX + rightPawXOffset),
                new KeyValue(leftPaw.layoutYProperty(), endY + pawYOffset),
                new KeyValue(head.layoutYProperty(), endY),
                new KeyValue(rightPaw.layoutYProperty(), endY + pawYOffset));
        tl2.getKeyFrames().addAll(tl2kf1, tl2kf2);

        loader.getTimelines().add(tl1);
        loader.getTimelines().add(tl2);
        tl1.play();
        tl1.setOnFinished((event) -> {
            tl2.play();
            loader.getTimelines().remove(tl1);
        });
        tl2.setCycleCount(Timeline.INDEFINITE);
        tl2.setAutoReverse(true);
        tl2.setOnFinished((event) -> {
            loader.getTimelines().remove(tl2);
        });

        return bossParts;
    }

    public Entities getClouds() {

        Random randomize = new Random();
        Entities[] clouds = {
            new Entities(SPRITE_SHEET, CLOUD_1, null, 0, false),
            new Entities(SPRITE_SHEET, CLOUD_1, null, 0, false),
            new Entities(SPRITE_SHEET, CLOUD_1, null, 0, false)
        };
        entities = clouds[randomize.nextInt(clouds.length)];

        int selector = getRandom(1, 3);
        switch (selector) {
            case 1:
                entities.setVelocity(40 * 1000);
                entities.setOpacity(.8);
                loader.getBottomLayer().getChildren().add(entities);
                break;
            case 2:
                entities.setVelocity(20 * 1000);
                entities.setOpacity(.4);
                loader.getTopLayer().getChildren().add(entities);
                break;
        }
        double startX = FIELD_WIDTH * Math.random();
        Animations.path1(entities, startX, false, Duration.ZERO, Duration.millis(entities.getVelocity()));

        return entities;
    }

    public void dropItem(Entities source, Item item) {
//        double randomize = Math.random();
//        if (randomize < source.getItemDropChance()) {
//            
            Animations.itemPath(source, item, (source.getTranslateX() + source.getSpriteW() / 2));
//        }
    }

    public void enemyShot(Entities source, String weaponName, Pane layer, int xMod, int yMod, boolean playIndefinitely, int... args) {
        //xMod = modifies the initial X coord of the projectile's start position
        //yMod = modifies the initial Y coord of the projectile's start position

        Timeline timeline;
        ArrayList<KeyFrame> keyframes = new ArrayList();
        for (int i = 0; i < args.length; ++i) {
            KeyFrame kf = new KeyFrame(Duration.millis(args[i]), (event) -> {
                if (weaponName.equalsIgnoreCase(FIREBALL)) {
                    entities = fireball(xMod, yMod, layer);
                    loader.getEnemies().add(entities);
                }
                if (weaponName.equalsIgnoreCase(ROCKET)) {
                    entities = rocket(source, xMod, yMod, layer);
                    loader.getEnemies().add(entities);
                }
                if (weaponName.equalsIgnoreCase(TWIN_FIREBALL)) {
                    ArrayList<Entities> multiShots = new ArrayList();
                    multiShots = twinFireball(source, 
                    (source.getTranslateX() + source.getSpriteW() * 1.5),
                    (source.getTranslateY() - source.getSpriteH()/2), layer);
                }
                if (weaponName.equalsIgnoreCase(FIREBALL_DIR)) {
                    entities = fireballDirectional(xMod, yMod, 4, layer);
                    loader.getEnemies().add(entities);
                }
                if (weaponName.equalsIgnoreCase(SHT_DIR)) {
                    entities = shtDirectional(source.getTranslateX(), source.getTranslateY(), 4, layer);
                    playSound(DOG_FART);
                    setRotation(entities, Duration.millis(1000));
                    loader.getEnemies().add(entities);
                }
                if (weaponName.equalsIgnoreCase(HEAVYLASER)) {
                    entities = heavyLaser(source, xMod, yMod, layer);
                    entities.translateXProperty().bind(source.translateXProperty());
                    entities.translateYProperty().bind(source.translateYProperty());
                    loader.getEnemies().add(entities);
                }
                if (weaponName.equalsIgnoreCase(SPREADFIRE)) {
                    ArrayList<Entities> shots = spreadFire(0, 0, layer);
                    for (Entities ge : shots) {
                        loader.getEnemies().add(ge);
                        ge.translateXProperty().bind(source.layoutXProperty().add(source.getSpriteW() / 2));
                        ge.translateYProperty().bind(source.layoutYProperty().add(source.getSpriteH()));
                        ge.translateXProperty().unbind();
                        ge.translateYProperty().unbind();
                    }
                }
//                loader.getEnemies().add(entities);
            });
            keyframes.add(kf);
        }
        timeline = new Timeline();
        timeline.getKeyFrames().addAll(keyframes);
        if (playIndefinitely) {
            timeline.setCycleCount(Timeline.INDEFINITE);
        } else {
            timeline.setCycleCount(1);
        }
        loader.getTimelines().add(timeline);
        timeline.play();
        source.getIsMarkedForRemovalProperty().addListener((observable, oldValue, newValue) -> {
            timeline.stop();
            loader.getTimelines().remove(timeline);
        });
        source.getIsDeadProperty().addListener((observable, oldValue, newValue) -> {
            timeline.stop();
            loader.getTimelines().remove(timeline);
        });
        if (source.getIsMarkedForRemoval()) {
            timeline.stop();
            loader.getTimelines().remove(timeline);
        }
    }

    public Rectangle getHealthBar(Entities source, int h, int x, int y, Pane layer) {

        //game entity's HP property
        //healthbar component        
        Rectangle healthBar = new Rectangle();
        healthBar.setId("healthBar");
        healthBar.widthProperty().bind(source.getHpProperty());
        healthBar.relocate(x, y);
        healthBar.setHeight(h);

        //healthbar container to keep the size constant
        HBox healthBarContainer = new HBox(healthBar);
        healthBarContainer.setPrefHeight(h);
        healthBarContainer.setPrefWidth(source.getHpProperty().getValue());
        healthBarContainer.relocate(x, y);

        layer.getChildren().add(healthBarContainer);
        return healthBar;
    }
    
    public Entities addShadow(Entities source) {
        Entities shadow = new Entities(SPRITE_SHEET, source.getSpriteView(), "shadow", 
                source.getRotationAngle(), source.getIsMarkedForRemoval());
//        shadow.setIsNeutral(true);
        loader.getEnemies().add(shadow);
        ColorAdjust colorAdjust = new ColorAdjust(-1, -1, -1, -1);
        shadow.setIsNeutral(true);
        shadow.setOpacity(0.3);
        shadow.setFitWidth(source.getSpriteW()/2);
        shadow.setFitHeight(source.getSpriteH()/2);
        shadow.setEffect(colorAdjust);
        shadow.translateXProperty().bind(source.translateXProperty().multiply(X_SHADOW_OFFSET));
        shadow.translateYProperty().bind(source.translateYProperty().multiply(Y_SHADOW_OFFSET));                
        shadow.rotateProperty().bind(source.rotateProperty());
        loader.getGroundObjectLayer().getChildren().add(shadow);
        source.getIsDeadProperty().bindBidirectional(shadow.getIsDeadProperty());
        source.getIsMarkedForRemovalProperty().bindBidirectional(shadow.getIsMarkedForRemovalProperty());
        source.getIsDeadProperty().addListener((Observable obs) -> {
            loader.getGroundObjectLayer().getChildren().remove(shadow);
        });     
        source.getIsMarkedForRemovalProperty().addListener((Observable obs) -> {
            loader.getGroundObjectLayer().getChildren().remove(shadow);
        });     
     
        return shadow;
    }

    /*
    *   ENEMY WEAPONS
    */
    private Entities fireball(double startX, double startY, Pane layer) {
        int damage = 2;
        Entities ge = new Entities(SPRITE_SHEET, FIREBALL_IMAGE, FIREBALL,
                0, damage, 1000, 0, false, true, false, false, false);
        ge.setHp(damage);
        layer.getChildren().add(ge);
        Timeline timeline = new Timeline();
        ge.relocate(startX, startY);
        KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(ge.layoutXProperty(), startX),
                new KeyValue(ge.layoutYProperty(), startY));
        KeyFrame kf2 = new KeyFrame(Duration.millis(ge.getVelocity()),
                new KeyValue(ge.layoutXProperty(), startX),
                new KeyValue(ge.layoutYProperty(), startY + FIELD_HEIGHT));
        timeline.getKeyFrames().addAll(kf1, kf2);
        loader.getTimelines().add(timeline);
        timeline.play();
        timeline.setOnFinished((event) -> {
            ge.setIsMarkedForRemoval(true);
            loader.getTimelines().remove(timeline);
            layer.getChildren().remove(ge);
        });
        return ge;
    }
    
    private ArrayList<Entities> twinFireball(Entities source, double startX, double startY, Pane layer) {
        int damage = 2;
        int modifier = source.getSpriteW();
        ArrayList<Entities> projectiles = new ArrayList();
        
        for (int i = 0; i < 2; ++i) {        
            Entities ge = new Entities(SPRITE_SHEET, FIREBALL_IMAGE, TWIN_FIREBALL,
                    0, damage, 1000, 0, false, true, false, false, false);
            ge.setHp(damage);
            projectiles.add(ge);
            loader.getEnemies().add(ge);
            layer.getChildren().add(ge);
            Timeline timeline = new Timeline();
            ge.relocate(startX + (i * modifier), startY + (i * modifier));
            KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                    new KeyValue(ge.layoutXProperty(), startX + (i * modifier)),
                    new KeyValue(ge.layoutYProperty(), startY));
            KeyFrame kf2 = new KeyFrame(Duration.millis(ge.getVelocity()),
                    new KeyValue(ge.layoutXProperty(), startX + (i * modifier)),
                    new KeyValue(ge.layoutYProperty(), startY + FIELD_HEIGHT));
            timeline.getKeyFrames().addAll(kf1, kf2);
            loader.getTimelines().add(timeline);
            timeline.play();
            timeline.setOnFinished((event) -> {
                ge.setIsMarkedForRemoval(true);
                loader.getTimelines().remove(timeline);
                layer.getChildren().remove(ge);
            });
        }
        return projectiles;
    }


    private Entities fireballDirectional(double startX, double startY, int duration, Pane layer) {
        int damage = 2;
        Entities ge = new Entities(SPRITE_SHEET, FIREBALL_IMAGE, FIREBALL_DIR,
                0, damage, 0, 0, false, true, false, false, false);
        ge.setHp(damage);
        ge.relocate(startX, startY);
        layer.getChildren().add(ge);
        double dx = startX - loader.getPlayerXposition();
        double dy = startY - loader.getPlayerYposition();
        Point2D target = new Point2D(-dx, -dy);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(
                Duration.millis(duration), (ActionEvent event) -> {
            ge.setLayoutX(ge.getLayoutX() + target.normalize().getX());
            ge.setLayoutY(ge.getLayoutY() + target.normalize().getY());
        });
        timeline.getKeyFrames().add(kf);
        loader.getTimelines().add(timeline);
        timeline.play();
        timeline.setOnFinished((event) -> {
            ge.setIsMarkedForRemoval(true);
            loader.getTimelines().remove(timeline);
            layer.getChildren().remove(ge);
        });
        return ge;
    }
    
    private Entities shtDirectional(double startX, double startY, int duration, Pane layer) {
        int damage = 5;
        Entities ge = new Entities(SPRITE_SHEET, SHT_IMAGE, SHT_DIR,
                0, damage, 0, 0, false, true, false, false, false);
        ge.setHp(damage);
        ge.relocate(startX, startY);
        layer.getChildren().add(ge);
        double dx = startX - loader.getPlayerXposition();
        double dy = startY - loader.getPlayerYposition();
        Point2D target = new Point2D(-dx, -dy);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(
                Duration.millis(duration), (ActionEvent event) -> {
            ge.setLayoutX(ge.getLayoutX() + target.normalize().getX());
            ge.setLayoutY(ge.getLayoutY() + target.normalize().getY());
        });
        timeline.getKeyFrames().add(kf);
        loader.getTimelines().add(timeline);
        timeline.play();
        timeline.setOnFinished((event) -> {
            ge.setIsMarkedForRemoval(true);
            loader.getTimelines().remove(timeline);
            layer.getChildren().remove(ge);
        });
        return ge;
    }
    
    private Entities rocket(Entities source, int xMod, int yMod, Pane layer) {
        int damage = 5;
        Entities ge = new Entities(SPRITE_SHEET, ROCKET_IMAGE, ROCKET,
                0, damage, 0, 0, false, true, false, false, false);
        ge.setHp(damage);
        double startX = (source.getTranslateX() + xMod);
        double startY = (source.getTranslateY() + yMod);
        ge.relocate(startX, startY);
        layer.getChildren().add(ge);

        Timeline timeline = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(ge.layoutXProperty(), startX),
                new KeyValue(ge.layoutYProperty(), startY));
        KeyFrame kf2 = new KeyFrame(Duration.millis(2000),
                new KeyValue(ge.layoutXProperty(), startX),
                new KeyValue(ge.layoutYProperty(), startY + FIELD_HEIGHT));
        timeline.getKeyFrames().addAll(kf1, kf2);
        loader.getTimelines().add(timeline);
        timeline.play();
        timeline.setOnFinished((event) -> {
            ge.setIsMarkedForRemoval(true);
            loader.getTimelines().remove(timeline);
            layer.getChildren().remove(ge);
        });
        return ge;
    }

    private Entities heavyLaser(Entities source, double startX, double startY, Pane layer) {
        int damage = 10;
        Entities ge = new Entities(SPRITE_SHEET, HEAVYLASER_BEAM_IMAGE, HEAVYLASER,
                0, damage, 0, 0, false, true, false, false, false);
        ge.setHp(damage);
        layer.getChildren().add(ge);
        ge.relocate(startX, startY);

        int fullBeamLength = (FIELD_HEIGHT + OFFSET);
        int toPlayerBeamLength = (int) (LevelLoader.player.getLayoutY() - source.translateYProperty().get() + startY);
        Line precursor = new Line(
                source.translateXProperty().get() + startX,
                source.translateYProperty().get() + startY,
                source.translateXProperty().get() + startX,
                source.translateYProperty().get() + fullBeamLength);

        precursor.setFill(Color.BLACK);
        layer.getChildren().add(precursor);
//        int toPlayerBeamLength = (int) (startY + loader.getPlayerYposition());
//        int scaleFactor = ((FIELD_HEIGHT + OFFSET) / ge.getSpriteH());
//        ge.setScaleY(scaleFactor);

        Timeline timeline = new Timeline();
        KeyFrame kf1, kf2;

        if (precursor.getBoundsInParent().intersects(LevelLoader.player.getBoundsInParent())) {
            kf1 = new KeyFrame(Duration.millis(0),
                    new KeyValue(ge.opacityProperty(), 0.1),
                    new KeyValue(ge.layoutXProperty(), startX),
                    new KeyValue(ge.layoutYProperty(), startY),
                    new KeyValue(ge.fitHeightProperty(), (toPlayerBeamLength)));
            kf2 = new KeyFrame(Duration.millis(200),
                    new KeyValue(ge.opacityProperty(), 0.7),
                    new KeyValue(ge.layoutXProperty(), startX),
                    new KeyValue(ge.layoutYProperty(), startY),
                    new KeyValue(ge.fitHeightProperty(), (toPlayerBeamLength)));
        } else {
            kf1 = new KeyFrame(Duration.millis(0),
                    new KeyValue(ge.opacityProperty(), 0.1),
                    new KeyValue(ge.layoutXProperty(), startX),
                    new KeyValue(ge.layoutYProperty(), startY),
                    new KeyValue(ge.fitHeightProperty(), (fullBeamLength)));
            kf2 = new KeyFrame(Duration.millis(200),
                    new KeyValue(ge.opacityProperty(), 0.7),
                    new KeyValue(ge.layoutXProperty(), startX),
                    new KeyValue(ge.layoutYProperty(), startY),
                    new KeyValue(ge.fitHeightProperty(), (fullBeamLength)));
        }

        timeline.getKeyFrames().addAll(kf1, kf2);
        loader.getTimelines().add(timeline);
        timeline.play();
        timeline.setOnFinished((event) -> {
            ge.setIsMarkedForRemoval(true);
            loader.getTimelines().remove(timeline);
            layer.getChildren().removeAll(ge, precursor);
        });
        return ge;
    }

    public ArrayList<Entities> spreadFire(double startX, double startY, Pane layer) {
        int damage = 2;
        ArrayList<Entities> shots = new ArrayList();
        for (int i = 0; i < 20; ++i) {
            Entities ge = new Entities(SPRITE_SHEET, SPREADFIRE_IMAGE, SPREADFIRE,
                    0, damage, 0, 0, false, true, false, false, false);
            ge.setHp(damage);
            shots.add(ge);
            ge.relocate(startX, startY);
            layer.getChildren().add(ge);
            double dx = startX + 100 * Math.cos(10 * i);
            double dy = startX + 100 * Math.sin(10 * i);
            Point2D target = new Point2D(dx, dy);

            Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            KeyFrame kf = new KeyFrame(
                    Duration.millis(3), (ActionEvent event) -> {
                ge.setLayoutX(ge.getLayoutX() + target.normalize().getX());
                ge.setLayoutY(ge.getLayoutY() + target.normalize().getY());
            });
            timeline.getKeyFrames().add(kf);
            loader.getTimelines().add(timeline);
            timeline.play();
        }
        return shots;
    }

    public Circle bombBlast(Pane layer, Entities entities, int radius) {
        int x = (int) entities.getBoundsInParent().getMinX() + entities.getSpriteW() / 2;
        int y = (int) entities.getBoundsInParent().getMinY() + entities.getSpriteH() / 2;
        Circle c = new Circle(x, y, 0, Color.ORANGERED);
        layer.getChildren().add(c);
        Timeline timeline = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(c.radiusProperty(), 0.0),
                new KeyValue(c.opacityProperty(), 1.0));
        KeyFrame kf2 = new KeyFrame(Duration.millis(500),
                (event) -> {
                    if (c.getBoundsInParent().intersects(LevelLoader.player.getBoundsInParent())) {
                        loader.getPlayer().setHp(loader.getPlayer().getHp() - 20);
                    }
                },
                new KeyValue(c.radiusProperty(), radius),
                new KeyValue(c.opacityProperty(), 0.0));
        timeline.getKeyFrames().addAll(kf1, kf2);
        timeline.setCycleCount(1);
        loader.getTimelines().add(timeline);      //add to collection to for game pausing
        timeline.play();
        timeline.setOnFinished(e -> {
            c.setVisible(false);
            loader.getTimelines().remove(timeline);
        });
        
        return c;
    }


    /*
    HALF A TON OF GETTERS AND SETTERS
     */
    public int getSpriteX() {
        return spriteX;
    }

    public void setSpriteX(int spriteX) {
        this.spriteX = spriteX;
    }

    public int getSpriteY() {
        return spriteY;
    }

    public void setSpriteY(int spriteY) {
        this.spriteY = spriteY;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    @Override
    public void setHp(int hp) {
        hpProperty.set(hp);
    }

    @Override
    public int getHp() {
        return hpProperty.get();
    }
    
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }
    
    public int getMaxHp() {
        return maxHp;
    }    

    public IntegerProperty getHpProperty() {
        return hpProperty;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getVelocity() {
        return velocity;
    }

    public double getItemDropChance() {
        return itemDropChance;
    }

    public void setItemDropChance(double itemDropChance) {
        this.itemDropChance = itemDropChance;
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

    public void setIsBoss(boolean isBoss) {
        this.isBoss = isBoss;
    }

    public boolean getIsBoss() {
        return isBoss;
    }

    public void setIsWeapon(boolean isWeapon) {
        this.isWeapon = isWeapon;
    }

    public boolean getIsWeapon() {
        return isWeapon;
    }

    public void setIsNeutral(boolean isNeutral) {
        this.isNeutral = isNeutral;
    }

    public boolean getIsNeutral() {
        return isNeutral;
    }

    public void setIsExplosionTriggered(boolean isExplosionTriggered) {
        this.isExplosionTriggered = isExplosionTriggered;
    }

    public boolean getIsExplosionTriggered() {
        return isExplosionTriggered;
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
    
    public void setScore(int i) {
        scoreProperty.set(i);
    }
    
    public int getScore() {
        return scoreProperty.get();
    }
    
    public IntegerProperty getScoreProperty() {
        return scoreProperty;
    }
    
    public int getDfMissileAmmo() {
        return dfMissileAmmo;
    }

    public void setDfMissileAmmo(int dfMissileAmmo) {
        this.dfMissileAmmo = dfMissileAmmo;
    }

    public int getSonicAmmo() {
        return sonicAmmo;
    }

    public void setSonicAmmo(int sonicAmmo) {
        this.sonicAmmo = sonicAmmo;
    }    

}
