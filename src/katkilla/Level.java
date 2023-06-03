   
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package katkilla;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.util.Duration;
import static katkilla.Config.*;
import static katkilla.General.*;
import static katkilla.Resources.*;
import static katkilla.Animations.*;

/**
 *
 * @author Kuba
 */
public class Level extends Parent {

    private int playerDeltaX = 0,
                playerDeltaY = 0,
                counter = 0,                                                    //game loop counter, refreshed @~60 FPS
                scrollSpeed;
    
    private double  hue;                                                        //level background hue
    
    private long    lastTime,
                    removeItem;

    private boolean hasBoss,                                                    //checks is the level is a boss level
                    spawnTime = true;                                           //if true, enemies can spawn; false after boss appears

    private Image   background;                                                 //level background image

    private String  musicFile,
                    name;                                                       //level music
    
    private AnimationTimer levelTimer;
    
    //class initialization
    Weapon weapon;
    KatKilla katKilla;
    Frames frames;
    Item item = new Item();
    Entities entities = new Entities();
    LevelLoader loader = new LevelLoader();
    Resources resources = new Resources();
    Animations animations = new Animations();
    

    //default empty constructor
    public Level() {    }
    
    private Level(String musicFile, String name, Image background, double hue, int scrollSpeed, boolean hasBoss) {
        this.musicFile = musicFile;
        this.name = name;
        this.background = background;
        this.hue = hue;
        this.scrollSpeed = scrollSpeed;
        this.hasBoss = hasBoss;
    }
    
    private void onLevelStart() {
        levelTimer = null;
        loader.setEnemiesKilled(0);
        loader.getTimers().clear();
        loader.clearLevel();
        loader.initializeGame();
        counter = 0;
    }
    
    public void onLevelEnd() {
        loader.endLevel();
        levelTimer.stop();
    }

//    public void stopLevelTimer() {
//        levelTimer.stop();
//    }    


    /*
    GETTERS AND SETTERS
     */

    public String getName() {
        return name;
    }

    public boolean getSpawnTime() {
        return spawnTime;
    }

    public void setSpawnTime(boolean spawnTime) {
        this.spawnTime = spawnTime;
    }

    public String getMusicFile() {
        return musicFile;
    }

    public void setMusicFile(String musicFile) {
        this.musicFile = musicFile;
    }

    public double getHue() {
        return hue;
    }

    public void setHue(double hue) {
        this.hue = hue;
    }

    public Image getBackground() {
        return background;
    }

    public void setBackground(Image background) {
        this.background = background;
    }

    public void setScrollSpeed(int scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    public int getScrollSpeed() {
        return scrollSpeed;
    }
    


    
    /*****************
    *   GAME LEVELS  *
    ******************/
    
    //Level 1: duration = 180 secs
    public Level levelA1() {
        onLevelStart();
        Level lvl = new Level(LEVEL_1_MUSIC, "LVL1", LEVEL_A1, 0.0, 180, false);
        loader.setBossTime(false);
        levelTimer = new AnimationTimer() {
          
            @Override
            public void handle(long now) {                                      //max. counter value: 180 * ~60 = ~ 10800
                ++counter;                                                   
                lastTime = System.currentTimeMillis();
                if (counter % ONE_SEC == 0) {
                    System.out.println(counter / ONE_SEC);                      //level timer display for testing
                    System.out.println("No. of enemies: " +loader.getTotalEnemies());
                    System.out.println("Enemies killed: " +loader.getEnemiesKilled());
                }
                loader.update();
                loader.movePlayer();
                if (loader.getIsShooting()) {
                    loader.shoot();
                }
                if (!loader.getBossTime()) {
                    
                    if (counter == ONE_SEC) {                                   //for testing only
                    }                    
                    //spawn clouds
                    if (counter % (ONE_SEC * 10) == 0 && counter < (ONE_SEC * 170)) {
                        entities.getClouds();
                    }         

                    //spawn random enemies ac intervals set by the level's entitiesModifier
                    if (counter > (ONE_SEC * 5) && counter < (ONE_SEC * 35) && counter % 120 == 0
                            || counter > (ONE_SEC * 50) && counter < (ONE_SEC * 70) && counter % 120 == 0
                            || counter > (ONE_SEC * 100) && counter < (ONE_SEC * 130) && counter % 60 == 0 
                            || counter > (ONE_SEC * 135) && counter < (ONE_SEC * 155) && counter % 60 == 0) {
                              entities = entities.spawnRandomEnemy(loader.getAirObjectLayer());
                    }
                    //spawn preset enemies
                    if (counter == (ONE_SEC * 30)) {
                            Entities[] geArray = {
                            entities = entities.puppyOne(loader.getAirObjectLayer()),
                            entities = entities.puppyOne(loader.getAirObjectLayer()),
                            entities = entities.puppyOne(loader.getAirObjectLayer()),
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path6(geArray[i], 160 + (i * 160), 100,
                                    false, Duration.millis(1000 * i), Duration.millis(5000));
                        }
                    }
                    if (counter == (ONE_SEC * 60) || counter == (ONE_SEC * 128)) {
                            Entities[] geArray = {
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path8(geArray[i], 200 + (i * 200), 
                                    false, 270, Duration.millis(1000 * i), Duration.millis(5000));
                        }                        
                    }                        
                    if (counter == (ONE_SEC * 170)) {
                            Entities[] geArray = {
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path8(geArray[i], 200 + (i * 200), 
                                    true, 90, Duration.millis(1000 * i), Duration.millis(5000));
                        }                        
                    }                        
                    if (counter == (ONE_SEC * 75) || counter == (ONE_SEC * 110)) {
                            Entities[] geArray = {
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                            entities = entities.puppyShooter(loader.getAirObjectLayer())
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path4(geArray[i], 150, true, Duration.millis(1000 * i), Duration.millis(5000));
                        }                        
                    }                                           
                    if (counter == (ONE_SEC * 80)) {
                            Entities[] geArray = {
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                            entities = entities.puppyShooter(loader.getAirObjectLayer())
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path4(geArray[i], 150, false, Duration.millis(1000 * i), Duration.millis(5000));
                        }                        
                    }                                           
                    if (counter == ONE_SEC * 40
                            || counter == ONE_SEC * 85 || counter == ONE_SEC * 95 || counter == ONE_SEC * 98
                            || counter == ONE_SEC * 163 || counter == ONE_SEC * 168 || counter == ONE_SEC * 173) {
                        Entities[] geArray = {
                            entities = entities.puppyOne(loader.getAirObjectLayer()),
                            entities = entities.puppyOne(loader.getAirObjectLayer()),
                            entities = entities.puppyOne(loader.getAirObjectLayer()),
                            entities = entities.puppyOne(loader.getAirObjectLayer()),
                            entities = entities.puppyOne(loader.getAirObjectLayer())
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path3(geArray[i], 100 + (i * 100), 200,
                                    Duration.millis(1000 * i), Duration.millis(2000), Duration.millis(1000));
                        }
                    }
                    if (counter == ONE_SEC  * 45
                            || counter == ONE_SEC * 90) {
                        Entities[] geArray = {
                            entities = entities.puppyTwo(loader.getAirObjectLayer()),
                            entities = entities.puppyTwo(loader.getAirObjectLayer()),
                            entities = entities.puppyTwo(loader.getAirObjectLayer()),
                            entities = entities.puppyTwo(loader.getAirObjectLayer()),
                            entities = entities.puppyTwo(loader.getAirObjectLayer())
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path3(geArray[i], (APP_WIDTH - 100) - (i * 100), 200,
                                    Duration.millis(1000 * i), Duration.millis(2000), Duration.millis(1000));
                        }
                    }
                    if (counter == (ONE_SEC * 132)) {
                            Entities[] geArray = {
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path8(geArray[i], 300 + (i * 300), 
                                    true, 90, Duration.millis(1000 * i), Duration.millis(5000));
                        }                        
                    }     
                } else {
                    onLevelEnd();
                }
            }
        };
        loader.getTimers().add(levelTimer);
        levelTimer.start();
        return lvl;
    }

    public Level levelA2() {
        onLevelStart();
        Level lvl = new Level(LEVEL_2_MUSIC, "LVL2", LEVEL_A1, 0.1, 180, false);
        loader.setBossTime(false);
        levelTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                ++counter;
                loader.update();
                loader.movePlayer();
                if (loader.getIsShooting()) {
                    loader.shoot();                  
                }
                if (counter == ONE_SEC) {

                    }     
                if (counter % ONE_SEC == 0) {
                    System.out.println(counter / ONE_SEC);                      //level timer display for testing
                    System.out.println("No. of enemies: " +loader.getTotalEnemies());
                    System.out.println("Enemies killed: " +loader.getEnemiesKilled());
                }
                if (!loader.getBossTime()) {
                    if (counter % (ONE_SEC * 10) == 0 && counter < (ONE_SEC * 170)) {
                        entities.getClouds();
                    }
                    if (counter > (ONE_SEC * 30) && counter < (ONE_SEC * 70) && counter % 120 == 0 ||
                        counter > (ONE_SEC * 100) && counter < (ONE_SEC * 130) && counter % 60 == 0 ||
                        counter > (ONE_SEC * 150) && counter < (ONE_SEC * 175) && counter % 60 == 0) {
                              entities = entities.spawnRandomEnemy(loader.getAirObjectLayer());
                    }                    
                    if (counter == ONE_SEC * 5 || counter == ONE_SEC * 15) {
                        Entities[] geArray = {
                            entities = entities.puppyTwo(loader.getAirObjectLayer()),
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                            entities = entities.puppyTwo(loader.getAirObjectLayer())
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path2(geArray[i], 150 + (i * 50), 250,
                                    Duration.millis(1000 * i), Duration.millis(2000), Duration.millis(1000));
                            loader.getEnemies().add(geArray[i]);
                        }
                    } 
                    if (counter == ONE_SEC * 10 || counter == ONE_SEC * 20) {
                        Entities[] geArray = {
                            entities = entities.puppyTwo(loader.getAirObjectLayer()),
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                            entities = entities.puppyTwo(loader.getAirObjectLayer())
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path2(geArray[i], 500 - (i * 50), 250,
                                    Duration.millis(1000 * i), Duration.millis(2000), Duration.millis(1000));
                            loader.getEnemies().add(geArray[i]);
                        }
                    } 
                    if (counter == ONE_SEC * 25 || counter == ONE_SEC * 28) {
                        Entities[] geArray = {
                            entities = entities.puppyOne(loader.getAirObjectLayer()),
                            entities = entities.puppyOne(loader.getAirObjectLayer()),
                            entities = entities.puppyOne(loader.getAirObjectLayer()),
                            entities = entities.puppyOne(loader.getAirObjectLayer()),
                            entities = entities.puppyOne(loader.getAirObjectLayer())
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path3(geArray[i], 100 + (i * 100), 200,
                                    Duration.millis(1000 * i), Duration.millis(2000), Duration.millis(1000));
                        }
                    }
                    if (counter == ONE_SEC  * 35 || counter == ONE_SEC * 38) {
                        Entities[] geArray = {
                            entities = entities.puppyTwo(loader.getAirObjectLayer()),
                            entities = entities.puppyTwo(loader.getAirObjectLayer()),
                            entities = entities.puppyTwo(loader.getAirObjectLayer()),
                            entities = entities.puppyTwo(loader.getAirObjectLayer()),
                            entities = entities.puppyTwo(loader.getAirObjectLayer())
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path3(geArray[i], (APP_WIDTH - 100) - (i * 100), 200,
                                    Duration.millis(1000 * i), Duration.millis(2000), Duration.millis(1000));
                        }
                    }  
                    if (counter == (ONE_SEC * 55)) {
                            Entities[] geArray = {
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path8(geArray[i], 300 + (i * 300), 
                                    false, 270, Duration.millis(1000 * i), Duration.millis(5000));
                        }                        
                    }                      
                    if (counter == (ONE_SEC * 55) || counter == (ONE_SEC * 70) || counter == (ONE_SEC * 90)) {
                            Entities[] geArray = {
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                            entities = entities.puppyShooter(loader.getAirObjectLayer())
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path8(geArray[i], 300 + (i * 300), 
                                    false, 270, Duration.millis(1000 * i), Duration.millis(5000));
                        }                        
                    }
                    if (counter == (ONE_SEC * 80) || counter == (ONE_SEC * 140)) {
                            Entities[] geArray = {
                            entities = entities.bomber(loader.getAirObjectLayer()),
                            entities = entities.bomber(loader.getAirObjectLayer()),
                            entities = entities.bomber(loader.getAirObjectLayer()),
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path1(geArray[i], 100 + (i * 200), 
                                    false, Duration.millis(3000 * i), Duration.millis(10000));
                        }
                    }
                    if (counter == (ONE_SEC * 100) || counter == (ONE_SEC * 160)) {
                            Entities[] geArray = {
                            entities = entities.bomber(loader.getAirObjectLayer()),
                            entities = entities.bomber(loader.getAirObjectLayer()),
                            entities = entities.bomber(loader.getAirObjectLayer()),
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path1(geArray[i], FIELD_WIDTH - 100 - (i * 200), 
                                    false, Duration.millis(3000 * i), Duration.millis(10000));
                        }
                    }
                    if (counter == (ONE_SEC * 120) || counter == (ONE_SEC * 150)) {
                            Entities[] geArray = {
                            entities = entities.puppyShooter(loader.getAirObjectLayer()),
                            entities = entities.puppyShooter(loader.getAirObjectLayer())
                        };
                        for (int i = 0; i < geArray.length; i++) {
                            Animations.path9(geArray[i], 150 + (i * 330), 250,
                                    false, 0, Duration.millis(0), Duration.millis(10000));
                        }
                    }
                } else {
                    onLevelEnd();
                }
            }
        };
        loader.getTimers().add(levelTimer);
        levelTimer.start();
        return lvl;
    }

}
