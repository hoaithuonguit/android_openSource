package com.jumpdontdie.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jumpdontdie.Screen.BaseScreen;
import com.jumpdontdie.MainGame;


public class Scene2DScreen extends BaseScreen {

    public Scene2DScreen(MainGame game) {
        super(game);
        ttPlayer = new Texture("badlogic.jpg");
        ttSkewer = new Texture("skewer.png");
        regionSkewer = new TextureRegion(ttSkewer,0,0,128,128);
    }

    private Stage stage;
    private Player player;
    private com.jumpdontdie.scene2d.Barrier.Skewer skewer;

    private Texture ttPlayer, ttSkewer;

    private TextureRegion regionSkewer;


    @Override
    public void show() {

        stage = new Stage();
        stage.setDebugAll(true);

        player = new Player(ttPlayer);
        skewer = new com.jumpdontdie.scene2d.Barrier.Skewer(regionSkewer);
        stage.addActor(player);
        stage.addActor(skewer);

        player.setPosition(20, 100);
        skewer.setPosition(500, 100);

    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        compareCollision();
        stage.draw();
    }

    private void compareCollision(){
        if (player.isAlive() && player.getX()+player.getWidth()>skewer.getX()){
            System.out.println("Collision!!!");
            player.setAlive(false);
        }
    }

    @Override
    public void dispose() {
        ttPlayer.dispose();
    }
}
