package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.SpaceGame;

public class MainMenuScreen implements Screen {

    private static final int EXIT_BUTTON_WIDTH = 250;
    private static final int EXIT_BUTTON_HEIGHT = 120;

    private static final int PLAY_BUTTON_WIDTH = 300;
    private static final int PLAY_BUTTON_HEIGHT = 120;

    private static final int EXIT_BUTTON_Y = 100;
    private static final int PLAY_BUTTON_Y = 230;
    SpaceGame game;

    Texture exitButtonActive;
    Texture exitButtonInactive;

    Texture playButtonActive;
    Texture playButtonInactive;

    public MainMenuScreen(SpaceGame game) {
        this.game = game;
        playButtonActive = new Texture("play_button_active.png");
        playButtonInactive = new Texture("play_button_inactive.png");
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.3f, 1);
        game.batch.begin();

        if (hover(EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT, EXIT_BUTTON_Y)) {
            game.batch.draw(exitButtonActive, (SpaceGame.WIDTH - EXIT_BUTTON_WIDTH) / 2, EXIT_BUTTON_Y,
                    EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                Gdx.app.exit();
            }
        } else {
            game.batch.draw(exitButtonInactive, (SpaceGame.WIDTH - EXIT_BUTTON_WIDTH) / 2, EXIT_BUTTON_Y,
                    EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }

        if (hover(PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT, PLAY_BUTTON_Y)) {
            game.batch.draw(playButtonActive, (SpaceGame.WIDTH - PLAY_BUTTON_WIDTH) / 2, PLAY_BUTTON_Y,
                    PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                this.dispose();
                game.setScreen(new MainGameScreen(game));
            }
        } else {
            game.batch.draw(playButtonInactive, (SpaceGame.WIDTH - PLAY_BUTTON_WIDTH) / 2, PLAY_BUTTON_Y,
                    PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        game.batch.end();
    }

    public boolean hover(int objectWidth, int objectHeight, int objectY) {

        if ((SpaceGame.WIDTH - objectWidth) / 2 < Gdx.input.getX()
                && Gdx.input.getX() < (SpaceGame.WIDTH + objectWidth) / 2
                && SpaceGame.HEIGHT - (objectHeight + objectY) < Gdx.input.getY()
                && Gdx.input.getY() < SpaceGame.HEIGHT - objectY) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        exitButtonActive.dispose();
        exitButtonInactive.dispose();
        playButtonActive.dispose();
        playButtonInactive.dispose();
    }

}
