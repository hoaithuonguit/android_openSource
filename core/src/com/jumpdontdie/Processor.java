package com.jumpdontdie;

import com.badlogic.gdx.InputAdapter;

public class Processor extends InputAdapter {

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("Da nhan o: " + screenX + ", " + screenY);
        System.out.println("pointer = [" + pointer + "], button = [" + button + "]");
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return super.touchUp(screenX, screenY, pointer, button);
    }
}
