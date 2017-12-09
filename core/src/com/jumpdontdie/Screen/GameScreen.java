package com.jumpdontdie.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jumpdontdie.MainGame;
import com.jumpdontdie.entities.FloorEntity;
import com.jumpdontdie.entities.PlayerEntity;
import com.jumpdontdie.entities.SpikeEntity;
import com.jumpdontdie.entities.TextEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.jumpdontdie.Constants.PIXELS_IN_METER;
import static com.jumpdontdie.Constants.PLAYER_SPEED;

/**
 * Created by Admin on 4/12/2017.
 */

public class GameScreen extends BaseScreen {

    private Stage stage;
    private World world;
    private PlayerEntity player;
    private Texture playerTexture, floorTexture, spikeTexture;
    private int score, highscore;
    private Preferences preferences;

    //    private List<FloorEntity> floorList = new ArrayList<FloorEntity>();
//    private List<SpikeEntity> spikeList = new ArrayList<SpikeEntity>();
    private FloorEntity floor;
    private SpikeEntity spike;
    private TextEntity text;

    private Sound jumpSound, dieSound, biteSound;
    private Music bgMusic;
    private boolean collied = false, inScreen = true;
    private float xSpike, xFloor;
    private TextureAtlas playerAtlas;
    private Random rand = new Random();



    public GameScreen(final MainGame game) {
        super(game);
        preferences = Gdx.app.getPreferences("My Preferences");
        playerTexture = game.getManager().get("player-run.png");
        playerAtlas = new TextureAtlas(Gdx.files.internal("player-run.atlas"));
        floorTexture = game.getManager().get("cheese.png");
        spikeTexture = game.getManager().get("spike/skewer.png");
        score = 0;
        getHighscore();

        jumpSound = game.getManager().get("audio/jump1.wav");
        dieSound = game.getManager().get("audio/finish1.wav");
        biteSound = game.getManager().get("audio/bite.mp3");
        switch (rand.nextInt(1)+1){
            case 1:
                bgMusic = game.getManager().get("audio/guitarbackground2.mp3");
                break;
        }
        stage = new Stage(new FitViewport(640, 360));
        world = new World(new Vector2(0, -10), true);

        world.setContactListener(new ContactListener() {

            private boolean areCollied(Contact contact, Object userA, Object userB) {
                return (contact.getFixtureA().getUserData().equals(userA) && contact.getFixtureB().getUserData().equals(userB)) ||
                        (contact.getFixtureA().getUserData().equals(userB) && contact.getFixtureB().getUserData().equals(userA));
            }

            @Override
            public void beginContact(Contact contact) {
                if (areCollied(contact, "player", "floor")) {
                    player.setJumping(false);
                    if (Gdx.input.isTouched()) {
                        player.setMustJump(true);
                    }
                }

                if ((areCollied(contact, "player", "spike") && !collied)) { //|| !inScreen) {
                    collied = true;
                    bgMusic.stop();
                    biteSound.play();
                    dieSound.play();
                    player.setAlive(false);
                    if (score>highscore)
                        setHighscore(score);

                    stage.addAction(
                            Actions.sequence(
                                    Actions.delay(1.5f),
                                    Actions.run(new Runnable() {
                                        @Override
                                        public void run() {
                                            game.setScreen(new GameOverScreen(game));
                                        }
                                    })
                            )
                    );
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    @Override
    public void show() {
        player = new PlayerEntity(world, playerTexture, playerAtlas, new Vector2(1.5f, 1.5f));
        text = new TextEntity(world, score, highscore, new Vector2(10,10));
        floor = new FloorEntity(world, floorTexture, 0, 10000, 1);
        /*floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 12, 10, 2));
        spikeList.add(new SpikeEntity(world, spikeTexture, 6, 1));
        spikeList.add(new SpikeEntity(world, spikeTexture, 18, 2));
        spikeList.add(new SpikeEntity(world, spikeTexture, 32, 1));
        for (FloorEntity floor : floorList) {
            stage.addActor(floor);
        }
        for (SpikeEntity spike : spikeList) {
            stage.addActor(spike);
        }*/

        spike = new SpikeEntity(world, spikeTexture, 6, 1);

        stage.addActor(spike);
        stage.addActor(player);
        stage.addActor(floor);
        stage.addActor(text);


        bgMusic.setVolume(0.75f);
        bgMusic.setLooping(true);
        bgMusic.play();
    }

    @Override
    public void hide() {
        bgMusic.stop();
        player.detach();
        player.remove();
        floor.detach();
        floor.remove();
        spike.detach();
        spike.remove();

        /*for (FloorEntity floor : floorList) {
            floor.detach();
            floor.remove();
        }
        for (SpikeEntity spike : spikeList) {
            spike.detach();
            spike.remove();
        }*/
    }

    @Override
    public void render(float delta) {
        score = floor.getFloorWidth();
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        if (player.getX() > 150 && player.isAlive())
            stage.getCamera().translate(PLAYER_SPEED * delta * PIXELS_IN_METER, 0, 0);

        if (Gdx.input.justTouched() && !player.isJumping()) {
            jumpSound.play();
        }
        //player co con trong man hinh hay khong
        Vector3 position = new Vector3(player.body.getPosition().x, player.body.getPosition().y, 0);
        inScreen = stage.getCamera().frustum.pointInFrustum(position);

        xSpike = xSpike + (rand.nextInt(10) + 6);
        xFloor = floor.getX() + (rand.nextInt(10) + 6);
        xFloor = xFloor - xSpike < 5 ? xFloor + 4 : xFloor;
        float widthFloor = rand.nextInt(10) + 5;
        switch (rand.nextInt(28) + 1) {
            case 1:
                spikeTexture = game.getManager().get("spike/apple.png");
                break;
            case 2:
                spikeTexture = game.getManager().get("spike/bananas.png");
                break;
            case 3:
                spikeTexture = game.getManager().get("spike/burger.png");
                break;
            case 4:
                spikeTexture = game.getManager().get("spike/candy.png");
                break;
            case 5:
                spikeTexture = game.getManager().get("spike/cheese.png");
                break;
            case 6:
                spikeTexture = game.getManager().get("spike/chicken.png");
                break;
            case 7:
                spikeTexture = game.getManager().get("spike/cookie.png");
                break;
            case 8:
                spikeTexture = game.getManager().get("spike/creme-caramel.png");
                break;
            case 9:
                spikeTexture = game.getManager().get("spike/cupcake.png");
                break;
            case 10:
                spikeTexture = game.getManager().get("spike/dimsum.png");
                break;
            case 11:
                spikeTexture = game.getManager().get("spike/fish.png");
                break;
            case 12:
                spikeTexture = game.getManager().get("spike/french-fries.png");
                break;
            case 13:
                spikeTexture = game.getManager().get("spike/grapes.png");
                break;
            case 14:
                spikeTexture = game.getManager().get("spike/honeycomb.png");
                break;
            case 15:
                spikeTexture = game.getManager().get("spike/kebab.png");
                break;
            case 16:
                spikeTexture = game.getManager().get("spike/lobster.png");
                break;
            case 17:
                spikeTexture = game.getManager().get("spike/meatballs.png");
                break;
            case 18:
                spikeTexture = game.getManager().get("spike/milkshake.png");
                break;
            case 19:
                spikeTexture = game.getManager().get("spike/onigiri.png");
                break;
            case 20:
                spikeTexture = game.getManager().get("spike/pizza.png");
                break;
            case 21:
                spikeTexture = game.getManager().get("spike/ribs.png");
                break;
            case 22:
                spikeTexture = game.getManager().get("spike/soda.png");
                break;
            case 23:
                spikeTexture = game.getManager().get("spike/spaguetti.png");
                break;
            case 24:
                spikeTexture = game.getManager().get("spike/steak.png");
                break;
            case 25:
                spikeTexture = game.getManager().get("spike/strawberry.png");
                break;
            case 26:
                spikeTexture = game.getManager().get("spike/taco.png");
                break;
            case 27:
                spikeTexture = game.getManager().get("spike/watermelon.png");
                break;
            default:
                spikeTexture = game.getManager().get("spike/skewer.png");
                break;
        }


        spike = new SpikeEntity(world, spikeTexture, xSpike, 1);
        //floor = new FloorEntity(world, floorTexture, 12 + xFloor, widthFloor, 2);

        //stage.addActor(floor);
        stage.addActor(spike);

        stage.act();
        world.step(delta, 6, 2);

        stage.draw();

    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
    }

    public TextureAtlas getPlayerAtlas(){
        return playerAtlas;
    }

    public int getHighscore() {
        return highscore = preferences.getInteger("highscore");

    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
        preferences.putInteger("highscore",highscore);
    }
}
