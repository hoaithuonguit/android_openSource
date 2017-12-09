package com.jumpdontdie.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.jumpdontdie.Constants;

import static com.jumpdontdie.Constants.IMPULSE_JUMP;
import static com.jumpdontdie.Constants.PIXELS_IN_METER;
import static com.jumpdontdie.Constants.PLAYER_SPEED;

/**
 * Created by Admin on 4/12/2017.
 */

public class PlayerEntity extends Actor {
    private Texture texture;
    private World world;
    public Body body;
    private Fixture fixture;
    private TextureAtlas atlas;
    private int intCurrFrame = 0;
    TextureRegion currentFrame;

    private boolean alive = true, jumping = false, mustJump = false;

    public PlayerEntity(World world, Texture texture, TextureAtlas atlas, Vector2 position) {
        this.world = world;
        this.texture = texture;
        this.atlas = atlas;

        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("player");
        box.dispose();
        currentFrame = new TextureRegion(texture, atlas.findRegions("run").get(1).getRegionX(), atlas.findRegions("run").get(1).getRegionY(),
                atlas.findRegions("run").get(1).getRegionWidth(), atlas.findRegions("run").get(1).getRegionHeight());



        setSize(PIXELS_IN_METER, PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - 0.5f) * PIXELS_IN_METER,
                (body.getPosition().y - 0.5f) * PIXELS_IN_METER);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }


    @Override
    public void act(float delta) {
        if (Gdx.input.justTouched() || mustJump) {
            mustJump = false;
            jump();
        }

        if (alive) {
            float velocityY = body.getLinearVelocity().y;
            body.setLinearVelocity(PLAYER_SPEED, velocityY);
            currentFrame = new TextureRegion(texture, atlas.findRegions("run").get(intCurrFrame).getRegionX(),
                    atlas.findRegions("run").get(intCurrFrame).getRegionY(),
                    atlas.findRegions("run").get(intCurrFrame).getRegionWidth(),
                    atlas.findRegions("run").get(intCurrFrame).getRegionHeight());
        } else {
            //body.setLinearVelocity(0,0);
            body.setType(BodyDef.BodyType.StaticBody);
            currentFrame = new TextureRegion(texture,atlas.findRegions("fat").get(0).getRegionX(),
                    atlas.findRegions("fat").get(0).getRegionY(),
                    atlas.findRegions("fat").get(0).getRegionWidth(),
                    atlas.findRegions("fat").get(0).getRegionHeight());
        }

        if (jumping) {
            intCurrFrame = 2;
            body.applyForceToCenter(0, -IMPULSE_JUMP * 1.15f, true);
        } else {
            if (alive)
                intCurrFrame = (intCurrFrame + 1) % 8;
        }
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    private void jump() {
        if (!jumping) {
            jumping = true;
            Vector2 position = body.getPosition();
            body.applyLinearImpulse(0, IMPULSE_JUMP, position.x, position.y, true);
            intCurrFrame = 0;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean isMustJump() {
        return mustJump;
    }

    public void setMustJump(boolean mustJump) {
        this.mustJump = mustJump;
    }
}
