/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package katkilla;

import java.util.ArrayList;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import static katkilla.Config.*;
import static katkilla.General.*;
import static katkilla.Resources.*;
import static katkilla.Animations.*;
import static katkilla.LevelLoader.player;

/**
 *
 * @author Kuba
 */
public class Frames extends Parent {

    KatKilla katKilla;
    LevelLoader loader = new LevelLoader();
    Level level = new Level();
    private static Weapon weapon = new Weapon();
    private Resources resources = new Resources();
    private Entities entities = new Entities();

    private int counter = 0;
    private static boolean isMusicPlaying = false;
    private Label titleLabel = new Label();
    private static Label ammoLabel = new Label();
    private static Label scoreLabel = new Label();
    private static Label currentWeaponLabel = new Label();
    
    private BooleanProperty sector2EnabledProperty = new SimpleBooleanProperty(false);
    private BooleanProperty optionsVisibleProperty = new SimpleBooleanProperty(false);
    
    

    public Frames() {    }

    public Group introScreen() {

        Group group = new Group();
        Pane topLayer = new Pane();
        Pane bottomLayer = new Pane();
        ArrayList<Rectangle> particles = new ArrayList();
        titleLabel.setPrefWidth(APP_WIDTH - 200);
        titleLabel.relocate(100, 75);
        titleLabel.setId("titleLabel");
        titleLabel.setText("  KAT \nKILLA");        

        topLayer.setMinSize(APP_WIDTH + OFFSET, APP_HEIGHT + OFFSET);
        topLayer.setBackground(new Background(new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY, Insets.EMPTY)));
        bottomLayer.setMinSize(APP_WIDTH + OFFSET, APP_HEIGHT + OFFSET);
        bottomLayer.setBackground(new Background(new BackgroundFill(Color.color(0.05, 0.1, 0.15),
                CornerRadii.EMPTY, Insets.EMPTY)));        

        Entities kat = new Entities(SPRITE_SHEET, PLAYER_STRAIGHT, "Kat", 
                0, 0, 0, 0, false, false, false, false, false);
        kat.relocate(APP_WIDTH / 2 - kat.getSpriteW()/ 2, APP_HEIGHT + OFFSET);
        bottomLayer.getChildren().add(kat);
               
        Entities topLayerEnemy = entities.introPuppy(topLayer);
        topLayerEnemy.relocate(APP_WIDTH / 2 - topLayerEnemy.getViewport().getWidth() / 2,
                APP_HEIGHT + topLayerEnemy.getViewport().getHeight());
        topLayerEnemy.setRotate(180);
        Entities bottomLayerEnemy = entities.introPuppy(bottomLayer);
        
        bottomLayerEnemy.relocate(APP_WIDTH / 2 - topLayerEnemy.getViewport().getWidth() / 2,
                APP_HEIGHT / 2);        
        bottomLayerEnemy.setRotate(180);

        Timeline timeline = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.millis(2000),
                new KeyValue(topLayerEnemy.layoutYProperty(), APP_HEIGHT + topLayerEnemy.getViewport().getHeight()));
        KeyFrame kf2 = new KeyFrame(Duration.millis(3000),
                new KeyValue(topLayerEnemy.layoutYProperty(), -topLayerEnemy.getViewport().getHeight()));
        KeyFrame kf3 = new KeyFrame(Duration.millis(4000),
                new KeyValue(topLayer.opacityProperty(), 1.0));
        KeyFrame kf4 = new KeyFrame(Duration.millis(6000),
                new KeyValue(topLayer.opacityProperty(), 0.0));

        timeline.getKeyFrames().addAll(kf1, kf2, kf3, kf4);
        timeline.play();
        
        AnimationTimer timer = new AnimationTimer() {
            Rectangle r;
            
            ArrayList<Weapon> shotLaserRounds = new ArrayList();

            @Override
            public void handle(long now) {
                ++counter;
                if (counter % ONE_SEC == 0) {
                    System.out.println(counter/ONE_SEC);
                }
                if (counter % 10 == 0) {
                    r = speedLines();
                    particles.add(r);
                    r.relocate(Math.random() * APP_WIDTH, 0);
                    bottomLayer.getChildren().add(r);
                }
                if (counter == 120) {
                    playSound(DOG_BARK);
                }
                if (counter > 420) {
                    for (int i = counter; i < 480; ++i) {
                        kat.setLayoutY(kat.getLayoutY() - 0.1);
                    }
                }
                if (counter == 510 || counter == 530 || counter == 550) {
                    playSound(L_LASER_SOUND);
                    shotLaserRounds = weapon.getLightLaser(kat, bottomLayer);
                    for (int i = 0; i < shotLaserRounds.size(); ++i) {
                        int modifier = ((i * kat.getSpriteW() + 5));
                        int modifierSign = Integer.compare(kat.getSpriteW(), modifier);
                        shotLaserRounds.get(i).relocate(kat.getLayoutX() + (i * kat.getViewport().getWidth() + (15 * modifierSign)),
                                kat.getLayoutY() - shotLaserRounds.get(i).getSpriteH());
                    }
                    if (counter == 550) {
                        explosionLarge(bottomLayer, bottomLayerEnemy, Color.ALICEBLUE, 5);
                        blast(bottomLayer, bottomLayerEnemy, 400, Color.ALICEBLUE);
                        shaker(KatKilla.getRoot(), 20, 10, -10);
                        bottomLayer.getChildren().remove(bottomLayerEnemy);
                    }
                }
                if (counter == 600) {
                    playSound(LEVEL_END);
                    bottomLayer.getChildren().add(titleLabel);
                    titleLabel.setOpacity(0.0);
                }
                if (counter > 600) {
                    for (int i = counter; i < 660; ++i) {
                        kat.setViewport(STRAFE_RIGHT);
                        kat.setLayoutX(kat.getLayoutX() + 0.2);

                    }
                }
                if (counter == 720) {
                    playSound(TITLE);
                    Timeline timeline = new Timeline();
                    KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                            new KeyValue(titleLabel.opacityProperty(), 0.0));
                    KeyFrame kf2 = new KeyFrame(Duration.millis(3000),
                            new KeyValue(titleLabel.opacityProperty(), 1.0));
                    timeline.getKeyFrames().addAll(kf1, kf2);
                    timeline.play();
                }
                for (Rectangle r : particles) {
                    if (r.getOpacity() < 0.3) {
                        r.setLayoutY(r.getLayoutY() + 20);
                    } else {
                        r.setLayoutY(r.getLayoutY() + 15);
                    }
                    if (r.getLayoutY() > APP_HEIGHT) {
                        bottomLayer.getChildren().remove(r);
                    }
                }
                shotLaserRounds.forEach((t) -> {
                    if (t.getBoundsInParent().intersects(bottomLayerEnemy.getBoundsInParent())) {
                        enemyHit(bottomLayerEnemy, 1.0);
                        bottomLayer.getChildren().remove(t);
                    }
                });
            }
        };
        timer.start();

        group.getChildren().addAll(bottomLayer, topLayer);

        KatKilla.scene.setOnKeyPressed((KeyEvent event) -> {
            KeyCode key = event.getCode();
            switch (key) {
                case SPACE:
                    KatKilla.clearAndDisplay(menuScreen());
                    timer.stop();
                    particles.clear();
                    break;
            }
            event.consume();
        });

        return group;
    }

    public Group menuScreen() {    
        
        int buttonWidth = 300;
        Group group = new Group();
        Pane bgPane = new Pane(); 
        

        Button  sec1Button = new Button("SECTOR 1"),
                sec2Button = new Button("SECTOR 2"),                            //add when sec 2 is programmed
                optionsButton = new Button("OPTIONS"),
                aboutButton = new Button("ABOUT"),
                exitButton = new Button("EXIT");
        VBox menuContainer = new VBox(25, sec1Button, optionsButton, aboutButton, exitButton);
        Button[] buttons = {sec1Button, sec2Button, optionsButton, aboutButton, exitButton};
        for (int i = 0; i < buttons.length; ++i) {
            buttons[i].setPrefWidth(buttonWidth);
        }
        sec2Button.setDisable(true);
        if (loader.getLevelSelector() >=4 ){
            sec2Button.setDisable(false);
        }
        
        titleLabel.setPrefWidth(APP_WIDTH - 200);
        titleLabel.relocate(100, 75);
        titleLabel.setId("titleLabel");
        titleLabel.setText("  KAT \nKILLA");
        bgPane.setMinSize(APP_WIDTH, APP_HEIGHT);
        menuContainer.setLayoutX(APP_WIDTH / 2);
        menuContainer.setLayoutY(APP_HEIGHT * .3);
        bgPane.getChildren().addAll(titleLabel, menuContainer);
        group.getChildren().addAll(bgPane, optionsMenu());

//        sec2Button.disableProperty().bind(Bindings.not(sector2EnabledProperty));
        sec2Button.disableProperty().bind(sector2EnabledProperty);
                
        sec1Button.setOnAction((event) -> {
            General.stopMusic();
            loader.setLevelSelector(1);
//            selectLevel();
            KatKilla.clearAndDisplay(getReadyScreen());
        });
        sec2Button.setOnAction((event) -> {
            General.stopMusic();
            loader.setLevelSelector(3);
//            selectLevel();
            KatKilla.clearAndDisplay(getReadyScreen());
        });
        optionsButton.setOnAction((event) -> {
            if (optionsVisibleProperty.get() == false) {
                optionsVisibleProperty.set(true);
            } else {
                optionsVisibleProperty.set(false);
            }
        });
        aboutButton.setOnAction((event) -> {
            KatKilla.clearAndDisplay(aboutScreen());
        });
        exitButton.setOnAction((event) -> {
            System.exit(0);
        });
        
        if(!getIsMusicPlaying()) {
            General.playMusic(MENU_MUSIC, 0);
            setIsMusicPlaying(true);
        }
        return group;
    }
    
    public Group optionsMenu() {
        
        int buttonWidth = 70;
        Group group = new Group();
        group.visibleProperty().bind(optionsVisibleProperty);
        Pane bgPane = new Pane();
        bgPane.setId("optionsPane");
        bgPane.setPrefWidth(APP_WIDTH/2 - OFFSET*2);
        
        Label   sfxAdjustLabel = new Label("SFX VOLUME"),
                musicAdjustLabel = new Label("MUSIC VOLUME");

        sfxAdjustLabel.setId("sfxAdjustLabel");
        musicAdjustLabel.setId("musicAdjustLabel");
        
        ToggleGroup sfxToggleGroup = new ToggleGroup();
        sfxToggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });        
        ToggleGroup musicToggleGroup = new ToggleGroup();
        musicToggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });                
        ToggleButton sfxOff = new ToggleButton("OFF"),
                sfxLo = new ToggleButton("LO"),
                sfxMed = new ToggleButton("MED"),
                sfxHi = new ToggleButton("HI"),
                musicOff = new ToggleButton("OFF"),
                musicLo = new ToggleButton("LO"),
                musicMed = new ToggleButton("MED"),
                musicHi = new ToggleButton("HI");
        sfxOff.setToggleGroup(sfxToggleGroup);
        sfxLo.setToggleGroup(sfxToggleGroup);
        sfxMed.setToggleGroup(sfxToggleGroup);
        sfxMed.setSelected(true);
        sfxHi.setToggleGroup(sfxToggleGroup);
        musicOff.setToggleGroup(musicToggleGroup);
        musicLo.setToggleGroup(musicToggleGroup);
        musicMed.setToggleGroup(musicToggleGroup);
        musicMed.setSelected(true);
        musicHi.setToggleGroup(musicToggleGroup);
        
        sfxOff.setOnAction((event) -> {
            Config.setSfxVolume(.0);
        });
        sfxLo.setOnAction((event) -> {
            Config.setSfxVolume(.3);
        });
        sfxMed.setOnAction((event) -> {
            Config.setSfxVolume(.6);
        });
        sfxHi.setOnAction((event) -> {
            Config.setSfxVolume(1);
        });        
        musicOff.setOnAction((event) -> {
            Config.setMusicVolume(.0);
        });
        musicLo.setOnAction((event) -> {
            Config.setMusicVolume(.3);
        });
        musicMed.setOnAction((event) -> {
            Config.setMusicVolume(.6);
        });
        musicHi.setOnAction((event) -> {
            Config.setMusicVolume(1);
        });        
        
        ToggleButton[] toggles = {sfxOff, sfxLo, sfxMed, sfxHi, musicOff, musicLo,
            musicMed, musicHi};
        for (int i = 0; i < toggles.length; ++i) {
            toggles[i].setPrefWidth(buttonWidth);
        }

        HBox sfxVolContainer = new HBox(sfxOff, sfxLo, sfxMed, sfxHi);
        HBox musicVolContainer = new HBox(musicOff, musicLo, musicMed, musicHi);
        VBox mainContainer = new VBox(10, sfxAdjustLabel, sfxVolContainer, musicAdjustLabel,
                musicVolContainer);

        bgPane.getChildren().addAll(mainContainer);
        group.getChildren().addAll(bgPane);        
        group.relocate(OFFSET, APP_HEIGHT / 3);

        return group;
    }

    public Group pauseMenu() {
        int buttonWidth = 300;
        Group group = new Group();
        Pane bgPane = new Pane();

        Button  resumeButton = new Button("RESUME"),
                optionsButton = new Button("OPTIONS"),
                toMenuButton = new Button("EXIT");
        VBox menuBox = new VBox(25, resumeButton, optionsButton, toMenuButton);
        Button[] buttons = {resumeButton, optionsButton, toMenuButton};
        for (int i = 0; i < buttons.length; ++i) {
            buttons[i].setPrefWidth(buttonWidth);
        }

        bgPane.setMinSize(APP_WIDTH, APP_HEIGHT);
        menuBox.setLayoutX(APP_WIDTH / 2);
        menuBox.setLayoutY(APP_HEIGHT * 0.3);
        bgPane.getChildren().addAll(menuBox);
        group.getChildren().addAll(bgPane, optionsMenu());

        resumeButton.setOnAction((event) -> {
            loader.pauseGame();
        });
        optionsButton.setOnAction((event) -> {
            if (optionsVisibleProperty.get() == false) {
                optionsVisibleProperty.set(true);
            } else {
                optionsVisibleProperty.set(false);
            }
        });        
        toMenuButton.setOnAction((event) -> {
            General.stopMusic();
            setIsMusicPlaying(false);
            loader.stopAndClearAll();
            entities.setScore(0);
            KatKilla.clearAndDisplay(menuScreen());
        });

        return group;
    }    

    public Group levelSummary() {
        loader.getGuiLayer().getChildren().clear();
        Group group = new Group();

        //labels 
        Label levelFinishedLabel = new Label("LEVEL " +(loader.getLevelSelector() - 1)+ " COMPLETED!");
        levelFinishedLabel.setId("levelFinishedLabel");
        levelFinishedLabel.setPrefWidth(APP_WIDTH);
        levelFinishedLabel.relocate(OFFSET, 100);
                
        Label scoreTextLabel = new Label("SCORE: ");
        Label scoreNumberLabel = new Label(String.valueOf(player.getScore()));
        Label enemiesKilledTextLabel = new Label("ENEMIES KILLED: ");
        Label enemiesKilledNumberLabel = new Label(String.valueOf(loader.getEnemiesKilled()));
        Label totalEnemiesTextLabel = new Label("TOTAL ENEMIES: ");
        Label totalEnemiesNumberLabel = new Label(String.valueOf(loader.getTotalEnemies()));
        Label overkillLabel = new Label("DOG OVERKILL!!!");
        Label[] labels = {scoreTextLabel, scoreNumberLabel,
                          enemiesKilledTextLabel, enemiesKilledNumberLabel, 
                          totalEnemiesTextLabel, totalEnemiesNumberLabel, overkillLabel};
        for(int i = 0; i < labels.length; i++) {
            labels[i].setId("levelSummaryLabel");
            labels[i].relocate(100, 30*i);
            labels[i].visibleProperty().set(false);
        }
     
        //gridpane arrangement
        int col1 = 300;
        int col2 = 100;
        GridPane gridPane = new GridPane();
        gridPane.add(scoreTextLabel, 0, 0);
        gridPane.add(scoreNumberLabel, 1, 0);
        gridPane.add(enemiesKilledTextLabel, 0, 1);
        gridPane.add(enemiesKilledNumberLabel, 1, 1);
        gridPane.add(totalEnemiesTextLabel, 0, 2);
        gridPane.add(totalEnemiesNumberLabel, 1, 2);
        gridPane.add(overkillLabel, 0, 3);
        gridPane.getColumnConstraints().add(new ColumnConstraints(col1));
        gridPane.getColumnConstraints().add(new ColumnConstraints(col2));
        gridPane.relocate(APP_WIDTH - (col1 + col2), 250);

        Pane bgPane = new Pane();
        bgPane.setMinSize(APP_WIDTH + OFFSET, APP_HEIGHT + OFFSET);
        bgPane.setBackground(new Background(new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY, Insets.EMPTY)));
        group.getChildren().addAll(bgPane, levelFinishedLabel, gridPane);
        group.setTranslateX(APP_WIDTH);
        playSound(PANE_SLIDE);
        
        Timeline tl1 = new Timeline();
        Timeline tl2 = new Timeline();
        
        //timeline1 keyframes
        KeyFrame tl1kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(group.translateXProperty(), APP_WIDTH));
        KeyFrame tl1kf2 = new KeyFrame(Duration.millis(2000),
                new KeyValue(group.translateXProperty(), 0));
        tl1.getKeyFrames().addAll(tl1kf1, tl1kf2);
        loader.getTimelines().add(tl1);
        tl1.play();
        tl1.setOnFinished((event) -> {
            tl2.play();
        });
        
        //timeline2 keyframes
        KeyFrame tl2kf1 = new KeyFrame(Duration.millis(500),
                new KeyValue(levelFinishedLabel.visibleProperty(), true));
        KeyFrame tl2kf2 = new KeyFrame(Duration.millis(1000),
                new KeyValue(scoreTextLabel.visibleProperty(), true));
        KeyFrame tl2kf3 = new KeyFrame(Duration.millis(1500),
                new KeyValue(scoreNumberLabel.visibleProperty(), true));
        KeyFrame tl2kf4 = new KeyFrame(Duration.millis(2000),
                new KeyValue(enemiesKilledTextLabel.visibleProperty(), true));
        KeyFrame tl2kf5 = new KeyFrame(Duration.millis(2500),
                new KeyValue(enemiesKilledNumberLabel.visibleProperty(), true));
        KeyFrame tl2kf6 = new KeyFrame(Duration.millis(3000),
                new KeyValue(totalEnemiesTextLabel.visibleProperty(), true));
        KeyFrame tl2kf7 = new KeyFrame(Duration.millis(3500),
                new KeyValue(totalEnemiesNumberLabel.visibleProperty(), true));
        if(loader.getEnemiesKilled() == loader.getTotalEnemies()) {
            KeyFrame tl2kf8 = new KeyFrame(Duration.millis(4000),
            new KeyValue(overkillLabel.visibleProperty(), true));
            tl2.getKeyFrames().add(tl2kf8);
        }        
                
        tl2.getKeyFrames().addAll(tl2kf1, tl2kf2, tl2kf3, tl2kf4, tl2kf5, tl2kf6, tl2kf7);
        loader.getTimelines().add(tl2);
        tl2.setOnFinished((event) -> {
            keyControl();
            loader.getTimelines().clear();
        });
                
        
        return group;
    }
    
    public Group gameOverScreen() {
        Group group = new Group();
        group.setOpacity(.0);
        Pane bgPane = new Pane();
        int width = 200;

        Label headerLabel = new Label("GAME OVER");
        headerLabel.setId("headerLabel");
        headerLabel.setPrefWidth(APP_WIDTH);
        headerLabel.relocate(0, APP_HEIGHT/3);
        Button tryAgainButton = new Button("TRY AGAIN");
        tryAgainButton.setId("tryAgainButton");
        tryAgainButton.setPrefWidth(width);
        tryAgainButton.relocate((APP_WIDTH/2 - width/2), (APP_HEIGHT - 100));
        tryAgainButton.setDisable(true);
        tryAgainButton.setOpacity(.0);

        tryAgainButton.setOnAction((event) -> {
            General.stopMusic();
            KatKilla.clearAndDisplay(menuScreen());
        });
        
        bgPane.getChildren().addAll(headerLabel, tryAgainButton);
        bgPane.setMinSize(APP_WIDTH + OFFSET, APP_HEIGHT + OFFSET);
        bgPane.setBackground(new Background(new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY, Insets.EMPTY)));
        group.getChildren().addAll(bgPane);        

        Timeline tl1 = new Timeline();
        //timeline keyframes
        KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(group.opacityProperty(), .0),
                new KeyValue(headerLabel.opacityProperty(), .0));
        KeyFrame kf2 = new KeyFrame(Duration.millis(3000),
                new KeyValue(group.opacityProperty(), 1.0),
                new KeyValue(headerLabel.opacityProperty(), .0));
        KeyFrame kf3 = new KeyFrame(Duration.millis(4000),
                new KeyValue(headerLabel.opacityProperty(), 1.0),
                new KeyValue(tryAgainButton.opacityProperty(), .0));
        KeyFrame kf4 = new KeyFrame(Duration.millis(5000),
                new KeyValue(tryAgainButton.opacityProperty(), 1.0),
                new KeyValue(tryAgainButton.disableProperty(), false));
       
        tl1.getKeyFrames().addAll(kf1, kf2, kf3, kf4);
        loader.getTimelines().add(tl1);
        tl1.play();
        tl1.setOnFinished((event) -> {
            loader.stopAndClearAll();
        });

        return group;
    }
    
    public Group getReadyScreen() {
        Group group = new Group();
        Pane bgPane = new Pane();
        int width = 200;

        Label headerLabel = new Label("GET READY!!!");
        headerLabel.setId("headerLabel");
        headerLabel.setPrefWidth(APP_WIDTH);
        headerLabel.relocate(0, APP_HEIGHT/3);
        Button goButton = new Button("GO!");
        goButton.setId("goButton");
        goButton.setPrefWidth(width);
        goButton.relocate((APP_WIDTH/2 - width/2), (APP_HEIGHT - 100));

        goButton.setOnAction((event) -> {
            selectLevel();
        });
        
        Timeline tl1 = new Timeline();
        //timeline keyframes
        KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(headerLabel.visibleProperty(), false));
        KeyFrame kf2 = new KeyFrame(Duration.millis(500),
                new KeyValue(headerLabel.visibleProperty(), true));
        KeyFrame kf3 = new KeyFrame(Duration.millis(1000),
                new KeyValue(headerLabel.visibleProperty(), false));
        KeyFrame kf4 = new KeyFrame(Duration.millis(1500),
                new KeyValue(headerLabel.visibleProperty(), true));
        KeyFrame kf5 = new KeyFrame(Duration.millis(2000),
                new KeyValue(headerLabel.visibleProperty(), false));
        KeyFrame kf6 = new KeyFrame(Duration.millis(2100),
                new KeyValue(headerLabel.visibleProperty(), true));
        KeyFrame kf7 = new KeyFrame(Duration.millis(2200),
                new KeyValue(headerLabel.visibleProperty(), false));
        KeyFrame kf8 = new KeyFrame(Duration.millis(2300),
                new KeyValue(headerLabel.visibleProperty(), true));
        KeyFrame kf9 = new KeyFrame(Duration.millis(2400),
                new KeyValue(headerLabel.visibleProperty(), false));
        KeyFrame kf10 = new KeyFrame(Duration.millis(2500),
                new KeyValue(headerLabel.visibleProperty(), true));

       
        tl1.getKeyFrames().addAll(kf1, kf2, kf3, kf4, kf5, kf6, kf7, kf8, kf9, kf10);
        loader.getTimelines().add(tl1);
        tl1.play();        
        
        bgPane.getChildren().addAll(headerLabel, goButton);
        bgPane.setMinSize(APP_WIDTH + OFFSET, APP_HEIGHT + OFFSET);
        bgPane.setBackground(new Background(new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY, Insets.EMPTY)));
        group.getChildren().addAll(bgPane);        

        return group;        
    }
    
    public Group aboutScreen() {
        Group group = new Group();
        group.setOpacity(.0);
        Pane bgPane = new Pane();
        int width = 200;

        titleLabel.setPrefWidth(APP_WIDTH - 200);
        titleLabel.setId("titleLabel");
        titleLabel.setText("  KAT \nKILLA");
        
        Label creditsLabel = new Label(
                "Programmed by Jakub Ciszak\n" +
                "for fun and learning purposes.\n"+
                "\n"+
                "Music in this version was taken from \n"+
                "NoCopyrightSounds channel on YouTube:\n"+
                "\n\n"+                        
                "Menu: Cold Cinema - Dark Road\n"+
                "Level 1: Infraction - Can You Feel\n"+
                "Level 2: Warriyo - Mortals\n"+
                "\n\n"+
                "No copyright infringement intended.\n"+
                "If you believe this is the case,\n"+
                "please contact me, I'll remove it."+
                "\n\n"+
                "All artwork done by me."+
                "\n\n"+"Enjoy!!!");
        creditsLabel.setId("creditsLabel");
        
        Button toMenuButton = new Button("BACK");
        toMenuButton.setId("toMenuButton");
        toMenuButton.setPrefWidth(width);
        toMenuButton.relocate((APP_WIDTH/2 - width/2), (APP_HEIGHT - 100));
        toMenuButton.setDisable(true);
        toMenuButton.setOpacity(.0);

        toMenuButton.setOnAction((event) -> {
            KatKilla.clearAndDisplay(menuScreen());
        });
        
        VBox container = new VBox(75, titleLabel, creditsLabel);
        container.relocate(100, 75);
        
        bgPane.getChildren().addAll(container, toMenuButton);
        bgPane.setMinSize(APP_WIDTH + OFFSET, APP_HEIGHT + OFFSET);
        bgPane.setBackground(new Background(new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY, Insets.EMPTY)));
        group.getChildren().addAll(bgPane);        

        Timeline tl1 = new Timeline();
        //timeline keyframes
        KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(group.opacityProperty(), .0),
                new KeyValue(titleLabel.opacityProperty(), .0));
        KeyFrame kf2 = new KeyFrame(Duration.millis(1000),
                new KeyValue(group.opacityProperty(), 1.0),
                new KeyValue(toMenuButton.opacityProperty(), .0),
                new KeyValue(titleLabel.opacityProperty(), .0));
        KeyFrame kf3 = new KeyFrame(Duration.millis(2000),
                new KeyValue(titleLabel.opacityProperty(), 1.0),
                new KeyValue(toMenuButton.opacityProperty(), 1.0),
                new KeyValue(toMenuButton.disableProperty(), false));
       
        tl1.getKeyFrames().addAll(kf1, kf2, kf3);
        loader.getTimelines().add(tl1);
        tl1.play();
    
        return group;
    }

    //text displays

    public Group dashboard() {                                        //returns game dashboard pane
        int dashboardX = 25;
        int dashboardY = 5;
        int healthBarHeight = 15;
        //score counter component
        Rectangle healthbar = entities.getHealthBar(loader.getPlayer(), healthBarHeight, 0, OFFSET, loader.getGuiLayer());
        HBox healthbarContainer = new HBox(healthbar);
        healthbarContainer.setPrefWidth(player.getMaxHp());
        scoreLabel = new Label();
        scoreLabel.setId("scoreLabel");
        scoreLabel.textProperty().bind(loader.getPlayer().getScoreProperty().asString());
        //score counter container to keep the size constant
        HBox scoreDisplayContainer = new HBox(scoreLabel);
        scoreDisplayContainer.setPrefWidth(200);
        //selected weapon component
        currentWeaponLabel = new Label();
        currentWeaponLabel.setId("currentWeaponLabel");
        currentWeaponLabel.textProperty().bind(weapon.getObservableWeaponNameProperty());
        currentWeaponLabel.relocate(dashboardX, (dashboardY * 2 + healthBarHeight));
        //weapon ammo component
        ammoLabel = new Label();
        ammoLabel.setId("ammoLabel");
        ammoLabel.textProperty().bind(weapon.getObservableWeaponNameProperty());
        //common container for all dashboard components
        HBox dashboardContainer = new HBox(dashboardX, healthbarContainer, scoreDisplayContainer);
        dashboardContainer.setPrefSize(APP_WIDTH, DASHBOARD_HEIGHT);
        dashboardContainer.relocate(dashboardX, dashboardY);
        ImageView dashboardLine = new ImageView(SPRITE_SHEET);
        dashboardLine.setViewport(DASHBOARD_LINE);

        Group group = new Group(dashboardContainer, currentWeaponLabel, dashboardLine);

        return group;
    }
     
    public void keyControl() {
        KatKilla.scene.setOnKeyPressed((KeyEvent event) -> {
            KeyCode key = event.getCode();
            switch (key) {
                case SPACE: {
                    if(loader.getLevelSelector() <= LEVEL_COUNT) {
                        KatKilla.clearAndDisplay(getReadyScreen());
                    } else {
                        KatKilla.clearAndDisplay(menuScreen());
                    }
//                    selectLevel();
                }
                break;

            }
        });
    }
    
    private void selectLevel() {
        int selector = loader.getLevelSelector();
        if (selector <= LEVEL_COUNT) {
            switch (loader.getLevelSelector()) {
                case 1:
                    KatKilla.clearAndDisplay(loader.loadLevel(level.levelA1()));
                    System.out.println("SELECTOR: " +loader.getLevelSelector());//for testing
                    break;
                case 2:
                    KatKilla.clearAndDisplay(loader.loadLevel(level.levelA2()));
                    System.out.println("SELECTOR: " +loader.getLevelSelector());//for testing
                    break;
            }
        } else {
            KatKilla.clearAndDisplay(menuScreen());
            System.out.println("SELECTOR: " +loader.getLevelSelector());        //for testing
        }
    }
    
    public Rectangle speedLines() {
        Rectangle r1 = new Rectangle(2, 50, Color.GHOSTWHITE);
        r1.setOpacity(0.5);
        Rectangle r2 = new Rectangle(1, 40, Color.GHOSTWHITE);
        r1.setOpacity(0.2);
        Rectangle[] rectangles = {r1, r2};
        Random randomize = new Random();
        Rectangle r = rectangles[randomize.nextInt(rectangles.length)];
        return r;
    }   
    
    
    /*
    HALF A TON OF GETTERS AND SETTERS
    */
    
    public void setSector2Enabled(boolean sector2Enabled) {
        sector2EnabledProperty.set(sector2Enabled);
    }
    
    public boolean getSector2Enabled() {
        return sector2EnabledProperty.get();
    }
    
    public BooleanProperty getSector2EnabledProperty() {
        return sector2EnabledProperty;
    }

    public boolean getIsMusicPlaying() {
        return isMusicPlaying;
    }

    public void setIsMusicPlaying(boolean isMusicPlaying) {
        this.isMusicPlaying = isMusicPlaying;
    }
    
    


}
