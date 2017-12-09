package com.jumpdontdie.scene2d.Barrier;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class Skewer extends Actor {

    private TextureRegion skewer;

    public Skewer(TextureRegion skewer) {
        this.skewer = skewer;
        setSize(skewer.getRegionWidth(), skewer.getRegionHeight());
    }

    @Override
    public void act(float delta) {
        setX(getX()-250*delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(skewer, getX(), getY());
    }
}
