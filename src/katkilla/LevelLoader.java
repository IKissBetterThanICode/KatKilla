
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package katkilla;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.scene.media.Media;
import static katkilla.Config.*;
import static katkilla.General.*;
import static katkilla.Resources.*;
import static katkilla.Animations.*;


/**
 *
 * @author Kuba
 */
public class LevelLoader extends Parent {

//    private AnimationTimer levelTimer;
    private Group gameGroup;                                                    //the game field
    private Timeline scroll;                                                    //level background scroll path
    public static Entities player;

    private static boolean goLeft, //booleans used to control player movement
            goRight,
            goUp,
            goDown,
            isShooting,
            bossTime = false;                                                   //if set to true, the boss appears

    private int playerDeltaX = 0,
            playerDeltaY = 0,
            delay = 0, //delay between weapon shots
            counter = 0;                                                        //game loop counter, refreshed @~60 FPS

    private static int levelSelector = 1, //used to select levels (current level = (0 + 1))
//            killedEnemies, //counts enemies for overkill bonus
            totalEnemies, //total number of enemies in a level
            enemiesKilled;                                           //counts enemies killed in each level

    private long lastShot;                                                      //used for firing delay calculation

    //used to set pause
    private static BooleanProperty isPausedProperty = new SimpleBooleanProperty();
    private static DoubleProperty playerXpositionProperty = new SimpleDoubleProperty();
    private static DoubleProperty playerYpositionProperty = new SimpleDoubleProperty();

    private static Pane backgroundLayer = new Pane(),
            groundObjectLayer = new Pane(),
            bottomLayer = new Pane(),
            airObjectLayer = new Pane(),
            guiLayer = new Pane(),
            topLayer = new Pane();

    private Pane[] layers = {backgroundLayer, backgroundLayer, bottomLayer, airObjectLayer,
        guiLayer, topLayer};

    private static ArrayList<Entities> enemies = new ArrayList();               //a list of all enemies
    private static ArrayList<Item> items = new ArrayList();                     //a list of items
    private static ArrayList<Weapon> weapons = new ArrayList();                 //a common arraylist of projectiles
    private static ArrayList<Weapon> shotLaserRounds = new ArrayList();         //light laser list
    private static ArrayList<Weapon> shotMissileRounds = new ArrayList();       //DF missile list
    private static ArrayList<Timeline> timelines = new ArrayList();             //Timelines arraylist for pause to work
    private static ArrayList<AnimationTimer> timers = new ArrayList();          //Timers arraylist for pause to work
    private static ArrayList<PathTransition> pathTransitions = new ArrayList(); //PathTransitons arraylist for pause to work
    private static ArrayList<Media> media = new ArrayList();                    //arraylist for music files

    //class initialization
    Weapon weapon;
    KatKilla katKilla;
    Frames frames;
    Entities entities;
    Entities playerShadow;
    Resources resources = new Resources();
    Animations animations = new Animations();

    //empty default constructor
    public LevelLoader() {
    }

    private boolean spawnTime = true;           //if true, enemies can spawn; false after boss appears

    public void initializeGame() {

        katKilla = new KatKilla();
        entities = new Entities();
        frames = new Frames();
        weapon = new Weapon();

        weapon.setObservableWeaponName(LIGHTLASER);
        player = Entities.createPlayer();
        playerShadow = playerShadow();
        player.relocate((FIELD_WIDTH / 2 - player.getSpriteW() / 2), FIELD_HEIGHT - 100);
        playerXpositionProperty.set(FIELD_WIDTH / 2 - player.getSpriteW() / 2);
        playerYpositionProperty.set(FIELD_HEIGHT - 100);
        getAirObjectLayer().getChildren().add(player);
        Group pauseMenuGroup = frames.pauseMenu();
        getGuiLayer().getChildren().add(pauseMenuGroup);
        pauseMenuGroup.visibleProperty().bind(isPausedProperty);
        
        isPausedProperty.set(false);
        getEnemies().clear();

        guiLayer.getChildren().add(frames.dashboard());

        controls();
    }

//    private Group loadLevel(Image background, double hue, int scrollSpeed) {
    public Group loadLevel(Level level) {
        getBottomLayer().getChildren().clear();
        getTopLayer().getChildren().clear();
        setTotalEnemies(0);
        General.playMusic (level.getMusicFile(), 0);
        gameGroup = new Group();
        gameGroup.getChildren().addAll(backgroundLayer(level.getBackground(),
                level.getHue(), level.getScrollSpeed()),
                groundObjectLayer, bottomLayer, airObjectLayer, topLayer, guiLayer);

        System.out.println("No. of enemies: " +getTotalEnemies());
        return gameGroup;
        
    }
    
    private Entities playerShadow() {
        Entities shadow = new Entities(SPRITE_SHEET, player.getSpriteView(), "shadow",
                player.getRotationAngle(), player.getIsMarkedForRemoval());
        ColorAdjust colorAdjust = new ColorAdjust(-1, -1, -1, -1);
        
        getGroundObjectLayer().getChildren().add(shadow);
        shadow.setIsNeutral(true);
        shadow.setOpacity(0.3);
        shadow.setFitWidth(player.getSpriteW() / 2);
        shadow.setFitHeight(player.getSpriteH() / 2);
        shadow.setEffect(colorAdjust);
        shadow.translateXProperty().bind(player.layoutXProperty().multiply(X_SHADOW_OFFSET));
        shadow.translateYProperty().bind(player.layoutYProperty().multiply(Y_SHADOW_OFFSET));
        getEnemies().add(shadow);

        player.getIsDeadProperty().bindBidirectional(shadow.getIsDeadProperty());
        return shadow;
    }
        

    private Pane backgroundLayer(Image background, double hue, int scrollSpeed) {
        ImageView iv = new ImageView(background);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(hue);
        iv.setEffect(colorAdjust);
        scroll = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.ZERO,
                new KeyValue(iv.translateYProperty(), 
                        -(iv.getImage().getHeight() - (APP_HEIGHT + OFFSET))));
        KeyFrame kf2 = new KeyFrame(Duration.seconds(scrollSpeed),
                new KeyValue(iv.translateYProperty(), 0));
        scroll.getKeyFrames().addAll(kf1, kf2);
        timelines.add(scroll);
        scroll.play();
        scroll.setOnFinished(event -> {
            setBossTime(true);
        });
        backgroundLayer.getChildren().add(iv);
        return backgroundLayer;
    }

    private void controls() {
        KatKilla.scene.setOnKeyPressed((KeyEvent event) -> {
            KeyCode key = event.getCode();
            switch (key) {
                case LEFT:
                    goLeft = true;
                    player.setViewport(STRAFE_LEFT);
                    break;
                case RIGHT:
                    goRight = true;
                    player.setViewport(STRAFE_RIGHT);
                    break;
                case UP:
                    goUp = true;
                    break;
                case DOWN:
                    goDown = true;
                    break;
                case SPACE:
                    setIsShooting(true);
                    break;
                case ESCAPE:
                    pauseGame();
                    break;
                case DIGIT1:
                    weapon.setObservableWeaponName(LIGHTLASER);
                    break;
                case DIGIT2:
                    weapon.setObservableWeaponName(DFMISSILE);
                    break;
                case DIGIT3:
                    weapon.setObservableWeaponName(SONICCANNON);
                    break;
            }
        });

        KatKilla.scene.setOnKeyReleased((KeyEvent event) -> {

            KeyCode key = event.getCode();
            switch (key) {
                case LEFT:
                    goLeft = false;
                    player.setViewport(PLAYER_STRAIGHT);
                    break;
                case RIGHT:
                    goRight = false;
                    player.setViewport(PLAYER_STRAIGHT);
                    break;
                case UP:
                    goUp = false;
                    break;
                case DOWN:
                    goDown = false;
                    break;
                case SPACE:
                    setIsShooting(false);
                    break;
                case ESCAPE:
                    break;
                case DIGIT0:
                    break;
            }
        });
    }

    public void pauseGame() {
        setIsShooting(false);
        if (!getIsPaused()) {
            for (AnimationTimer at : timers) {
                at.stop();
            }
            for (Timeline tl : timelines) {
                tl.pause();
            }
            for (PathTransition pl : pathTransitions) {
                pl.pause();
            }
            setIsPaused(true);
        } else {
            for (AnimationTimer at : timers) {
                at.start();
            }
            for (Timeline tl : timelines) {
                tl.play();
            }
            for (PathTransition pl : pathTransitions) {
                pl.play();   
            }
            setIsPaused(false);
        }   
    }
    
    public void stopAndClearAll() {
        for (AnimationTimer at : timers) {
            at.stop();
        }   
        getTimers().clear();
//        for (Timeline tl : timelines) {
//            tl.stop();
//        }   
        timelines.clear();
        for (PathTransition pl : pathTransitions) {
            pl.stop();
        }   pathTransitions.clear();
        getGroundObjectLayer().getChildren().clear();
    }

    public void movePlayer() {

        double playerMinX = 0 - (player.getSpriteW() / 2),
                playerMaxX = FIELD_WIDTH - (player.getSpriteW() / 2),
                playerMinY = 0,
                playerMaxY = FIELD_HEIGHT + (player.getSpriteH() / 3);
        // vertical movement
        if (goUp && !goDown) {
            playerDeltaY = -player.getVelocity();
        } else if (!goUp && goDown) {
            playerDeltaY = player.getVelocity();
        } else {
            playerDeltaY = 0;
        }
        // horizontal movement
        if (goLeft && !goRight) {
            playerDeltaX = -player.getVelocity();
        } else if (!goLeft && goRight) {
            playerDeltaX = player.getVelocity();
        } else {
            playerDeltaX = 0;
        }
        double newX = player.getLayoutX() + playerDeltaX;
        double newY = player.getLayoutY() + playerDeltaY;
        if (Double.compare(newY, playerMinY) < 0) {
            newY = playerMinY;
        } else if (Double.compare(newY, playerMaxY) > 0) {
            newY = playerMaxY;
        }
        if (Double.compare(newX, playerMinX) < 0) {
            newX = playerMinX;
        } else if (Double.compare(newX, playerMaxX) > 0) {
            newX = playerMaxX;
        }
        player.setLayoutX(newX);
        player.setLayoutY(newY);
        playerXpositionProperty.set((int) newX);
        playerYpositionProperty.set((int) newY);

    }

    private void moveGameEntity(Entities entities) {                            //verbose
        entities.setLayoutX((int) entities.getLayoutX() + entities.getDx());
        entities.setLayoutY((int) entities.getLayoutY() + entities.getDy());
    }

    private void removeEnemy(Entities entities) {                               //verbose
        // check upper screen bounds
        if ((entities.getLayoutY() + entities.getSpriteH() < 0) && entities.getDy() < 0) {
            entities.setIsMarkedForRemoval(true);
        }
        // check lower screen bounds
        if ((entities.getLayoutY() - entities.getSpriteH() > FIELD_HEIGHT) && entities.getDy() > 0) {
            entities.setIsMarkedForRemoval(true);
        }
        // check right screen bounds
        if ((entities.getLayoutX() - entities.getSpriteW() > FIELD_WIDTH) && entities.getDx() > 0) {
            entities.setIsMarkedForRemoval(true);
        }
        // check left screen bounds
        if ((entities.getLayoutX() + entities.getSpriteW() < 0) && entities.getDx() < 0) {
            entities.setIsMarkedForRemoval(true);
        }
    }

    public void shoot() {
        delay = weapon.getDelay();
        if (lastShot == 0 || System.currentTimeMillis() - lastShot > delay) {
            if (weapon.getObservableWeaponName().equals(LIGHTLASER)) {
                shotLaserRounds = weapon.getLightLaser(this.player, airObjectLayer);
                for (int i = 0; i < shotLaserRounds.size(); ++i) {
                    int modifier = ((i * player.getSpriteW() + 5));
                    int modifierSign = Integer.compare(player.getSpriteW(), modifier);
                    shotLaserRounds.get(i).relocate(player.getLayoutX() + (i * player.getSpriteW() + (15 * modifierSign)),
                            player.getLayoutY() - shotLaserRounds.get(i).getSpriteH());
                    weapon = shotLaserRounds.get(i);
                    getWeapons().add(weapon);
                }
                playSound(weapon.getSoundFile());
            }
            if (weapon.getObservableWeaponName().equals(DFMISSILE)) {
//                if(player.getDfMissileAmmo() > 0) {
                shotMissileRounds = weapon.getDFMissile(this.player, airObjectLayer);
                for (int i = 0; i < shotMissileRounds.size(); ++i) {
                    int modifier = ((i * player.getSpriteW()));
                    shotMissileRounds.get(i).relocate(player.getLayoutX() + modifier,
                            player.getLayoutY());
                    weapon = shotMissileRounds.get(i);
                    getWeapons().add(weapon);
                    player.setDfMissileAmmo(player.getDfMissileAmmo() - 1);
                }
                playSound(weapon.getSoundFile());
//                } else {
//                    playSound(EMPTY);
//                }
            }
            if (weapon.getObservableWeaponName().equals(SONICCANNON)) {
//                if (player.getSonicAmmo() > 0) {
                weapon = weapon.getSonicCannon(this.player, airObjectLayer);
                weapon.relocate(player.getLayoutX(), player.getLayoutY());
                getWeapons().add(weapon);
                playSound(weapon.getSoundFile());
//                    cannonAmmo.remove(goUp);
                player.setSonicAmmo(player.getSonicAmmo() - 1);
//                } else {
//                    playSound(EMPTY);
//                }
            }
            lastShot = System.currentTimeMillis();
        }
    }

    public void update() {
        int enemyKillScore;
        //Collisions between enemies and player projectiles
        for (Entities ge : getEnemies()) {

            for (Weapon w : getWeapons()) {
                enemyKillScore = ge.getHp() * 10;
                if (checkForCollision(w, ge) && !w.getIsNeutralized() && 
                        !ge.getIsNeutral() && !ge.getIsWeapon()) {
                    if (!w.getName().equalsIgnoreCase(SONICCANNON)) {
                        w.setIsNeutralized(true);
                    }
                    int weaponDamage = w.getHp();
                    int enemyHp = ge.getHp();
                    playSound(HIT_1);
                    Animations.enemyHit(ge, 1.0);
                    ge.setHp(enemyHp -= weaponDamage);
                    if (enemyHp <= 0) {
                        ge.setIsDead(true);
//                        setEnemiesKilled(getEnemiesKilled() + 1);
                        player.setScore(player.getScore() + enemyKillScore);    //sets player score
                        playSound(BOOM_1);
                        explosionSmall(airObjectLayer, ge, Color.ALICEBLUE, Color.ORANGERED, 50);
                        airObjectLayer.getChildren().remove(ge);
                        if (ge.getIsBoss()) {
                            explosionLarge(airObjectLayer, ge, Color.ALICEBLUE, 10);
                            shaker(KatKilla.getRoot(), 20, 10, -10);
                            airObjectLayer.getChildren().removeAll(getEnemies());
                        }
                    }
                    if (w.getIsNeutralized()) {
                        airObjectLayer.getChildren().remove(w);
                    }
                }
                if (w.getIsDead()) {
                    getChildren().remove(w);
                }
            }
            if (ge.getIsDead() && !ge.getIsBoss() && !ge.getIsNeutral()) {
                setEnemiesKilled(getEnemiesKilled() + 1);
            }
        }

        //Collisions between enemies (including enemy projectiles) and player
        for (Entities ge : getEnemies()) {
            if (checkForCollision(player, ge) && !ge.getIsNeutral() && !ge.getIsWeapon()) {
                ge.setIsDead(true);
                explosionSmall(airObjectLayer, ge, Color.ALICEBLUE, Color.RED, 50);
//                playSound(PLAYER_HIT);
                playSound(BOOM_1);
                player.setHp(player.getHp() - ge.getHp() * 2);
                airObjectLayer.getChildren().remove(ge);
                if (ge.getIsBoss()) {
                    player.setIsDead(true);
                    getChildren().remove(player);
                }
            }
            if (checkForCollision(player, ge) && !ge.getIsNeutral() && ge.getIsWeapon()) {
                ge.setIsMarkedForRemoval(true);
                explosionSmall(airObjectLayer, player, Color.ALICEBLUE, Color.RED, 50);
                playSound(BOOM_1);
                player.setHp(player.getHp() - ge.getHp());
                airObjectLayer.getChildren().remove(ge);
            }
            if (ge.getName().equalsIgnoreCase("heavyLaser")) {
                ge.setIsMarkedForRemoval(true);
                explosionSmall(airObjectLayer, player, Color.ALICEBLUE, Color.RED, 50);
                playSound(BOOM_1);
                player.setHp(player.getHp() - ge.getHp());
            }
            if (ge.getName().equalsIgnoreCase("Bomber")) {
                if (proximityCheck(ge, player) < 200) {
                    ge.setIsMarkedForRemoval(true);
                    Animations.bombTriggered(ge, 5, airObjectLayer);
                }
            }
        }

        for (Item it : getItems()) {
            if (checkForCollision(player, it)) {
                playSound(ITEM_HEART_COLLECTED);
                airObjectLayer.getChildren().remove(it);
                it.setIsMarkedForRemoval(true);
                if (it.getName().equalsIgnoreCase("Heart")) {
                    if ((player.getHp() + it.getBoost()) <= PLAYER_MAX_HP) {
                        player.setHp(player.getHp() + it.getBoost());
                    } else {
                        player.setHp(PLAYER_MAX_HP);
                    }
                }
            }
        }
        if (player.getHp() <= 0) {
            player.setIsDead(true);
        }
        if (player.getIsDead()) {
            explosionLarge(airObjectLayer, player, Color.ALICEBLUE, 10);
            gameOver();
        }

        //conditions for removing items from collections
        getWeapons().removeIf(Weapon::getIsMarkedForRemoval);
        getItems().removeIf(Item::getIsMarkedForRemoval);
        getEnemies().removeIf(Entities::getIsDead);
        getEnemies().removeIf(Entities::getIsMarkedForRemoval);
    }

    public boolean checkForCollision(Node n1, Node n2) {
        return n1.getBoundsInParent().intersects(n2.getBoundsInParent());

    }

    private int proximityCheck(Entities entities, Entities player) {
        int geX = (int) entities.getTranslateX() + entities.getSpriteW() / 2;
        int geY = (int) entities.getTranslateY() + entities.getSpriteH() / 2;
        int plX = (int) player.getLayoutX() + player.getSpriteW() / 2;
        int plY = (int) player.getLayoutY() + player.getSpriteH() / 2;
        Point2D p1 = new Point2D(geX, geY);
        Point2D p2 = new Point2D(plX, plY);
        return (int) p1.distance(p2);
    }

    public void bossFight(ArrayList entitiesList) {
        setBossTime(false);
        ArrayList<Entities> bossParts = entitiesList;
        for (int i = 0; i < bossParts.size(); ++i) {
            entities = bossParts.get(i);
        }
    }
    
    public void gameOver() {
        getAirObjectLayer().getChildren().remove(player);
        getGroundObjectLayer().getChildren().remove(playerShadow);
        getGuiLayer().getChildren().clear();

        for(AnimationTimer at : timers) {
            at.stop();
        }
        getGuiLayer().getChildren().add(frames.gameOverScreen());
    }


    public void endLevel() {
        for(Weapon w : getWeapons()) {
            getAirObjectLayer().getChildren().remove(w);
        }
        setLevelSelector(getLevelSelector() + 1);                               //increase current level number
        PathTransition path = new PathTransition();                             //this is the end level animation
        path.setNode(player);
        path.setDuration(Duration.millis(3000));
        path.setPath(new Line(
                player.getTranslateX() + X_MOD, player.getTranslateY(),
                player.getTranslateX() + X_MOD, -FIELD_HEIGHT * 2));
        path.play();
        playSound(LEVEL_END);
        frames = new Frames();
        path.setOnFinished(e -> {
            getTopLayer().getChildren().add(frames.levelSummary());
        });
        stopAndClearAll();
        getGroundObjectLayer().getChildren().remove(playerShadow);
        for(Entities ge : getEnemies()) {
            ge.setIsMarkedForRemoval(true);
        }
        getEnemies().clear();
        General.stopMusic();        
    }

    public void clearLevel() {
        for (Pane p : Arrays.asList(layers)) {
            p.getChildren().clear();
        }
    }

    /*
    GETTERS AND SETTERS
     */
    public void setLevelSelector(int levelSelector) {
        this.levelSelector = levelSelector;
    }

    public int getLevelSelector() {
        return levelSelector;
    }

    public Pane getBackgroundLayer() {
        return backgroundLayer;
    }

    public Pane getGroundObjectLayer() {
        return groundObjectLayer;
    }

    public Pane getBottomLayer() {
        return bottomLayer;
    }

    public Pane getAirObjectLayer() {
        return airObjectLayer;
    }

    public Pane getTopLayer() {
        return topLayer;
    }

    public Pane getGuiLayer() {
        return guiLayer;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setIsShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }

    public boolean getIsShooting() {
        return isShooting;
    }

    public ArrayList getEntities() {
        return enemies;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public ArrayList getShotLaserRounds() {
        return shotLaserRounds;
    }

    public ArrayList<Entities> getEnemies() {
        return enemies;
    }

    public ArrayList<Timeline> getTimelines() {
        return timelines;
    }

    public ArrayList<AnimationTimer> getTimers() {
        return timers;
    }

    public ArrayList<PathTransition> getPathTransitions() {
        return pathTransitions;
    }
    
    public static ArrayList<Media> getMedia() {
        return media;
    }

    public boolean getSpawnTime() {
        return spawnTime;
    }

    public void setSpawnTime(boolean spawnTime) {
        this.spawnTime = spawnTime;
    }

    public void setPlayerXposition(int i) {
        playerXpositionProperty.set(i);
    }

    public double getPlayerXposition() {
        return playerXpositionProperty.get();
    }

    public DoubleProperty getPlayerXpositionProperty() {
        return playerXpositionProperty;
    }

    public void setPlayerYposition(int i) {
        playerYpositionProperty.set(i);
    }

    public double getPlayerYposition() {
        return playerYpositionProperty.get();
    }

    public DoubleProperty getPlayerYpositionProperty() {
        return playerYpositionProperty;
    }

    public boolean getIsPaused() {
        return isPausedProperty.get();
    }

    public void setIsPaused(boolean isPaused) {
        isPausedProperty.set(isPaused);
    }

    public BooleanProperty getIsPausedProperty() {
        return isPausedProperty;
    }    

    public boolean getBossTime() {
        return bossTime;
    }

    public void setBossTime(boolean bossTime) {
        this.bossTime = bossTime;
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public void setEnemiesKilled(int enemiesKilled) {
        this.enemiesKilled = enemiesKilled;
    }

    public int getTotalEnemies() {
        return totalEnemies;
    }

    public void setTotalEnemies(int totalEnemies) {
        this.totalEnemies = totalEnemies;
    }

    public Entities getPlayer() {
        return player;
    }

}
