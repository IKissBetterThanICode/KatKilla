 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package katkilla;

import java.io.File;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

/**
 *
 * @author Kuba
 */
public class Resources {

    public static final Image SPRITE_SHEET = new Image(KatKilla.class.getResource("/graphics/sprites.png").toExternalForm());
    
    //Level background images
    public static final Image LEVEL_A1 = new Image(KatKilla.class.getResource("/graphics/backgrounds/level1.png").toExternalForm());
    public static final Image LEVEL_A2 = new Image(KatKilla.class.getResource("/graphics/backgrounds/level2.png").toExternalForm());
    public static final Image LEVEL_A3 = new Image(KatKilla.class.getResource("/graphics/backgrounds/level3.png").toExternalForm());
//    public static final Image LEVEL_B1 = new Image(KatKilla.class.getResource("/graphics/backgrounds/level1.png").toExternalForm());
//    public static final Image LEVEL_B2 = new Image(KatKilla.class.getResource("/graphics/backgrounds/level2.png").toExternalForm());
//    public static final Image LEVEL_B3 = new Image(KatKilla.class.getResource("/graphics/backgrounds/level3.png").toExternalForm());

    /*
    * When entering coordinates of a sprite on a sprite sheet, always add 1 pixel
    * above the top pixel of the sprite; otherwise the sprite migh not display
    * properly.
    *
    * 1st value in the constructor is X cord of the left-top pixel of a sprite on the sprit sheet.
    * 2nd value in the constructor is Y cord of the left-top pixel of a sprite on the sprit sheet.
    * 3rd and 4th values are sprite width and height respectively.
    */
    
    
    //Player images
    public static final Rectangle2D PLAYER_STRAIGHT = new Rectangle2D(0, 2, 48, 70);
    public static final Rectangle2D STRAFE_LEFT = new Rectangle2D(60, 2, 38, 70);
    public static final Rectangle2D STRAFE_RIGHT = new Rectangle2D(120, 2, 38, 70);
    

    //Enemy images
    public static final Rectangle2D PUPPY_ONE = new Rectangle2D(0, 301, 51, 98);
    public static final Rectangle2D PUPPY_TWO = new Rectangle2D(60, 301, 51, 98);
    public static final Rectangle2D CHIUAUA = new Rectangle2D(240, 301, 50, 95);
    public static final Rectangle2D POMERANIAN = new Rectangle2D(300, 301, 66, 78);
    public static final Rectangle2D BOMBER = new Rectangle2D(0, 501, 80, 151);
    public static final Rectangle2D MECHA_DOG = new Rectangle2D(242, 501, 87, 111);
    public static final Rectangle2D ROCKETEER = new Rectangle2D(360, 501, 62, 114);
    public static final Rectangle2D LESSER_HULK = new Rectangle2D(120, 302, 106, 158);
    
    //Boss images
    public static final Rectangle2D BOSS_ONE = new Rectangle2D(0, 1805, 252, 195);
    public static final Rectangle2D BOSS_ONE_LEFT_PAW = new Rectangle2D(264, 1805, 72, 170);
    public static final Rectangle2D BOSS_ONE_RIGHT_PAW = new Rectangle2D(340, 1805, 72, 170);
    
    public static final Rectangle2D BOSS_TWO = new Rectangle2D(242, 701, 76, 166);
    
    //player's weapon images
    public static final Rectangle2D L_LASER_IMAGE = new Rectangle2D(360, 1, 2, 30);
    public static final Rectangle2D DFMISSILE_IMAGE = new Rectangle2D(364, 1, 6, 36);
    public static final Rectangle2D SONIC_IMAGE = new Rectangle2D(375, 1, 42, 30);
    
    //item images
    public static final Rectangle2D ITEM_HP = new Rectangle2D(0, 102, 20, 22);
    
    
    //enemy weapon images
    public static final Rectangle2D FIREBALL_IMAGE = new Rectangle2D(480, 1, 8, 14);
    public static final Rectangle2D SHT_IMAGE = new Rectangle2D(540, 1, 25, 25);
    public static final Rectangle2D ROCKET_IMAGE = new Rectangle2D(571, 1, 13, 46);
    public static final Rectangle2D SPREADFIRE_IMAGE = new Rectangle2D(480, 20, 10, 10);
    public static final Rectangle2D HEAVYLASER_LAUNCH_IMAGE = new Rectangle2D(506, 3, 14, 11);
    public static final Rectangle2D HEAVYLASER_BEAM_IMAGE = new Rectangle2D(494, 3, 8, 11);

    //weapon names
    public static final String LIGHTLASER = "LIGHTLASER";
    public static final String DFMISSILE = "DFMISSILE";
    public static final String SONICCANNON = "SONICCANNON";
    public static final String FIREBALL = "FIREBALL";
    public static final String ROCKET = "ROCKET";
    public static final String TWIN_FIREBALL = "TWIN_FIREBALL";
    public static final String FIREBALL_DIR = "FIREBALL DIRECTIONAL";
    public static final String SHT_DIR = "DOG SHIT";
    public static final String HEAVYLASER = "HEAVYLASER";
    public static final String SPREADFIRE = "SPREADFIRE";    
    
    //misc images
    public static final Rectangle2D CLOUD_1 = new Rectangle2D(0, 1502, 600, 296);
    public static final Rectangle2D CLOUD_2 = new Rectangle2D(0, 1502, 600, 296);
    public static final Rectangle2D CLOUD_3 = new Rectangle2D(0, 1502, 600, 296);
    public static final Rectangle2D INTRO_CAT = new Rectangle2D(0, 701, 230, 230);

    //weapon sounds
    public static final String L_LASER_SOUND = new String("/sounds/sfx/LLASER.wav");
    public static final String DFMISSILE_SOUND = new String("/sounds/sfx/DFMISSILE.wav");
    public static final String SONICCANNON_SOUND = new String("/sounds/sfx/SONIC.wav");
    
    //enemy sounds
    public static final String DOG_BARK = new String("/sounds/sfx/DOGYAP.wav");
    public static final String DOG_FART = new String("/sounds/sfx/FART.wav");

    //other sound effects
    public static final String BOOM_1 = new String("/sounds/sfx/BOOM.wav");
    public static final String BOMB = new String("/sounds/sfx/BOMB.wav");
    public static final String HIT_1 = new String("/sounds/sfx/HIT.wav");
    public static final String HIT_2 = new String("/sounds/sfx/HIT2.wav");
    public static final String PLAYER_HIT = new String("/sounds/sfx/MEOW.wav");
    public static final String LEVEL_END = new String("/sounds/sfx/LEVEL_END.wav");
    public static final String PANE_SLIDE = new String("/sounds/sfx/PANESLIDE.wav");
    public static final String TITLE = new String("/sounds/sfx/TITLE.wav");
    public static final String EMPTY = new String("/sounds/sfx/EMPTY.wav");
    public static final String ITEM_HEART_COLLECTED = new String("/sounds/sfx/SWIPE1.wav");
        
    //music
    public static final String LEVEL_1_MUSIC = new String("/sounds/music/level1.mp3");
    public static final String LEVEL_2_MUSIC = new String("/sounds/music/level2.mp3");
    public static final String MENU_MUSIC = new String("/sounds/music/menu.mp3");
    
    //GUI elements
    public static final Rectangle2D DASHBOARD = new Rectangle2D(480, 0, 118, 100);
    public static final Rectangle2D DASHBOARD_LINE = new Rectangle2D(0, 1401, 350, 50);
}
