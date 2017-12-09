package com.jumpdontdie.scene2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class Player extends Actor {

    private Texture ttplayer;

    private boolean alive;

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Player(Texture ttplayer){
        this.alive = true;
        this.ttplayer = ttplayer;
        setSize(ttplayer.getWidth(), ttplayer.getHeight());
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(ttplayer, getX(), getY());
    }


}
