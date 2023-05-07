/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package katkilla;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import sun.misc.IOUtils;

/**
 *
 * @author Kuba
 */
public class General {

    public static MediaPlayer mediaPlayer;
    private Config config = new Config();
    
    private static List<MediaPlayer> mediaPlayers = new ArrayList();

    private Image spriteSheet;
    private int spriteX, spriteY, spriteW, spriteH, rotate, count, columns;
    private Duration duration;

    public static ImageView createEntity(Image spriteSheet, int spriteX, int spriteY,
            int spriteW, int spriteH, int rotate, int count, int columns, Duration duration) {

        ImageView iv = new ImageView(spriteSheet);

        iv.setTranslateX(spriteX);
        iv.setTranslateY(spriteY);
        iv.setRotate(rotate);

        iv.setViewport(new Rectangle2D(spriteX, spriteY, spriteW, spriteH));

        final Animation animation = new Animator(
                iv, duration,
                count, columns,
                spriteX, spriteY,
                spriteW, spriteH);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();

        return iv;
    }

    public static void playSound(String resource) {
        AudioClip audio = new AudioClip(KatKilla.class.getResource(resource).toString());
        audio.setVolume(Config.getSfxVolume());
        audio.play();
    }
//   
//    
//    public static MediaPlayer playMedia(String resource, int noOfRepeats) {     //plays indefinitely if noOfRepeats <= 0
//        
//        
//        Media media = new Media(Paths.get(resource).toUri().toString());
//        mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.volumeProperty().bind(Config.getMusicVolumeProperty());
//       
//        if (noOfRepeats <= 0) {
//            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
//        } else {
//            mediaPlayer.setCycleCount(noOfRepeats);
//        }
//        mediaPlayer.play();
//        
//        return mediaPlayer;
//    }
    
    public static void playMusic(String resource, int noOfRepeats) {     //plays indefinitely if noOfRepeats <= 0          
//        Media media = new Media(new File(resource).toURI().toString());
        Media media = new Media(KatKilla.class.getResource(resource).toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayers.add(mediaPlayer);
        mediaPlayer.volumeProperty().bind(Config.getMusicVolumeProperty());
       
        if (noOfRepeats <= 0) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        } else {
            mediaPlayer.setCycleCount(noOfRepeats);
        }
        mediaPlayer.play();
    }
    
    public static void stopMusic() {
        for(MediaPlayer mp: mediaPlayers) {
            mp.stop();
        }
        mediaPlayers.clear();
    }
    

    
    public static int getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    private static class Animator extends Transition {

        private final ImageView imageView;
        private final int count;
        private final int columns;
        private final int offsetX;
        private final int offsetY;
        private final int width;
        private final int height;

        private int lastIndex;

        public Animator(
                ImageView imageView,
                Duration duration,
                int count, int columns,
                int offsetX, int offsetY,
                int width, int height) {
            this.imageView = imageView;
            this.count = count;
            this.columns = columns;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.width = width;
            this.height = height;
            setCycleDuration(duration);
            setInterpolator(Interpolator.LINEAR);
        }

        @Override
        protected void interpolate(double frac) {
            final int index = Math.min((int) Math.floor(frac * count), count - 1);
            if (index != lastIndex) {
                final int x = (index % columns) * width + offsetX;
                final int y = (index / columns) * height + offsetY;
                imageView.setViewport(new Rectangle2D(x, y, width, height));
                lastIndex = index;
            }
        }
    }
    
    public static class MusicPlayer {
        
    }

}
