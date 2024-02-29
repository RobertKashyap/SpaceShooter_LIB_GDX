package com.mygdx.game.screens;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.SpaceGame;
import com.mygdx.game.entites.Asteroid;
import com.mygdx.game.entites.Bullet;

public class MainGameScreen implements Screen {
    // spaceship 17x32 aspectratio
    public SpaceGame game;

    public static final int SHIP_WIDTH_PIXELS = 17;
    public static final int SHIP_HEIGHT_PIXELS = 32;

    public static final int SHIP_WIDTH = SHIP_WIDTH_PIXELS * 3;
    public static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXELS * 3;

    public static final float SHIP_ANIMATION_SPEED = 0.5f;

    public static final float ROLL_TIMER_SWITCH_TIME = 0.25f;

    public static final float SHOOT_WAIT_TIME = 0.3f;

    public static final float MIN_ASTEROID_SPAWN_TIME = 0.3f;
    public static final float MAX_ASTEROID_SPAWN_TIME = 0.6f;

    Animation<TextureRegion>[] rolls;

    float x, y;
    int roll;
    float rollTimer;
    float stateTime;
    float shootTimer;
    float asteroidSpawnTimer;

    Random random;

    final float ACCN = 1000; // Acceleration in pixels per second squared
    final float MAX_SPEED = 500; // Maximum speed in pixels per second
    float velocityX, velocityY;
    int prevKey;

    ArrayList<Bullet> bullets;
    ArrayList<Asteroid> asteroids;

    public MainGameScreen(SpaceGame game) {
        this.game = game;

        y = 15;
        x = (SpaceGame.WIDTH - SHIP_WIDTH) / 2;
        bullets = new ArrayList<>();
        asteroids = new ArrayList<>();

        random = new Random();
        asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME)
                + MIN_ASTEROID_SPAWN_TIME;

        roll = 2;
        rolls = new Animation[5];
        rollTimer = 0;
        shootTimer = 0;
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

        int currentKey = Keys.CONTROL_LEFT;

        float dt = Gdx.graphics.getDeltaTime(); // Get delta time

        // Bullet Shoot Code
        // ___________________________________________________________________________
        shootTimer += dt;
        if (Gdx.input.isKeyPressed(Keys.SPACE) && shootTimer >= SHOOT_WAIT_TIME) {
            shootTimer = 0;

            int offset = 4;// higher offset => closer bullets
            if (roll == 1 || roll == 3) {
                offset = 8;// slight tilt close offset
            }
            if (roll == 0 || roll == 4) {
                offset = 16;// full tilt very close offset
            }

            bullets.add(new Bullet(x + offset));
            bullets.add(new Bullet(x + SHIP_WIDTH - offset));
        }
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.update(dt);
            if (bullet.remove) {
                bulletsToRemove.add(bullet);
            }
        }
        // ___________________________________________________________________________

        // Asteroid spawn code
        // ___________________________________________________________________________
        asteroidSpawnTimer -= dt;
        if (asteroidSpawnTimer <= 0) {
            asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME)
                    + MIN_ASTEROID_SPAWN_TIME;
            asteroids.add(new Asteroid((random.nextInt(SpaceGame.WIDTH) - Asteroid.WIDTH)));
        }

        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<>();
        for (Asteroid asteroid : asteroids) {
            asteroid.update(dt);
            if (asteroid.remove) {
                asteroidsToRemove.add(asteroid);
            }
        }

        // ___________________________________________________________________________

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
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll--;
            }
        } else {
            if (roll < 2) {
                // Update roll to make it go back to center
                rollTimer += Gdx.graphics.getDeltaTime();
                if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
                    rollTimer -= ROLL_TIMER_SWITCH_TIME;
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
            // update roll if button just clicked
            if (Gdx.input.isKeyJustPressed(Keys.RIGHT) && !Gdx.input.isKeyJustPressed(Keys.LEFT) && roll < 4) {
                rollTimer = 0;
                roll++;
            }
            rollTimer += dt;// update roll;
            if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll++;
            }
        } else {
            if (roll > 2) {
                // Update roll
                rollTimer -= Gdx.graphics.getDeltaTime();
                if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll > 0) {
                    rollTimer -= ROLL_TIMER_SWITCH_TIME;
                    roll--;
                }
            }
        }
        // ___________________________________________________________________________

        if (currentKey != prevKey) {
            velocityX = velocityY = 0;
        }

        prevKey = currentKey;
        // Movement ends here
        // ___________________________________________________________________________

        // Collision detection
        // after all updation of motion
        // ___________________________________________________________________________
        for (Bullet bullet : bullets) {
            for (Asteroid asteroid : asteroids) {
                if (bullet.getCollisionRect().collidesWith(asteroid.getCollisionRect())) {
                    bulletsToRemove.add(bullet);
                    asteroidsToRemove.add(asteroid);
                }
            }
        }
        // ___________________________________________________________________________
        asteroids.removeAll(asteroidsToRemove);
        bullets.removeAll(bulletsToRemove);

        stateTime += delta;

        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.begin();

        for (Bullet bullet : bullets) {
            bullet.render(game.batch);
        }

        for (Asteroid asteroid : asteroids) {
            asteroid.render(game.batch);
        }

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
