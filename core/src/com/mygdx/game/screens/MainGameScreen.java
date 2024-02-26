package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.SpaceGame;

public class MainGameScreen implements Screen {

    public SpaceGame game;

    Texture img;

    float x, y, width, height;
    final float ACCN = 1000; // Acceleration in pixels per second squared
    final float MAX_SPEED = 500; // Maximum speed in pixels per second
    float velocityX, velocityY;
    int prevKey;

    public MainGameScreen(SpaceGame game) {
        this.game = game;
    }
    @Override
    public void show() {

        img = new Texture("spaceship.png");

        // Initialize position and dimensions
        float scale = 0.1f;
        width = img.getWidth() * scale;
        height = img.getHeight() * scale;
        x = (Gdx.graphics.getWidth() - width) / 2;
        y = (Gdx.graphics.getHeight() - height) / 2;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        int currentKey = Keys.SPACE;

        float dt = Gdx.graphics.getDeltaTime(); // Get delta time

        // Apply acceleration
        velocityX += Math.min(ACCN * dt, MAX_SPEED);
        velocityY += Math.min(ACCN * dt, MAX_SPEED);

        if (Gdx.input.isKeyPressed(Keys.UP)) {
            y += velocityY * dt;
            currentKey = Keys.UP;
        } else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            y -= velocityY * dt;
            currentKey = Keys.DOWN;
        } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            x -= velocityX * dt;
            currentKey = Keys.LEFT;
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            x += velocityX * dt;
            currentKey = Keys.RIGHT;
        }

        if (currentKey != prevKey) {
            velocityX = velocityY = 0;
        }

        prevKey = currentKey;

        // Clamp position within the screen bounds
        x = Math.max(0, Math.min(x, Gdx.graphics.getWidth() - width));
        y = Math.max(0, Math.min(y, Gdx.graphics.getHeight() - height));

        game.batch.begin();
        game.batch.draw(img, x, y, width, height);
        game.batch.end();
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
    }

}
