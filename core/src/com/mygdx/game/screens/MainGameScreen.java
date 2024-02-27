package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.SpaceGame;

public class MainGameScreen implements Screen {
    // spaceship 17x32 aspectratio
    public SpaceGame game;

    public static final int SHIP_WIDTH_PIXELS = 17;
    public static final int SHIP_HEIGHT_PIXELS = 32;

    public static final int SHIP_WIDTH = SHIP_WIDTH_PIXELS * 3;
    public static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXELS * 3;

    public static final float SHIP_ANIMATION_SPEED = 0.5f;

    public static final float ROLL_TIMER_SWITCH_TIME = 0.25f;

    Animation<TextureRegion>[] rolls;

    float x, y;
    int roll;
    float rollTimer;
    float stateTime;

    final float ACCN = 1000; // Acceleration in pixels per second squared
    final float MAX_SPEED = 500; // Maximum speed in pixels per second
    float velocityX, velocityY;
    int prevKey;

    public MainGameScreen(SpaceGame game) {
        this.game = game;

        y = 15;
        x = (SpaceGame.WIDTH - SHIP_WIDTH) / 2;

        roll = 2;
        rolls = new Animation[5];
        rollTimer = 0;
        TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("ship.png"), SHIP_WIDTH_PIXELS,
                SHIP_HEIGHT_PIXELS);

        rolls[0] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[2]);// left roll
        rolls[1] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[1]);// left low roll
        rolls[2] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[0]);// normal
        rolls[3] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[3]);// right low roll
        rolls[4] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[4]);// right roll
    }

    @Override
    public void show() {
        // img = new Texture("spaceship.png");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        int currentKey = Keys.SPACE;

        float dt = Gdx.graphics.getDeltaTime(); // Get delta time

        // Apply acceleration and rolling Animation
        // ___________________________________________________________________________
        velocityX += Math.min(ACCN * dt, MAX_SPEED);
        velocityY += Math.min(ACCN * dt, MAX_SPEED);

        // Handling left roll and return
        // ___________________________________________________________________________
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            x -= velocityX * dt;
            currentKey = Keys.LEFT;
            if (x < 0)
                x = 0;// left bounds
            // update roll if button just clicked
            if (Gdx.input.isKeyJustPressed(Keys.LEFT) && !Gdx.input.isKeyJustPressed(Keys.RIGHT) && roll > 0) {
                rollTimer = 0;
                roll--;
            }

            rollTimer -= dt;// update roll;
            if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll > 0) {
                rollTimer = 0;
                roll--;
            }
        } else {
            if (roll < 2) {
                // Update roll to make it go back to center
                rollTimer += Gdx.graphics.getDeltaTime();
                if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
                    rollTimer = 0;
                    roll++;
                }
            }
        }
        // ___________________________________________________________________________

        // Handling right roll and return
        // ___________________________________________________________________________
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            x += velocityX * dt;
            currentKey = Keys.RIGHT;
            if (x + SHIP_WIDTH > SpaceGame.WIDTH)
                x = SpaceGame.WIDTH - SHIP_WIDTH;// right bounds

            rollTimer += dt;// update roll;
            if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
                rollTimer = 0;
                roll++;
            }
        } else {
            if (roll > 2) {
                // Update roll
                rollTimer -= Gdx.graphics.getDeltaTime();
                if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll > 0) {
                    rollTimer = 0;
                    roll--;
                }
            }
        }
        // ___________________________________________________________________________

        if (currentKey != prevKey) {
            velocityX = velocityY = 0;
        }

        prevKey = currentKey;
        // ___________________________________________________________________________

        stateTime += delta;
        game.batch.begin();

        game.batch.draw(rolls[roll].getKeyFrame(stateTime, true), x, y, SHIP_WIDTH, SHIP_HEIGHT);

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
