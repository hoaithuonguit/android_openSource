package com.jumpdontdie.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jumpdontdie.MainGame;

/**
 * Created by Admin on 5/12/2017.
 */

public class GameOverScreen extends BaseScreen {

    private Stage stage;
    private Skin skin;
    private Image gameover;
    private TextButton retry, menu;


    public GameOverScreen(final MainGame game) {
        super(game);

        stage = new Stage(new FitViewport(640, 360));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        gameover = new Image(game.getManager().get("gameover.jpg", Texture.class));
        retry = new TextButton("Retry", skin);
        menu = new TextButton("Menu", skin);


        retry.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });

        menu.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });


        gameover.setSize(300, 200);
        gameover.setPosition(320 - gameover.getWidth()/2, 320 - gameover.getHeight()/2);
        retry.setSize(200, 80);
        menu.setSize(200, 80);
        retry.setPosition(60, 50);
        menu.setPosition(380, 50);

        stage.addActor(retry);
        stage.addActor(menu);
        stage.addActor(gameover);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

    }
}
