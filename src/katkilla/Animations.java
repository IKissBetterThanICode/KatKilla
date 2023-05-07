/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package katkilla;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.VLineTo;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import static katkilla.Config.*;
import static katkilla.General.*;
import static katkilla.Resources.*;

/**
 *
 * @author Kuba
 */
public class Animations {

    static LevelLoader loader = new LevelLoader();
    private static Entities entities = new Entities() ;

    /*
    *   Glow effect after projectile hits enemy
     */
    public static void enemyHit(Entities entities, double adjust) {
        Timeline timeline = new Timeline();
        Glow g1 = new Glow(adjust);
        Glow g2 = new Glow(0.0);
        KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(entities.effectProperty(), g1));
        KeyFrame kf2 = new KeyFrame(Duration.millis(100),
                new KeyValue(entities.effectProperty(), g2));
        timeline.getKeyFrames().addAll(kf1, kf2);
        timeline.setCycleCount(1);
        loader.getTimelines().add(timeline);      //add to collection to for game pausing
        timeline.play();
        timeline.setOnFinished(event -> {
            loader.getTimelines().remove(timeline);
        });
    }

    public static ArrayList<Object> explosionSmall(Pane layer, Constructs constructs,
            Color blastColor, Color particleColor, int noOfParticles) {

        ArrayList<Object> particleList = new ArrayList();
        int particleSize = 5;
        int x = (int) constructs.getBoundsInParent().getMinX() + constructs.getSpriteW() / 2;
        int y = (int) constructs.getBoundsInParent().getMinY() + constructs.getSpriteH() / 2;
//        int x = (int) entities.getLayoutX() + entities.getSpriteW() / 2;
//        int y = (int) entities.getLayoutY() + entities.getSpriteH() / 2;
        int radius = constructs.getSpriteW();
        Circle c = new Circle(x, y, 0, blastColor);
        particleList.add(c);

        double sizeRandomizer = (Math.random() * (1 - 0.5)) + 0.5;
        for (int i = 0; i < noOfParticles; ++i) {
            Rectangle r = new Rectangle(particleSize * sizeRandomizer, particleSize * sizeRandomizer, particleColor);
            r.relocate(x, y);
            particleList.add(r);
            Timeline timeline = new Timeline();
            Point2D p2d = new Point2D((getRandom(-200, 200)), (getRandom(-200, 200)));
            KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                    new KeyValue(c.radiusProperty(), 0),
                    new KeyValue(c.opacityProperty(), 1.0),
                    new KeyValue(r.layoutXProperty(), x),
                    new KeyValue(r.layoutYProperty(), y),
                    new KeyValue(r.opacityProperty(), 1.0));
            KeyFrame kf2 = new KeyFrame(Duration.millis(500),
                    new KeyValue(c.radiusProperty(), radius),
                    new KeyValue(c.opacityProperty(), 0.0));
            KeyFrame kf3 = new KeyFrame(Duration.millis(1000),
                    new KeyValue(r.layoutXProperty(), x + p2d.getX()),
                    new KeyValue(r.layoutYProperty(), y + p2d.getY()),
                    new KeyValue(r.opacityProperty(), 0.0));
            timeline.getKeyFrames().addAll(kf1, kf2, kf3);
            timeline.setCycleCount(1);
            loader.getTimelines().add(timeline);   //add to collection to for game pausing
            timeline.play();
            timeline.setOnFinished(event -> {
                loader.getTimelines().remove(timeline);
            });
        }
        for (Object o : particleList) {
            layer.getChildren().add((Node) o);
        }
        return particleList;
    }

    public static ArrayList<Circle> explosionLarge(Pane layer, Entities entities,
            Color blastColor, int noOfBlasts) {

        ArrayList<Circle> particleList = new ArrayList();
        Bounds bounds = entities.getBoundsInParent();
        int radius = entities.getSpriteW() / 2;

        for (int i = 0; i < noOfBlasts; ++i) {
            int randX = getRandom((int) bounds.getMinX(), (int) bounds.getMaxX());
            int randY = getRandom((int) bounds.getMinY(), (int) bounds.getMaxY());
            Circle c = new Circle(randX, randY, 0, blastColor);
            particleList.add(c);
            Timeline timeline = new Timeline();
            int start = i * 200;
            int stop = (i * 200) + 250;
            KeyFrame kf1 = new KeyFrame(Duration.millis(start),
                    (ActionEvent event) -> {
                        playSound(BOOM_1);
                    },
                    new KeyValue(c.radiusProperty(), 0),
                    new KeyValue(c.opacityProperty(), 1.0));
            KeyFrame kf2 = new KeyFrame(Duration.millis(stop),
                    new KeyValue(c.radiusProperty(), radius),
                    new KeyValue(c.opacityProperty(), 0.0));
            timeline.getKeyFrames().addAll(kf1, kf2);
            timeline.setCycleCount(1);
            loader.getTimelines().add(timeline);   //add to collection to for game pausing
            timeline.play();
            timeline.setOnFinished(event -> {
                loader.getTimelines().remove(timeline);
            });
        }
        for (Circle c : particleList) {
            layer.getChildren().add(c);
        }
        return particleList;
    }

    //Enemy explosion blast
    public static Circle blast(Pane layer, Entities entities, int radius, Color color) {
        int x = (int) entities.getBoundsInParent().getMinX() + entities.getSpriteW() / 2;
        int y = (int) entities.getBoundsInParent().getMinY() + entities.getSpriteH() / 2;
        Circle c = new Circle(x, y, 0, color);
        Timeline timeline = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(c.radiusProperty(), 0.0),
                new KeyValue(c.opacityProperty(), 1.0));
        KeyFrame kf2 = new KeyFrame(Duration.millis(500),
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
        layer.getChildren().add(c);
        return c;
    }

    public static void shaker(Node node, int time, int right, int left) {

        Timeline timeline = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(node.translateXProperty(), 0),
                new KeyValue(node.translateYProperty(), 0));
        KeyFrame kf2 = new KeyFrame(Duration.millis(time * 1),
                new KeyValue(node.translateXProperty(), -10),
                new KeyValue(node.translateYProperty(), 0));
        KeyFrame kf3 = new KeyFrame(Duration.millis(time * 3),
                new KeyValue(node.translateXProperty(), 10),
                new KeyValue(node.translateYProperty(), 0));
        KeyFrame kf4 = new KeyFrame(Duration.millis(time * 4),
                new KeyValue(node.translateXProperty(), 0),
                new KeyValue(node.translateYProperty(), 0));
        timeline.getKeyFrames().addAll(kf1, kf2, kf3, kf4);
        timeline.setCycleCount(10);
        timeline.play();        //Do not think it needs a pause function...
    }

    public static void bombTriggered(Entities source, int cycles, Pane layer) {
        Timeline timeline = new Timeline();
        ColorAdjust cadj1 = new ColorAdjust();
        cadj1.setSaturation(0.0);
        ColorAdjust cadj2 = new ColorAdjust();
        cadj2.setSaturation(1.0);

        KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(source.effectProperty(), cadj1));
        KeyFrame kf2 = new KeyFrame(Duration.millis(100),
                new KeyValue(source.effectProperty(), cadj2));
        KeyFrame kf3 = new KeyFrame(Duration.millis(200),
                new KeyValue(source.effectProperty(), cadj1));

        timeline.getKeyFrames().addAll(kf1, kf2, kf3);
        timeline.setCycleCount(cycles);
        loader.getTimelines().add(timeline);      //add to collection to for game pausing
        timeline.play();
        timeline.setOnFinished((event) -> {
            loader.getTimelines().remove(timeline);
            shaker(KatKilla.getRoot(), 10, 5, -5);
            Circle c = source.bombBlast(layer, source, 200);
            source.setIsMarkedForRemoval(true);
            layer.getChildren().remove(source);
            playSound(BOMB);
        });
    }

    public static void overkill(Pane layer) {
        int label1StartY = 200;
        int label2StartY = 300;

        Label label1 = new Label("DOG");
        label1.setPrefWidth(200);
        label1.setPrefHeight(50);
        label1.setTranslateX(-label1.getPrefWidth());
        label1.setTranslateY(label1StartY);
        label1.setFont(Font.font("Calibri", 26));
        label1.setTextFill(Color.RED);
        Label label2 = new Label("OVERKILL");
        label2.setPrefWidth(200);
        label2.setPrefHeight(50);
        label2.setTranslateX(-label1.getPrefWidth());
        label2.setTranslateY(label1StartY);
        label2.setFont(Font.font("Calibri", 26));
        label2.setTextFill(Color.RED);
        layer.getChildren().addAll(label1, label2);

        Timeline timeline = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.millis(0),
                new KeyValue(label1.translateXProperty(), 0),
                new KeyValue(label1.translateYProperty(), label1StartY),
                new KeyValue(label2.translateXProperty(), (APP_WIDTH + OFFSET)),
                new KeyValue(label2.translateYProperty(), label2StartY));
        KeyFrame kf2 = new KeyFrame(Duration.millis(500),
                new KeyValue(label1.translateXProperty(), 200),
                new KeyValue(label1.translateYProperty(), label1StartY),
                new KeyValue(label2.translateXProperty(), (APP_WIDTH + OFFSET)),
                new KeyValue(label2.translateYProperty(), label2StartY));
        KeyFrame kf3 = new KeyFrame(Duration.millis(1000),
                (event) -> {
                    shaker(KatKilla.getRoot(), 10, 10, -10);
                },
                new KeyValue(label1.translateXProperty(), 200),
                new KeyValue(label1.translateYProperty(), label1StartY),
                new KeyValue(label2.translateXProperty(), 300),
                new KeyValue(label2.translateYProperty(), label2StartY));
        KeyFrame kf4 = new KeyFrame(Duration.millis(2000),
                new KeyValue(label1.visibleProperty(), false),
                new KeyValue(label2.visibleProperty(), false));
        timeline.getKeyFrames().addAll(kf1, kf2, kf3, kf4);
        timeline.play();
    }

    /*
    *   ANIMATION PATHS
     */
    public static void setRotation(ImageView imageView, Duration duration) {

        Timeline timeline = new Timeline();
        KeyFrame kf1 = new KeyFrame(duration, new KeyValue(imageView.rotateProperty(), 360));
        timeline.getKeyFrames().add(kf1);

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    //simple vertical path, x- and y-coords start from the source's center
    public static void itemPath(Entities source, Item item, double startX) {
        int startY;
        LineTo line;

        startY = (int) (source.getTranslateY() + source.getSpriteH() / 2);
        line = new LineTo(startX, startY + (FIELD_HEIGHT + (item.getSpriteH() * 2 + OFFSET)));

        MoveTo start1 = new MoveTo(startX, startY);
        item.setTranslateX(startX);
        item.setTranslateY(startY);
        Path path = new Path();
        path.getElements().addAll(start1, line);
        PathTransition pt = new PathTransition(Duration.millis(10000), path, item);
        pt.setInterpolator(Interpolator.LINEAR);
        loader.getPathTransitions().add(pt);
        pt.play();
        pt.setOnFinished((event) -> {
            item.setIsMarkedForRemoval(true);
            loader.getPathTransitions().remove(pt);
        });
    }


    public static void cloudPath(Entities source, double startX, Duration duration) {
        int startY;
        LineTo line;
        startY = -source.getSpriteH();
        line = new LineTo(startX, FIELD_HEIGHT + (source.getSpriteH() + OFFSET));

        Entities shadow = new Entities(SPRITE_SHEET, source.getSpriteView(), "shadow",
                source.getRotationAngle(), source.getIsMarkedForRemoval());
        loader.getEnemies().add(shadow);
        ColorAdjust colorAdjust = new ColorAdjust(-1, -1, -1, -1);

        shadow.setIsNeutral(true);
        shadow.setOpacity(0.1);
//        shadow.setFitWidth(source.getSpriteW() / 2);
//        shadow.setFitHeight(source.getSpriteH() / 2);
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

        MoveTo start1 = new MoveTo(startX, startY);
        source.setTranslateX(startX);
        source.setTranslateY(startY);
        Path path = new Path();
        path.getElements().addAll(start1, line);
        PathTransition pt = new PathTransition(duration, path, source);
        pt.setInterpolator(Interpolator.LINEAR);
        loader.getPathTransitions().add(pt);
        pt.play();
        pt.setOnFinished((event) -> {
            source.setIsMarkedForRemoval(true);
            loader.getPathTransitions().remove(pt);
        });
    }
    
    //moves vertically
    public static void path1(Entities source, double startX, boolean isReversed, Duration delay, Duration duration) {
        int startY;
        LineTo line;
        if (!isReversed) {
            startY = -source.getSpriteH();
            line = new LineTo(startX, FIELD_HEIGHT + (source.getSpriteH() + OFFSET));
        } else {
            startY = FIELD_HEIGHT + source.getSpriteH();
            line = new LineTo(startX, -(source.getSpriteH() + OFFSET));
            source.getTransforms().add(new Rotate(180, source.getSpriteW() / 2, source.getSpriteH() / 2));
        }
        
        Entities shadow = new Entities(SPRITE_SHEET, source.getSpriteView(), "shadow", 
                source.getRotationAngle(), source.getIsMarkedForRemoval());
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

        MoveTo start1 = new MoveTo(startX, startY);
        source.setTranslateX(startX);
        source.setTranslateY(startY);
        Path path = new Path();
        path.getElements().addAll(start1, line);
        PathTransition pt = new PathTransition(duration, path, source);
        pt.setInterpolator(Interpolator.LINEAR);
        pt.setDelay(delay);
        loader.getPathTransitions().add(pt);
        pt.play();
        pt.setOnFinished((event) -> {
            source.setIsMarkedForRemoval(true);
            loader.getPathTransitions().remove(pt);
        });
    }

    //moves vertically along verticalSegment length; 
    //than moves to the player's initial coords and to the bottom
    public static void path2(Entities source, int startX, int verticalSegment,
            Duration delay, Duration duration1, Duration duration2) {

        int playerX = (int) LevelLoader.player.getLayoutX();
        int playerY = (int) LevelLoader.player.getLayoutY();

        int startY = (int) (0 - source.getSpriteH());
        Path path1 = new Path();
        MoveTo start1 = new MoveTo(startX, startY);
        source.setTranslateX(startX);
        source.setTranslateY(startY);
        LineTo line1 = new LineTo(startX, verticalSegment);
        path1.getElements().addAll(start1, line1);
        
        Entities shadow = new Entities(SPRITE_SHEET, source.getSpriteView(), "shadow", 
                source.getRotationAngle(), source.getIsMarkedForRemoval());
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

        Path path2 = new Path();
        MoveTo start2 = new MoveTo(startX, verticalSegment);
        LineTo line2 = new LineTo(playerX, playerY);
        LineTo line3 = new LineTo(playerX, FIELD_HEIGHT + source.getSpriteH());
        path2.getElements().addAll(start2, line2, line3);

        PathTransition pt1 = new PathTransition(duration1, path1, source);
        pt1.setInterpolator(Interpolator.LINEAR);
        pt1.setDelay(delay);
        PathTransition pt2 = new PathTransition(duration2, path2, source);
        pt2.setInterpolator(Interpolator.LINEAR);
        loader.getPathTransitions().add(pt1);
        pt1.play();
        pt1.setOnFinished((event) -> {
            loader.getPathTransitions().remove(pt1);
            loader.getPathTransitions().add(pt2);
            pt2.play();
        });
        pt2.setOnFinished((event) -> {
            source.setIsMarkedForRemoval(true);
            loader.getPathTransitions().remove(pt2);
        });
    }

    //moves vertically along verticalSegment length, stops and moves to the bottom
    public static void path3(Entities source, int startX, int verticalSegment,
            Duration delay, Duration duration1, Duration duration2) {

        int startY = (int) (0 - source.getSpriteH());
        Path path1 = new Path();
        MoveTo start1 = new MoveTo(startX, startY);
        source.setTranslateX(startX);
        source.setTranslateY(startY);
        LineTo line1 = new LineTo(startX, verticalSegment);
        path1.getElements().addAll(start1, line1);

        Path path2 = new Path();
        MoveTo start2 = new MoveTo(startX, verticalSegment);
        LineTo line2 = new LineTo(startX, FIELD_HEIGHT + source.getSpriteH());
        path2.getElements().addAll(start2, line2);
        
        Entities shadow = new Entities(SPRITE_SHEET, source.getSpriteView(), "shadow", 
                source.getRotationAngle(), source.getIsMarkedForRemoval());
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
        
        
        PathTransition pt1 = new PathTransition(duration1, path1, source);
        pt1.setInterpolator(Interpolator.EASE_OUT);
        pt1.setDelay(delay);
        PathTransition pt2 = new PathTransition(duration2, path2, source);
        pt2.setInterpolator(Interpolator.LINEAR);
        loader.getPathTransitions().add(pt1);
        pt1.play();
        pt1.setOnFinished((event) -> {
            loader.getPathTransitions().remove(pt1);
            loader.getPathTransitions().add(pt2);
            pt2.play();
        });
        pt2.setOnFinished((event) -> {
            source.setIsMarkedForRemoval(true);
            loader.getPathTransitions().remove(pt2);
        });
    }
           
    public static void path4(Entities source, int radius, boolean isReversed, Duration delay, Duration duration) {
        int startX, startY, endX, endY, arcStartX, arcStartY = FIELD_HEIGHT / 3;
        ArcTo arc1;
        int rotate = 270;

        if (!isReversed) {
            startX = (int) -source.getBoundsInParent().getMaxX();
            startY = (int) (-source.getBoundsInParent().getMaxY() + OFFSET);
            arcStartX = FIELD_WIDTH / 3;
            endX = FIELD_WIDTH + source.getSpriteW() + OFFSET;
            endY = FIELD_HEIGHT + source.getSpriteW() + OFFSET;
            arc1 = new ArcTo(100, 100, 0, arcStartX - 10, arcStartY, true, true);
        } else {
            startX = (int) (FIELD_WIDTH + OFFSET + source.getBoundsInParent().getMaxX());
            startY = (int) (-source.getBoundsInParent().getMaxY() + OFFSET);
            arcStartX = FIELD_WIDTH - FIELD_WIDTH / 3 + OFFSET;
            endX = -(source.getSpriteW() + OFFSET);
            endY = FIELD_HEIGHT + source.getSpriteW() + OFFSET;
            arc1 = new ArcTo(100, 100, 0, arcStartX + 10, arcStartY, true, false);
        }
        source.setTranslateX(startX);
        source.setTranslateY(startY);
        
        Entities shadow = entities.addShadow(source);

        Path path = new Path();
        MoveTo start = new MoveTo(startX, startY);
        LineTo line1 = new LineTo(arcStartX, arcStartY);
        LineTo line2 = new LineTo(endX, endY);
        path.getElements().addAll(start, line1, arc1, line2);
        source.getTransforms().add(new Rotate(rotate, source.getSpriteW() / 2, source.getSpriteH() / 2));
        shadow.getTransforms().add(new Rotate(rotate));

        PathTransition pt = new PathTransition(duration, path, source);
        pt.setDelay(delay);
        pt.setInterpolator(Interpolator.LINEAR);
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        loader.getPathTransitions().add(pt);
        pt.play();
        pt.setOnFinished((event) -> {
            source.setIsMarkedForRemoval(true);
            loader.getPathTransitions().remove(pt);
        });
    }

    //moves from side along horizontalSegment, than moves vertically to the bottom
    public static void path5(Entities source, int startY, int horizontalSegment, boolean isReversed, Duration delay, Duration duration) {
        int startX, arcRadius = 25;
        HLineTo line1;
        ArcTo arc1;
        VLineTo line2;
        MoveTo start;
        if (!isReversed) {
            startX = -source.getSpriteH();
            start = new MoveTo(startX, startY);
            line1 = new HLineTo(startX + horizontalSegment);
            arc1 = new ArcTo(arcRadius, arcRadius, 0, (startX + horizontalSegment + arcRadius),
                    (startY + arcRadius), false, true);
            line2 = new VLineTo(FIELD_HEIGHT + source.getSpriteH());
        } else {
            startX = FIELD_WIDTH + OFFSET + source.getSpriteH();
            start = new MoveTo(startX, startY);
            line1 = new HLineTo(startX - horizontalSegment);
            arc1 = new ArcTo(arcRadius, arcRadius, 0, (startX - horizontalSegment - arcRadius),
                    (startY + arcRadius), false, false);
            line2 = new VLineTo(FIELD_HEIGHT + source.getSpriteH());
        }

        source.setTranslateX(startX);
        source.setTranslateY(startY);
        
        Entities shadow = new Entities(SPRITE_SHEET, source.getSpriteView(), "shadow", 
                source.getRotationAngle(), source.getIsMarkedForRemoval());
        loader.getEnemies().add(shadow);
        ColorAdjust colorAdjust = new ColorAdjust(-1, -1, -1, -1);
        
        shadow.setIsNeutral(true);
        shadow.setOpacity(0.3);
        shadow.setFitWidth(source.getSpriteW()/2);
        shadow.setFitHeight(source.getSpriteH()/2);
        shadow.setEffect(colorAdjust);
        shadow.translateXProperty().bind(source.translateXProperty().multiply(X_SHADOW_OFFSET));
        shadow.translateYProperty().bind(source.translateYProperty().multiply(Y_SHADOW_OFFSET));                
        shadow.getTransforms().add(new Rotate(270, shadow.getSpriteW() / 2, shadow.getSpriteH() / 2));
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
        
        Path path = new Path();
        path.getElements().addAll(start, line1, arc1, line2);
        source.getTransforms().add(new Rotate(270, source.getSpriteW() / 2, source.getSpriteH() / 2));
        
        PathTransition pt = new PathTransition(duration, path, source);
        pt.setDelay(delay);
        pt.setInterpolator(Interpolator.LINEAR);
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        loader.getPathTransitions().add(pt);
        pt.play();
        pt.setOnFinished((event) -> {
            source.setIsMarkedForRemoval(true);
            loader.getPathTransitions().remove(pt);
        });
    }

    //moves vertically along radial pattern
    public static void path6(Entities source, int startX, int arcRadius, boolean isReversed, Duration delay, Duration duration) {
        int startY = (0 - source.getSpriteH()), reverser = 1;

        MoveTo start = new MoveTo(startX, startY);
        Path path = new Path();
        path.getElements().add(start);

        int noOfSegments = (int) (FIELD_HEIGHT / arcRadius) + 2;                  //overall length of this path

        ArcTo[] segment = new ArcTo[noOfSegments];

        if (!isReversed) {
            for (int i = 0; i < noOfSegments; i++) {
                if (i % 2 == 0) {
                    segment[i] = new ArcTo(arcRadius * 3, arcRadius, 0, startX, arcRadius * reverser, false, true);
                } else {
                    segment[i] = new ArcTo(arcRadius * 3, arcRadius, 0, startX, arcRadius * reverser, false, false);
                }
                ++reverser;
                path.getElements().add(segment[i]);
            }
        } else {
            for (int i = 0; i < noOfSegments; i++) {
                if (i % 2 == 0) {
                    segment[i] = new ArcTo(arcRadius * 3, arcRadius, 0, startX, arcRadius * reverser, false, false);
                } else {
                    segment[i] = new ArcTo(arcRadius * 3, arcRadius, 0, startX, arcRadius * reverser, false, true);
                }
                ++reverser;
                path.getElements().add(segment[i]);
            }
        }

        source.setTranslateX(startX);
        source.setTranslateY(startY);
        source.getTransforms().add(new Rotate(270, source.getSpriteW() / 2, source.getSpriteH() / 2));
        
        Entities shadow = new Entities(SPRITE_SHEET, source.getSpriteView(), "shadow", 
                source.getRotationAngle(), source.getIsMarkedForRemoval());
        loader.getEnemies().add(shadow);
        ColorAdjust colorAdjust = new ColorAdjust(-1, -1, -1, -1);
        
        shadow.setIsNeutral(true);
        shadow.setOpacity(0.3);
        shadow.setFitWidth(source.getSpriteW()/2);
        shadow.setFitHeight(source.getSpriteH()/2);
        shadow.setEffect(colorAdjust);
        shadow.getTransforms().add(new Rotate(270, shadow.getSpriteW() / 2, shadow.getSpriteH() / 2));
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
        

        PathTransition pt = new PathTransition(duration, path, source);
        pt.setDelay(delay);
        pt.setInterpolator(Interpolator.LINEAR);
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        loader.getPathTransitions().add(pt);
        pt.play();
        pt.setOnFinished((event) -> {
            source.setIsMarkedForRemoval(true);
            loader.getPathTransitions().remove(pt);
        });
    }

    //???
    public static void path7(Entities source, int startY, boolean isReversed, Duration delay, Duration duration) {
        int startX, endX;
        HLineTo line;
        MoveTo start;
        if (!isReversed) {
            startX = -source.getSpriteW() - OFFSET;
            endX = FIELD_WIDTH + OFFSET + source.getSpriteW();
            start = new MoveTo(startX, startY);
        } else {
            startX = FIELD_WIDTH + OFFSET + source.getSpriteW();
            endX = -source.getSpriteW();
            start = new MoveTo(startX, startY);
        }
        line = new HLineTo(endX);
        source.setTranslateX(startX);
        source.setTranslateY(startY);
        
        Entities shadow = new Entities(SPRITE_SHEET, source.getSpriteView(), "shadow", 
                source.getRotationAngle(), source.getIsMarkedForRemoval());
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
        

        Path path = new Path();
        path.getElements().addAll(start, line);

        PathTransition pt = new PathTransition(duration, path, source);
        pt.setDelay(delay);
        pt.setInterpolator(Interpolator.LINEAR);
//        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        loader.getPathTransitions().add(pt);
        pt.play();
        pt.setOnFinished((event) -> {
            source.setIsMarkedForRemoval(true);
            loader.getPathTransitions().remove(pt);
        });
    }

    //simple horizontal path; reversible
    public static void path8(Entities source, double startY, boolean isReversed,
            int rotate, Duration delay, Duration duration) {
        int startX;
        HLineTo line;
        if (!isReversed) {
            if (source.getSpriteW() > source.getSpriteH()) {
                startX = -source.getSpriteW();
                line = new HLineTo(FIELD_WIDTH + source.getSpriteW());
            } else {
                startX = -source.getSpriteH();
                line = new HLineTo(FIELD_WIDTH + source.getSpriteH());
            }
        } else {
            if (source.getSpriteW() > source.getSpriteH()) {
                startX = FIELD_HEIGHT + source.getSpriteW();
                line = new HLineTo(-source.getSpriteW());
            } else {
                startX = -source.getSpriteH();
                line = new HLineTo(FIELD_WIDTH + source.getSpriteH());
            }
            startX = FIELD_HEIGHT + source.getSpriteW();
            line = new HLineTo(-source.getSpriteW());
        }

        MoveTo start = new MoveTo(startX, startY);
        source.setTranslateX(startX);
        source.setTranslateY(startY);
        source.setRotate(rotate);

        Entities shadow = new Entities(SPRITE_SHEET, source.getSpriteView(), "shadow", 
                source.getRotationAngle(), source.getIsMarkedForRemoval());
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
        
        Path path = new Path();
        path.getElements().addAll(start, line);
        PathTransition pt = new PathTransition(duration, path, source);
        pt.setInterpolator(Interpolator.LINEAR);
        pt.setDelay(delay);
        loader.getPathTransitions().add(pt);
        pt.play();
        pt.setOnFinished((event) -> {
            source.setIsMarkedForRemoval(true);
            loader.getPathTransitions().remove(pt);
        });
    }

    //simple vertical path to selected Y coordinate, than stops there; reversible
    public static void path9(Entities source, double startX, int verticalSegment,
            boolean isReversed, int rotate, Duration delay, Duration duration) {
        int startY;
        VLineTo line;
        if (!isReversed) {
            startY = -source.getSpriteH();
            line = new VLineTo(verticalSegment);
//            if (source.getSpriteH() > source.getSpriteW()) {
//                startX = -source.getSpriteW();
//                line = new HLineTo(FIELD_WIDTH + source.getSpriteW());                
//            } else {
//                startX = -source.getSpriteH();
//                line = new HLineTo(FIELD_WIDTH + source.getSpriteH());            
//            }
        } else {
            startY = FIELD_HEIGHT + source.getSpriteH();
            line = new VLineTo(verticalSegment);
            source.getTransforms().add(new Rotate(180, source.getSpriteW() / 2, source.getSpriteH() / 2));
        }

        MoveTo start = new MoveTo(startX, startY);
        source.setTranslateX(startX);
        source.setTranslateY(startY);
        source.setRotate(rotate);
        
        Entities shadow = new Entities(SPRITE_SHEET, source.getSpriteView(), "shadow", 
                source.getRotationAngle(), source.getIsMarkedForRemoval());
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
        
        
        Path path = new Path();
        path.getElements().addAll(start, line);
        PathTransition pt = new PathTransition(duration, path, source);
        pt.setInterpolator(Interpolator.LINEAR);
        pt.setDelay(delay);
        loader.getPathTransitions().add(pt);
        pt.play();
    }

    //moves vertically in a zigzac manner
    public static void path10(Entities source, double startX, int xSegment,
            int ySegment, Duration delay, Duration duration) {

        double startY = -source.getSpriteH();
//        +2 to compensate for sprite's y positions to move it out ou the screen
        int noOfSegments = (FIELD_HEIGHT / ySegment) + 2;
        int modifier;
        List<Double> segments = new ArrayList();

        for (int i = 0; i < (noOfSegments * 2); i++) {
            if (i % 2 == 0) {
                modifier = -1;
                segments.add(startX + xSegment * modifier);
            } else {
                modifier = 1;
                segments.add(startX + xSegment * modifier);
            }
            segments.add(startY + (ySegment * i));
        }
        source.relocate(startX, startY);
        
        Entities shadow = new Entities(SPRITE_SHEET, source.getSpriteView(), "shadow", 
                source.getRotationAngle(), source.getIsMarkedForRemoval());
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
        

        Polyline pLine = new Polyline();
        pLine.getPoints().addAll(segments);
        PathTransition pt = new PathTransition();
        pt.setNode(source);
        pt.setDuration(duration);
        pt.setPath(pLine);
        loader.getPathTransitions().add(pt);
        pt.setInterpolator(Interpolator.LINEAR);
        pt.setDelay(delay);
        pt.play();
        //marks entity as dead to remove it from the arraylist and scene        
        pt.setOnFinished(event -> {
            source.setIsMarkedForRemoval(true);
            loader.getPathTransitions().remove(pt);
        });

    }

    public static void bossPath1(Entities source) {
        Polyline pLine = new Polyline();
        pLine.getPoints().addAll(new Double[]{
            source.getTranslateX() + source.getSpriteW() / 2, source.getTranslateY() + source.getSpriteH() / 2,
            0.0 - source.getSpriteW() / 2, 200.0 + source.getSpriteH(),
            FIELD_WIDTH - (source.getSpriteW() * 1.2), 200.0 + source.getSpriteH(),
            source.getTranslateX() + source.getSpriteW() / 2, source.getTranslateY() + source.getSpriteH() / 2});
        PathTransition pt = new PathTransition();
        pt.setNode(source);
        pt.setDuration(Duration.millis(source.getVelocity() * 1));
        pt.setPath(pLine);
        loader.getPathTransitions().add(pt);
        pt.setCycleCount(99);
        pt.play();
        //marks entity as dead to remove it from the arraylist and scene        
        pt.setOnFinished(event -> {
            source.setIsMarkedForRemoval(true);
            loader.getPathTransitions().remove(pt);
//            layer.getChildren().remove(source);
        });
    }

    /*
    *   GETTERS AND SETTERS
     */
}
