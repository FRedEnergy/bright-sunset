package ru.redenergy.bs.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends Entity{

    private Sprite charTexture;
    public Player(World world, int x, int y) {
        super(world, x, y);
        charTexture = new Sprite(new Texture(Gdx.files.internal("char/pistol/idle/idle_pistol_0.png")));
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        charTexture.setScale(0.25F);
        charTexture.setPosition(body.getPosition().x - charTexture.getWidth() / 2, body.getPosition().y - charTexture.getHeight() / 2);
        charTexture.setRotation((float) Math.toDegrees(this.body.getAngle()));
        charTexture.draw(batch);
    }
}
