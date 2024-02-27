package com.mygdx.game.entites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.SpaceGame;

public class Bullet {
    public static final int SPEED = 500;
    public static final int DEFAULT_Y = 40;
    private static Texture texture;

    float x,y;

    public boolean remove =false;

    public Bullet(float x, float y) {
        this.x = x;
        this.y = DEFAULT_Y;
        if(texture == null)
            texture = new Texture("bullet.png");
    }

    public void update(float dt){
        y+=SPEED*dt;
        if(y>SpaceGame.HEIGHT)
            remove=true;
    }

    public void render (SpriteBatch batch){
        batch.draw(texture,x,y);
    }
}
