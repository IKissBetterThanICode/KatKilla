package katkilla;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kuba
 */
public class Config {
    
    public static final int OFFSET = 10;       //offset added for placing nodes correctly
                                                //in relation to scenegraph edges
    public static final int APP_WIDTH = 640;
    public static final int APP_HEIGHT = 800;
    public static final double APP_DIAGONAL = Math.sqrt(Math.pow(APP_WIDTH, 2) + Math.pow(APP_HEIGHT, 2));
    public static final int DASHBOARD_HEIGHT = 30;
    public static final int BACKGROUND_PADDING = 10;
    
    public static final int FIELD_WIDTH = APP_WIDTH;
    public static final int FIELD_HEIGHT = APP_HEIGHT - DASHBOARD_HEIGHT;
    
    //player strafe speed in both direstions
    public static final int PLAYER_STRAFE = 7;
    //player maxHP
    public static final int PLAYER_MAX_HP = 100;
    //number of enemies spawned randomly
    public static final int MAX_RANDOM_ENEMIES = 2;
    //number of enemies spawned randomly
    public static final int MAX_DFMISSILE_AMMO = 99;
    //number of enemies spawned randomly
    public static final int MAX_SONIC_AMMO = 99;
    //number of levels
    public static final int LEVEL_COUNT = 2;
    
    
    //option settings
    private static double sfxVolume = .6;
    private static double musicVolume = .6;
    private static DoubleProperty musicVolumeProperty = new SimpleDoubleProperty(musicVolume);
    
    //misc
    public static final int X_MOD = (int) Entities.createPlayer().getSpriteView().getWidth()/2;
    public static final int Y_MOD = (int) Entities.createPlayer().getSpriteView().getHeight()/2;
    public static final double X_SHADOW_OFFSET = 0.9;
    public static final double Y_SHADOW_OFFSET = 0.9;
    public static final int ONE_SEC = 60;
    public static final int OVERKILL_PERIOD = ONE_SEC * 5;

    public static double getSfxVolume() {
        return sfxVolume;
    }

    public static void setSfxVolume(double sfxVolume) {
        Config.sfxVolume = sfxVolume;
    }

    public static void setMusicVolume(double musicVolume) {
        musicVolumeProperty.set(musicVolume);
    }
    
    public static double getMusicVolume() {
        return musicVolumeProperty.get();
    }

    public static DoubleProperty getMusicVolumeProperty() {
        return musicVolumeProperty;
    }

    
    
    
    
    
}
