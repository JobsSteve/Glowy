// Assets holds all the games globals, when the OpenGL surface is lost 
// all can be reloaded with a call to reload() in the GlowyGame's Activity & Renderer
// onSurfaceCreated()


package com.twopointfived.glowy;

import com.twopointfived.engine.Music;
import com.twopointfived.engine.Sound;
import com.twopointfived.engine.gl.Animation;
import com.twopointfived.engine.gl.Font;
import com.twopointfived.engine.gl.Texture;
import com.twopointfived.engine.gl.TextureRegion;
import com.twopointfived.engine.impl.GLGame;

public class Assets {
    public static Texture background;
    public static Texture backgroundGame;
    public static TextureRegion backgroundRegion;
    public static TextureRegion backgroundRegionGame;
    
    public static Texture items;  
    // public static Texture text;   
    public static TextureRegion mainMenu;
    public static TextureRegion pauseMenu;
    public static TextureRegion ready;
    public static TextureRegion gameOver;
    public static TextureRegion highScoresRegion;
    public static TextureRegion logo;
    public static TextureRegion soundOn;
    public static TextureRegion soundOff;
    public static TextureRegion arrow;
    public static TextureRegion pause;    
    public static TextureRegion glowBall;
    
    public static TextureRegion UIcircle;
    
    public static Animation coinAnim;
    
    public static Animation glowyJump;
    public static Animation glowyFall;
    public static TextureRegion glowyHit;
    
    public static Animation starFly;
    public static TextureRegion platform;
    public static Animation brakingPlatform;    
    public static Font font;
    
    public static Music music;
    public static Sound jumpSound;
    public static Sound highJumpSound;
    public static Sound hitSound;
    public static Sound coinSound;
    public static Sound clickSound;

    public static void load(GLGame game) {
        background = new Texture(game, "background.png");
        backgroundGame = new Texture(game, "backgroundgame.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 960, 640);
        backgroundRegionGame = new TextureRegion(background, 0, 0, 960, 640);
              
        items = new Texture(game, "items.png");
        
        //text = new Texture(game, "text.png"); 
        font = new Font(items, 224*2, 0, 16, 16*2, 20*2);
        
        // Correctly mapped
        gameOver = new TextureRegion(items, 735, 541, 253, 72);
        ready = new TextureRegion(items, 655, 449, 354, 71);
        logo = new TextureRegion(items, 0, 730, 552, 292);
        mainMenu = new TextureRegion(items, 56, 457, 480, 271);
        highScoresRegion = new TextureRegion(items, 581, 730, 397, 86);
        pauseMenu = new TextureRegion(items, 465, 290, 346, 134);
        UIcircle = new TextureRegion(items, 594, 831, 177, 177);
        glowBall = new TextureRegion(items, 891, 640, 77, 77);
        
        // untouched mappings
        soundOff = new TextureRegion(items, 0, 0, 64*2, 64*2);
        soundOn = new TextureRegion(items, 64*2, 0, 64*2, 64*2);
        arrow = new TextureRegion(items, 0, 64*2, 64*2, 64*2);
        pause = new TextureRegion(items, 64*2, 64*2, 64*2, 64*2);
        
        coinAnim = new Animation(0.2f,                                 
                                 new TextureRegion(items, 128*2, 32*2, 32*2, 32*2),
                                 new TextureRegion(items, 160*2, 32*2, 32*2, 32*2),
                                 new TextureRegion(items, 192*2, 32*2, 32*2, 32*2),
                                 new TextureRegion(items, 160*2, 32*2, 32*2, 32*2));
        
        
        
        glowyJump = new Animation(0.2f,
                                new TextureRegion(items, 0, 128*2, 64, 64),
                                new TextureRegion(items, 64, 128*2, 64, 64));
           
        
        glowyFall = new Animation(0.2f,
                  new TextureRegion(items, 128, 128*2, 64, 64),
                  new TextureRegion(items, 192, 128*2, 64, 64));
        
        glowyHit = new TextureRegion(items, 256, 128*2, 64, 64);
        
        
        
        starFly = new Animation(0.2f, 
                                    new TextureRegion(items, 0, 160*2, 32*2, 32*2),
                                    new TextureRegion(items, 32*2, 160*2, 32*2, 32*2));
        platform = new TextureRegion(items, 64*2, 160*2, 64*2, 16*2);
        brakingPlatform = new Animation(0.2f,
                                     new TextureRegion(items, 64*2, 160*2, 64*2, 16*2),
                                     new TextureRegion(items, 64*2, 176*2, 64*2, 16*2),
                                     new TextureRegion(items, 64*2, 192*2, 64*2, 16*2),
                                     new TextureRegion(items, 64*2, 208*2, 64*2, 16*2));
        
        music = game.getAudio().newMusic("music.mp3");
        music.setLooping(true);
        music.setVolume(0.3f);
        if(Settings.soundEnabled)
            music.play();
        jumpSound = game.getAudio().newSound("jump.ogg");
        highJumpSound = game.getAudio().newSound("highjump.ogg");
        hitSound = game.getAudio().newSound("hit.ogg");
        coinSound = game.getAudio().newSound("coin.ogg");
        clickSound = game.getAudio().newSound("click.ogg");       
    }       

    public static void reload() {
        background.reload();
        items.reload();
        if(Settings.soundEnabled)
            music.play();
    }

    public static void playSound(Sound sound) {
        if(Settings.soundEnabled)
            sound.play(1);
    }
}
