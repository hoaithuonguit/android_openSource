package com.jumpdontdie.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.jumpdontdie.MainGame;

/**
 * Created by Admin on 1/12/2017.
 */

public class Box2DScreen extends BaseScreen {

    public Box2DScreen(MainGame game) {
        super(game);
    }

    private World world;
    private Box2DDebugRenderer renderer;
    private OrthographicCamera camera;

    private Body playerBody, floorBody, skewerBody;
    private Fixture playerFixture, floorFixture, skewerFixture;

    private boolean hasCollised, canJump, Jumping, isAlive = true;

    @Override
    public void show() {

        world = new World(new Vector2(0, -10), true);
        renderer = new Box2DDebugRenderer();
        //camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = new OrthographicCamera(16, 9);
        camera.translate(0, 1);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();

                if ((fixtureA.getUserData().equals("player")&& fixtureB.getUserData().equals("floor"))||
                        (fixtureA.getUserData().equals("floor")&& fixtureB.getUserData().equals("player"))) {

                    if (fixtureA == playerFixture && fixtureB == floorFixture) {
                        if (Gdx.input.isTouched())
                            canJump = true;
                        Jumping = false;
                    }
                }
                if ((fixtureA.getUserData().equals("player")&& fixtureB.getUserData().equals("spike"))||
                        (fixtureA.getUserData().equals("spike")&& fixtureB.getUserData().equals("player"))){
                    isAlive = false;
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();
                if (fixtureA == playerFixture && fixtureB == floorFixture) {
                    Jumping = true;
                }

                if (fixtureA == floorFixture && fixtureB == playerFixture) {
                    Jumping = true;
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        playerBody = world.createBody(createPlayerBodyDef());
        floorBody = world.createBody(createFloorBodyDef());
        skewerBody = world.createBody(createSkewerBodyDef(0.5f));

        PolygonShape playerShape = new PolygonShape();
        playerShape.setAsBox(0.5f, 0.5f);
        playerFixture = playerBody.createFixture(playerShape, 1);
        playerShape.dispose();

        PolygonShape floorShape = new PolygonShape();
        floorShape.setAsBox(500, 1);
        floorFixture = floorBody.createFixture(floorShape, 1);
        floorShape.dispose();

        skewerFixture = createSkewerFixture(skewerBody);

        playerFixture.setUserData("player");
        floorFixture.setUserData("floor");
        skewerFixture.setUserData("spike");
    }

    private BodyDef createPlayerBodyDef() {
        BodyDef def = new BodyDef();
        def.position.set(-5, -1);
        def.type = BodyDef.BodyType.DynamicBody;

        return def;
    }

    private BodyDef createFloorBodyDef() {
        BodyDef def = new BodyDef();
        def.position.set(0, -1);
        //def.type = BodyDef.BodyType.StaticBody;

        return def;
    }

    private BodyDef createSkewerBodyDef(float x) {
        BodyDef def = new BodyDef();
        def.position.set(x, 0.5f);
        //def.type = BodyDef.BodyType.StaticBody;

        return def;
    }

    private Fixture createSkewerFixture(Body skewerBody) {
        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(-0.5f, -0.5f);
        vertices[1] = new Vector2(0.5f, -0.5f);
        vertices[2] = new Vector2(0, 0.5f);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        Fixture fix = skewerBody.createFixture(shape, 1);
        shape.dispose();
        return fix;
    }

    @Override
    public void dispose() {
        playerBody.destroyFixture(playerFixture);
        skewerBody.destroyFixture(skewerFixture);
        floorBody.destroyFixture(floorFixture);
        world.destroyBody(playerBody);
        world.destroyBody(skewerBody);
        world.dispose();
        renderer.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(canJump){
            canJump = false;
            jump();
        }

        if (Gdx.input.justTouched()&&!Jumping) {
            canJump = true;
        }

        if (isAlive) {
            float velocityY = playerBody.getLinearVelocity().y;
            playerBody.setLinearVelocity(8, velocityY);
        }

        world.step(delta, 6, 2);

        camera.update();
        renderer.render(world, camera.combined);
    }

    private void jump() {
        Vector2 position = playerBody.getPosition();
        playerBody.applyLinearImpulse(0, 5, position.x, position.y, true);
    }
}
