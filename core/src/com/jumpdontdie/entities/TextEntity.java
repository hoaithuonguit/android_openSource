package com.jumpdontdie.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.jumpdontdie.Constants.PIXELS_IN_METER;
import static com.jumpdontdie.Constants.PLAYER_SPEED;

/**
 * Created by Admin on 8/12/2017.
 */

public class TextEntity extends Actor {

    BitmapFont font;
    int myScore, highscore;
    private World world;
    private Fixture fixture;
    public Body body;

    public TextEntity(World world, int myScore, int highscore, Vector2 position){
        this.world = world;
        this.myScore = myScore;
        this.highscore = highscore;

        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        font = new BitmapFont();
        font.setColor(0.5f,0.4f,0,1);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - 0.5f) * PIXELS_IN_METER,
                (body.getPosition().y - 0.5f) * PIXELS_IN_METER);
        font.draw(batch, "Score: " + myScore + "    HighScore: "+ highscore,body.getPosition().x, body.getPosition().y);
    }

    @Override
    public void act(float delta) {
        body.setLinearVelocity(PLAYER_SPEED, 0);

    }
}
