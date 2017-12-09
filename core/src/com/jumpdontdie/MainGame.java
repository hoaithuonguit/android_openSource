package com.jumpdontdie;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.jumpdontdie.Screen.LoadingScreen;
import com.jumpdontdie.Screen.MenuScreen;

public class MainGame extends Game {

    private AssetManager manager;

    public AssetManager getManager() {
        return manager;
    }

    @Override
    public void create() {
        manager = new AssetManager();
        manager.load("cheese.png", Texture.class);
        manager.load("overfloor.png", Texture.class);
        //player
        manager.load("player.png", Texture.class);
        manager.load("player-run.atlas", TextureAtlas.class);
        manager.load("logo.png", Texture.class);
        //spike
        manager.load("spike/skewer.png", Texture.class);
        manager.load("spike/apple.png", Texture.class);
        manager.load("spike/bananas.png", Texture.class);
        manager.load("spike/burger.png", Texture.class);
        manager.load("spike/candy.png", Texture.class);
        manager.load("spike/cheese.png", Texture.class);
        manager.load("spike/chicken.png", Texture.class);
        manager.load("spike/cookie.png", Texture.class);
        manager.load("spike/creme-caramel.png", Texture.class);
        manager.load("spike/cupcake.png", Texture.class);
        manager.load("spike/dimsum.png", Texture.class);
        manager.load("spike/fish.png", Texture.class);
        manager.load("spike/french-fries.png", Texture.class);
        manager.load("spike/grapes.png", Texture.class);
        manager.load("spike/honeycomb.png", Texture.class);
        manager.load("spike/kebab.png", Texture.class);
        manager.load("spike/lobster.png", Texture.class);
        manager.load("spike/meatballs.png", Texture.class);
        manager.load("spike/milkshake.png", Texture.class);
        manager.load("spike/onigiri.png", Texture.class);
        manager.load("spike/pizza.png", Texture.class);
        manager.load("spike/ribs.png", Texture.class);
        manager.load("spike/soda.png", Texture.class);
        manager.load("spike/spaguetti.png", Texture.class);
        manager.load("spike/steak.png", Texture.class);
        manager.load("spike/strawberry.png", Texture.class);
        manager.load("spike/taco.png", Texture.class);
        manager.load("spike/watermelon.png", Texture.class);


        manager.load("gameover.jpg", Texture.class);

        //sound
        manager.load("audio/jump1.wav", Sound.class);
        manager.load("audio/finish1.wav", Sound.class);
        manager.load("audio/bite.mp3", Sound.class);
        manager.load("audio/guitarbackground2.mp3", Music.class);
        manager.finishLoading();

        setScreen(new LoadingScreen(this));
    }

    public void finishLoading() {
        setScreen(new MenuScreen(this));
    }
}